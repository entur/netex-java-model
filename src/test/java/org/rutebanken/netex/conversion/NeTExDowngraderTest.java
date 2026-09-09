package org.rutebanken.netex.conversion;

import org.junit.jupiter.api.Test;
import org.rutebanken.netex.conversion.NeTExDowngrader.Conversion;
import org.rutebanken.netex.validation.NeTExValidator;
import org.rutebanken.netex.validation.NeTExValidator.NetexVersion;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXParseException;

import javax.xml.namespace.NamespaceContext;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathFactory;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.TimeUnit;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assumptions.assumeTrue;

class NeTExDowngraderTest {

    private static final String NETEX_NS = "http://www.netex.org.uk/netex";
    private static final Path FIXTURE_1_16 = Paths.get("src/test/resources/netex-1.16-dated-service-journey.xml");
    private static final Path STYLESHEET = Paths.get("src/main/resources/xslt/netex-1.16-to-1.15.xsl");
    private static final String REPLACING_DSJ = "VYG:DatedServiceJourney:96_KMB-NK_23-12-09";
    private static final String REPLACED_DSJ = "VYG:DatedServiceJourney:8916_KVG-DEG_23-10-19";

    @Test
    void fixtureIsValid116ButInvalid115() throws Exception {
        String xml = readFixture();
        validate(NetexVersion.v1_16, xml);
        assertThatThrownBy(() -> validate(NetexVersion.v1_15, xml))
                .isInstanceOf(SAXParseException.class)
                .hasMessageContaining("replacedJourneys");
    }

    @Test
    void downgradedDocumentIsValid115() throws Exception {
        String downgraded = downgrade(readFixture());
        validate(NetexVersion.v1_15, downgraded);
    }

    @Test
    void downgradeRewritesDatedServiceJourney() throws Exception {
        Document document = parse(downgrade(readFixture()));
        XPath xpath = netexXPath();

        assertThat(xpath.evaluate("/n:PublicationDelivery/@version", document))
                .isEqualTo("1.15:NO-NeTEx-networktimetable:1.3");
        assertThat(((NodeList) xpath.evaluate("//n:replacedJourneys", document, XPathConstants.NODESET)).getLength())
                .isZero();

        Element replacing = (Element) xpath.evaluate("//n:DatedServiceJourney[@id='" + REPLACING_DSJ + "']", document, XPathConstants.NODE);
        assertThat(childElementNames(replacing))
                .containsExactly("ServiceAlteration", "ServiceJourneyRef", "DatedServiceJourneyRef", "OperatingDayRef");
        Element replacedRef = (Element) xpath.evaluate("n:DatedServiceJourneyRef", replacing, XPathConstants.NODE);
        assertThat(replacedRef.getNamespaceURI()).isEqualTo(NETEX_NS);
        assertThat(replacedRef.getAttribute("ref")).isEqualTo(REPLACED_DSJ);
        assertThat(replacedRef.getAttribute("version")).isEqualTo("1");

        // the replaced journey has no 1.16-only content and is copied unchanged
        Element replaced = (Element) xpath.evaluate("//n:DatedServiceJourney[@id='" + REPLACED_DSJ + "']", document, XPathConstants.NODE);
        assertThat(childElementNames(replaced)).containsExactly("ServiceJourneyRef", "OperatingDayRef");
    }

    @Test
    void uicOperatingPeriodIsKeptWhenThereIsNoOperatingDayRef() throws Exception {
        String xml = readFixture().replace("<OperatingDayRef ref=\"VYG:OperatingDay:2023-12-09\" version=\"1\"/>", "");
        String downgraded = downgrade(xml);
        validate(NetexVersion.v1_15, downgraded);

        Element replacing = (Element) netexXPath().evaluate("//n:DatedServiceJourney[@id='" + REPLACING_DSJ + "']", parse(downgraded), XPathConstants.NODE);
        assertThat(childElementNames(replacing))
                .containsExactly("ServiceAlteration", "ServiceJourneyRef", "DatedServiceJourneyRef", "UicOperatingPeriod");
    }

    @Test
    void bareVersionAttributeIsRewritten() throws Exception {
        String xml = "<PublicationDelivery xmlns=\"http://www.netex.org.uk/netex\" version=\"1.16\"/>";
        assertThat(netexXPath().evaluate("/n:PublicationDelivery/@version", parse(downgrade(xml)))).isEqualTo("1.15");
    }

    @Test
    void documentsWithoutDatedServiceJourneyAreCopiedUnchanged() throws Exception {
        // version="any" does not start with 1.16 and is left alone
        String xml = "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>\n" +
                "<PublicationDelivery xmlns=\"http://www.netex.org.uk/netex\" version=\"any\">\n" +
                "    <PublicationTimestamp>2016-11-29T13:32:06.869+01:00</PublicationTimestamp>\n" +
                "    <ParticipantRef>NSR</ParticipantRef>\n" +
                "</PublicationDelivery>";
        String downgraded = downgrade(xml);
        validate(NetexVersion.v1_15, downgraded);
        assertThat(parse(downgraded).isEqualNode(parse(xml))).isTrue();

        String dateTimeExamples = new String(Files.readAllBytes(Paths.get("src/test/resources/date_time_examples.xml")), StandardCharsets.UTF_8);
        assertThat(parse(downgrade(dateTimeExamples)).isEqualNode(parse(dateTimeExamples))).isTrue();
    }

    @Test
    void downgraderIsCachedPerConversion() throws Exception {
        assertThat(NeTExDowngrader.getNeTExDowngrader()).isSameAs(NeTExDowngrader.getNeTExDowngrader(Conversion.V1_16_TO_V1_15));
        assertThat(NeTExDowngrader.getNeTExDowngrader().getConversion()).isEqualTo(NeTExDowngrader.LATEST);
        assertThat(NeTExDowngrader.LATEST.getSourceVersion()).isEqualTo(NeTExValidator.LATEST);
        assertThat(NeTExDowngrader.LATEST.getTargetVersion()).isEqualTo(NetexVersion.v1_15);
    }

    /**
     * The stylesheet must stay XSLT 1.0 so that it can be applied with libxslt (xmlstarlet), independently of this library.
     */
    @Test
    void stylesheetWorksWithXmlstarlet() throws Exception {
        assumeTrue(isOnPath("xmlstarlet"), "xmlstarlet not installed");
        Process process = new ProcessBuilder("xmlstarlet", "tr", STYLESHEET.toString(), FIXTURE_1_16.toString())
                .redirectErrorStream(true)
                .start();
        String output = new String(process.getInputStream().readAllBytes(), StandardCharsets.UTF_8);
        assertThat(process.waitFor(30, TimeUnit.SECONDS)).isTrue();
        assertThat(process.exitValue()).as(output).isZero();

        validate(NetexVersion.v1_15, output);
        assertThat(parse(output).isEqualNode(parse(downgrade(readFixture())))).as("xmlstarlet and JDK outputs differ").isTrue();
    }

    private static String readFixture() throws IOException {
        return new String(Files.readAllBytes(FIXTURE_1_16), StandardCharsets.UTF_8);
    }

    private static String downgrade(String xml) throws Exception {
        StringWriter writer = new StringWriter();
        NeTExDowngrader.getNeTExDowngrader().downgrade(new StreamSource(new StringReader(xml)), new StreamResult(writer));
        return writer.toString();
    }

    private static void validate(NetexVersion version, String xml) throws Exception {
        NeTExValidator.getNeTExValidator(version).validate(new StreamSource(new StringReader(xml)));
    }

    private static Document parse(String xml) throws Exception {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        factory.setNamespaceAware(true);
        factory.setIgnoringComments(true);
        factory.setIgnoringElementContentWhitespace(true);
        Document document = factory.newDocumentBuilder().parse(new ByteArrayInputStream(xml.getBytes(StandardCharsets.UTF_8)));
        stripWhitespaceTextNodes(document.getDocumentElement());
        return document;
    }

    /** Element content whitespace is not stripped without a DTD, so do it by hand to make DOM comparison formatting-agnostic. */
    private static void stripWhitespaceTextNodes(Node node) {
        Node child = node.getFirstChild();
        while (child != null) {
            Node next = child.getNextSibling();
            if (child.getNodeType() == Node.TEXT_NODE && child.getTextContent().trim().isEmpty()) {
                node.removeChild(child);
            } else if (child.getNodeType() == Node.ELEMENT_NODE) {
                stripWhitespaceTextNodes(child);
            }
            child = next;
        }
    }

    private static List<String> childElementNames(Element element) {
        List<String> names = new ArrayList<>();
        for (Node child = element.getFirstChild(); child != null; child = child.getNextSibling()) {
            if (child.getNodeType() == Node.ELEMENT_NODE) {
                names.add(child.getLocalName());
            }
        }
        return names;
    }

    private static XPath netexXPath() {
        XPath xpath = XPathFactory.newInstance().newXPath();
        xpath.setNamespaceContext(new NamespaceContext() {
            @Override
            public String getNamespaceURI(String prefix) {
                return "n".equals(prefix) ? NETEX_NS : null;
            }

            @Override
            public String getPrefix(String namespaceURI) {
                return null;
            }

            @Override
            public Iterator<String> getPrefixes(String namespaceURI) {
                return null;
            }
        });
        return xpath;
    }

    private static boolean isOnPath(String executable) {
        String path = System.getenv("PATH");
        if (path == null) {
            return false;
        }
        for (String dir : path.split(java.io.File.pathSeparator)) {
            if (Files.isExecutable(Paths.get(dir, executable))) {
                return true;
            }
        }
        return false;
    }
}
