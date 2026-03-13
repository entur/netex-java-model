# netex-java-model

[![Build and push](https://github.com/entur/netex-java-model/actions/workflows/deploy.yml/badge.svg)](https://github.com/entur/netex-java-model/actions/workflows/deploy.yml)
[![Maven Central](https://img.shields.io/maven-central/v/org.entur/netex-java-model.svg?label=Maven%20Central)](https://central.sonatype.com/artifact/org.entur/netex-java-model)
[![Maven Snapshots](https://img.shields.io/maven-metadata/v?metadataUrl=https%3A%2F%2Fcentral.sonatype.com%2Frepository%2Fmaven-snapshots%2Forg%2Fentur%2Fnetex-java-model%2Fmaven-metadata.xml&label=Snapshot)](https://central.sonatype.com/repository/maven-snapshots)
[![License: EUPL-1.2](https://img.shields.io/badge/License-EUPL--1.2-blue.svg)](https://joinup.ec.europa.eu/software/page/eupl)
[![Java 11+](https://img.shields.io/badge/Java-11%2B-orange)](https://adoptium.net/)

Java model classes generated from [NeTEx](https://github.com/entur/NeTEx) (Network Timetable Exchange) XML Schema Definition (XSD) files using JAXB. Maintained by [Entur AS](https://www.entur.org).

## Overview

This library downloads the NeTEx XSD files from the [Entur NeTEx fork](https://github.com/entur/NeTEx) and compiles them into JAXB-annotated Java model classes at build time. The generated classes support full marshalling and unmarshalling of NeTEx XML documents, including schema validation against multiple NeTEx versions.

- **NeTEx version**: 1.16 (model generation), with legacy validation support back to 1.07
- **JAXB**: Jakarta XML Bind 4.x (Jakarta EE 9+)
- **Generated package**: `org.rutebanken.netex.model`
- **GML types package**: `net.opengis.gml._3`

## Usage

### Release (Maven Central)

```xml
<dependency>
    <groupId>org.entur</groupId>
    <artifactId>netex-java-model</artifactId>
    <version>LATEST</version>
</dependency>
```

See [Maven Central](https://central.sonatype.com/artifact/org.entur/netex-java-model) for the latest released version.

### Snapshot

Snapshots are published to the Sonatype snapshot repository. Add the repository to your `pom.xml`:

```xml
<repositories>
    <repository>
        <id>sonatype-snapshots</id>
        <url>https://central.sonatype.com/repository/maven-snapshots</url>
        <snapshots>
            <enabled>true</enabled>
        </snapshots>
    </repository>
</repositories>
```

Then add the dependency with the snapshot version:

```xml
<dependency>
    <groupId>org.entur</groupId>
    <artifactId>netex-java-model</artifactId>
    <version>LATEST-SNAPSHOT</version>
</dependency>
```

## Building from Source

### Prerequisites

- Java 11+
- Maven 3.x
- [`xmlstarlet`](https://xmlstar.sourceforge.net/) (`apt install xmlstarlet` on Debian/Ubuntu, `brew install xmlstarlet` on macOS)

### Build

```bash
# Full build: downloads XSD files, generates JAXB classes, compiles, runs tests
mvn clean install

# Run tests only
mvn test

# Package without running tests
mvn package -DskipTests
```

The XSD files are downloaded from GitHub during the `generate-sources` phase and placed under `src/main/resources/xsd/`. The JAXB model is generated into `target/generated-sources/xjc/`.

## Project Structure

```
├── bin/
│   ├── netex-download-extract.sh   # Downloads NeTEx XSD from GitHub
│   ├── annotation-replacer.sh      # Post-processes generated Java files
│   └── version_updater.sh          # Updates version references in bindings
├── bindings.xjb                    # JAXB customization bindings
├── src/main/java/org/rutebanken/
│   ├── netex/                      # NeTEx utilities (validation, toString style)
│   └── util/                       # XML adapters for Java time types
├── src/main/resources/xsd/         # Downloaded NeTEx XSD files (build artifact)
└── src/test/                       # JUnit 5 marshalling/unmarshalling tests
```

## Key Technical Details

**JAXB customizations** (`bindings.xjb`):
- Java 8 time type adapters (`LocalDateTime`, `LocalTime`, `Duration`) via `threeten-jaxb-core`
- Fluent builder API (`withX()` methods) via `jaxb-fluent-api`
- Property name conflict resolution
- Package mapping to `org.rutebanken.netex.model`

**Schema validation**: `NeTExValidator` supports validation against NeTEx versions 1.07 through 1.16.

**Note on GML types**: This library generates classes under `net.opengis.gml._3` as part of the NeTEx model.

## License

Licensed under the [European Union Public Licence (EUPL) version 1.2](https://joinup.ec.europa.eu/software/page/eupl).

## Related Projects

- [netex-protobuf](https://github.com/entur/netex-protobuf) — NeTEx XSD files converted to protobuf descriptors
- [NeTEx (Entur fork)](https://github.com/entur/NeTEx) — NeTEx XSD source files used by this library