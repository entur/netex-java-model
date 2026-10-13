module org.entur.netex2 {
    requires transitive jakarta.xml.bind;
    requires transitive java.xml;
    requires transitive org.apache.commons.lang3;
    requires io.github.threetenjaxb.core;
    requires org.slf4j;

    exports org.entur.netex2.client;
    exports org.entur.netex2.validation;
    exports org.entur.netex2.model;
    exports org.entur.netex2.util to jakarta.xml.bind, org.glassfish.jaxb.core, org.glassfish.jaxb.runtime;
    exports net.opengis.gml._3;
    exports uk.org.siri.siri;

    opens org.entur.netex2.model to jakarta.xml.bind, org.glassfish.jaxb.runtime, org.apache.commons.lang3;
    opens net.opengis.gml._3 to jakarta.xml.bind, org.glassfish.jaxb.runtime, org.apache.commons.lang3;
    opens uk.org.siri.siri to jakarta.xml.bind, org.glassfish.jaxb.runtime, org.apache.commons.lang3;
}
