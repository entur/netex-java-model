#!/usr/bin/env python3
"""
Generate the JAXB bindings that hide the NeTEx "_Dummy" naming convention.

NeTEx 2.0 declares the abstract substitution-group heads with a "_Dummy" suffix
(for example DayType_Dummy, head of DayType). Without customisation, xjc leaks the
suffix into the generated API: getDayType_Dummy(), getDayTypeRefOrDayType_Dummy(),
createDayType_Dummy(...) and classes such as MediumAccessDevice_Dummy.

This script walks the schema files and emits one binding per leak:

  * a property rename for every element referencing a _Dummy head
    (getDayType_Dummy() -> getDayType()); when the reference sits in a repeated
    model group, the group itself is renamed (getDayTypeRefOrDayType())
  * a factory-method rename for every _Dummy element declaration
    (createDayType_Dummy(...) -> createDayType_(...)); dropping the suffix entirely
    would collide with the factory method of the concrete element
  * a class rename for the _Dummy elements with an inline type and for the
    _Dummy complex types (MediumAccessDevice_Dummy -> MediumAccessDevice_)

The generated block is written into the bindings file between the BEGIN/END
markers below (and appended before the closing tag if the markers are absent).
Re-run it after upgrading the NeTEx schema:

    bin/generate-dummy-bindings.py src/main/resources/xsd/2.0/NeTEx_publication.xsd bindings.xjb

Only the schema files reachable from the entry point through xsd:include and
xsd:import are visited; xjc rejects bindings for files outside the compilation.
"""

import pathlib
import sys
import xml.etree.ElementTree as ET

XS = "http://www.w3.org/2001/XMLSchema"
SUFFIX = "_Dummy"
BEGIN = "<!-- BEGIN generated _Dummy bindings (bin/generate-dummy-bindings.py) - do not edit by hand -->"
END = "<!-- END generated _Dummy bindings -->"
PARTICLES = {"element", "group", "choice", "sequence", "all", "any"}
MODEL_GROUPS = {"choice", "sequence", "all"}


def tag(node):
    return node.tag.split("}")[1]


def is_particle(node):
    return tag(node) in PARTICLES


def repeated(node):
    max_occurs = node.get("maxOccurs", "1")
    return max_occurs == "unbounded" or int(max_occurs) > 1


def lower_first(name):
    return name[0].lower() + name[1:]


def label(node):
    """Mirror xjc's ParticleBinder.computeLabel for the default property name."""
    kind = tag(node)
    if kind == "element":
        return node.get("name") or node.get("ref")
    if kind == "group":
        return node.get("ref")
    if kind == "any":
        return "any"
    # xjc only uses the first three particles of a model group for its name.
    return "Or".join(label(c) for c in [c for c in node if is_particle(c)][:3])


def xpath_step(node, parent):
    """One XPath step, with a positional predicate only when siblings share the tag."""
    siblings = [c for c in parent if c.tag == node.tag]
    step = "xsd:" + tag(node)
    if len(siblings) > 1:
        step += "[%d]" % (siblings.index(node) + 1)
    return step


def xpath(node, parents, schema):
    """Absolute path from the named top-level component down to node."""
    chain = []
    current = node
    while parents[current] is not schema:
        chain.append(current)
        current = parents[current]
    top = current
    if top.get("name") is None:
        raise SystemExit("top-level component without a name: %s" % top.tag)
    path = "//xsd:%s[@name = '%s']" % (tag(top), top.get("name"))
    for n in reversed(chain):
        path += "/" + xpath_step(n, parents[n])
    return path


def property_target(ref_element, parents):
    """The node xjc binds to a property for this reference: the outermost repeated
    model group containing it, or the element itself when there is none."""
    target = ref_element
    current = parents[ref_element]
    while tag(current) in MODEL_GROUPS:
        if repeated(current):
            target = current
        current = parents[current]
    return target


def process_schema(xsd_path):
    schema = ET.parse(xsd_path).getroot()
    parents = {c: p for p in schema.iter() for c in p}
    properties = []  # (xpath, new name, old name)
    factories = []  # (element name, new name)
    classes = []  # (xpath, new name)
    for node in schema.iter():
        kind = tag(node)
        if kind == "element" and (node.get("ref") or "").endswith(SUFFIX):
            target = property_target(node, parents)
            old = lower_first(label(target))
            new = old.replace(SUFFIX, "")
            properties.append((xpath(target, parents, schema), new, old))
        elif kind == "element" and (node.get("name") or "").endswith(SUFFIX) and parents[node] is schema:
            name = node.get("name")
            bare = name[: -len(SUFFIX)]
            if any(tag(c) == "complexType" for c in node):
                classes.append((xpath(node, parents, schema), bare + "_"))
            else:
                factories.append((name, bare + "_"))
        elif kind == "complexType" and (node.get("name") or "").endswith(SUFFIX) and parents[node] is schema:
            bare = node.get("name")[: -len(SUFFIX)]
            classes.append((xpath(node, parents, schema), bare + "_"))
    # The same repeated group is reached once per _Dummy reference it contains.
    properties = list(dict.fromkeys(properties))
    return properties, factories, classes


def reachable_schemas(entry):
    """All schema files included or imported, directly or not, from the entry point."""
    seen = set()
    todo = [entry]
    while todo:
        path = todo.pop().resolve()
        if path in seen:
            continue
        seen.add(path)
        for node in ET.parse(path).getroot():
            if tag(node) in ("include", "import") and node.get("schemaLocation"):
                todo.append(path.parent / node.get("schemaLocation"))
    return sorted(seen)


def render(entry, bindings_path):
    lines = ["\t" + BEGIN]
    old_names = []
    for xsd_path in reachable_schemas(entry):
        properties, factories, classes = process_schema(xsd_path)
        if not (properties or factories or classes):
            continue
        location = "./" + xsd_path.relative_to(bindings_path.parent).as_posix()
        lines.append('\t<jxb:bindings schemaLocation="%s">' % location)
        for path, new, old in properties:
            old_names.append(old)
            lines.append('\t\t<jxb:bindings node="%s">' % path)
            lines.append('\t\t\t<jxb:property name="%s" />' % new)
            lines.append("\t\t</jxb:bindings>")
        for name, new in factories:
            lines.append("\t\t<jxb:bindings node=\"//xsd:element[@name = '%s']\">" % name)
            lines.append('\t\t\t<jxb:factoryMethod name="%s" />' % new)
            lines.append("\t\t</jxb:bindings>")
        for path, new in classes:
            lines.append('\t\t<jxb:bindings node="%s">' % path)
            lines.append('\t\t\t<jxb:class name="%s" />' % new)
            lines.append("\t\t</jxb:bindings>")
        lines.append("\t</jxb:bindings>")
    lines.append("\t" + END)
    return "\n".join(lines) + "\n", old_names


def splice(bindings_path, block):
    text = bindings_path.read_text()
    begin = text.find("\t" + BEGIN)
    end = text.find(END)
    if begin >= 0 and end >= 0:
        end = text.index("\n", end) + 1
        return text[:begin] + block + text[end:]
    closing = text.rstrip().rfind("</jxb:bindings>")
    return text[:closing].rstrip() + "\n\n" + block + "\n</jxb:bindings>\n"


def main():
    if len(sys.argv) != 3:
        raise SystemExit("usage: generate-dummy-bindings.py <entry xsd> <bindings file>")
    entry = pathlib.Path(sys.argv[1]).resolve()
    bindings_path = pathlib.Path(sys.argv[2]).resolve()
    block, old_names = render(entry, bindings_path)
    bindings_path.write_text(splice(bindings_path, block))
    # Printed so the result can be checked against the names xjc generated before.
    for name in old_names:
        print(name)


if __name__ == "__main__":
    main()
