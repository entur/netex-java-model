# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Project Overview

**netex-java-model** is a Java library that generates JAXB model classes from NeTEx (Network Timetable Exchange) XML Schema Definition (XSD) files. The XSD files are downloaded from https://github.com/entur/NeTEx and compiled into Java classes at build time.

- **Organization**: Entur AS (Norwegian public transport data provider)
- **License**: EUPL-1.2
- **Java Version**: 11+ (builds on 17 in CI)
- **Package**: `org.rutebanken.netex.model` (generated), `org.rutebanken.netex` and `org.rutebanken.util` (source)

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
├── src/main/java/org/rutebanken/
│   ├── netex/                 # Utilities (validation, client, toString style)
│   └── util/                  # XML adapters for Java 8 time types
├── src/main/resources/xsd/    # NeTEx XSD files (downloaded at build time)
├── src/test/                  # JUnit 5 tests for marshalling/unmarshalling
└── target/generated-sources/  # JAXB-generated model classes (build artifact)
```

## Key Technical Details

- **JAXB Bindings**: The `bindings.xjb` file customizes code generation, including:
  - Java 8 time type adapters (`LocalDateTime`, `LocalTime`, `Duration`)
  - Property name conflict resolution
  - Package naming (`org.rutebanken.netex.model`)

- **Generated Code**: Model classes are generated in `target/generated-sources/` during build. Do not manually edit generated code.

- **NeTEx Version**: Configured in `pom.xml` via `<netexVersion>` property (currently 1.16).

- **Validation**: `NeTExValidator` class provides XML schema validation against NeTEx XSD.

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
