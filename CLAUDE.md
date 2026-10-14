# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Project Overview

**netex2-java-model** (Maven coordinates `org.entur:netex2-java-model`) is a Java library that generates JAXB model classes from NeTEx (Network Timetable Exchange) XML Schema Definition (XSD) files. The NeTEx 2.0 XSD is downloaded from https://github.com/TransmodelEcosystem/NeTEx and compiled into Java classes at build time. The legacy schemas used only for validation (1.04beta - 1.16.1) are downloaded from the Entur fork at https://github.com/entur/NeTEx.

- **Organization**: Entur AS (Norwegian public transport data provider)
- **License**: EUPL-1.2
- **Java Version**: 21+ (builds and publishes on 21 in CI)
- **Package**: `org.entur.netex2.model` (generated), `org.entur.netex2`, `org.entur.netex2.client`, `org.entur.netex2.validation` and `org.entur.netex2.util` (source). GML and SIRI types are generated in `net.opengis.gml._3` and `uk.org.siri.siri`.

## Build Commands

```bash
# Full build (downloads XSD, generates JAXB classes, compiles, tests)
mvn clean install

# Run tests only
mvn test

# Package without tests
mvn package -DskipTests

# Validate build setup
mvn validate
```

**Prerequisites**: `xmlstarlet` must be installed (`apt install xmlstarlet` on Debian/Ubuntu).

## Project Structure

```
├── bin/                        # Build scripts
│   ├── netex-download-extract.sh  # Downloads NeTEx XSD from GitHub
│   ├── annotation-replacer.sh     # Post-processes generated Java files
│   └── version_updater.sh         # Updates version references
├── bindings.xjb               # JAXB customization bindings
├── src/main/java/
│   ├── module-info.java       # JPMS module descriptor (org.entur.netex2)
│   └── org/entur/netex2/      # Utilities (validation, client, toString style)
│       └── util/              # XML adapters for Java 8 time types
├── src/main/resources/xsd/    # NeTEx XSD files (downloaded at build time)
├── src/test/                  # JUnit 5 tests for marshalling/unmarshalling
└── target/generated-sources/  # JAXB-generated model classes (build artifact)
```

## Key Technical Details

- **JAXB Bindings**: The `bindings.xjb` file customizes code generation, including:
  - Java 8 time type adapters (`LocalDateTime`, `LocalDate`, `LocalTime`, `Duration`)
  - Property name conflict resolution
  - Package naming (`org.entur.netex2.model`)
  - A generated block (between `BEGIN/END generated _Dummy bindings` markers) that hides the `_Dummy` suffix of the abstract substitution-group heads: `getDayType()` instead of `getDayType_Dummy()`, `createDayType_(...)` in `ObjectFactory`. Do not edit the block by hand; rerun `bin/generate-dummy-bindings.py src/main/resources/xsd/<version>/NeTEx_publication.xsd bindings.xjb` after a schema upgrade.

- **Generated Code**: Model classes are generated in `target/generated-sources/` during build. Do not manually edit generated code.

- **NeTEx Version**: Configured in `pom.xml` via `<netexVersion>` property (currently 2.0).

- **Validation**: `NeTExValidator` class provides XML schema validation against NeTEx XSD, for versions 1.04beta through 2.0. Documents declaring 1.16 are validated against the 1.16.1 schema.

## CI/CD

GitHub Actions workflows in `.github/workflows/`:
- `deploy.yml`: Main build, test, Sonar scan, and snapshot publishing
- Release workflows: GitFlow-based release management
- Publishes to Maven Central via JReleaser

## Testing

Tests verify JAXB marshalling/unmarshalling of various NeTEx frame types:
- `MarshalUnmarshalTest` - Round-trip serialization
- `UnmarshalServiceFrameTest`, `UnmarshalSiteFrameTest`, etc. - Frame-specific tests
- `NeTExValidatorTest` - Schema validation tests
