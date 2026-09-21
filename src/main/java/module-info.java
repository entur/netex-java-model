module org.entur.netex.java.model {
    requires transitive jakarta.xml.bind;
    requires transitive java.xml;
    requires transitive org.apache.commons.lang3;
    requires io.github.threetenjaxb.core;
    requires org.slf4j;

    exports org.rutebanken.netex;
    exports org.rutebanken.netex.client;
    exports org.rutebanken.netex.validation;
    exports org.rutebanken.netex.model;
    exports org.rutebanken.netex.util;
    exports net.opengis.gml._3;
    exports uk.org.siri.siri;

    opens org.rutebanken.netex.model to jakarta.xml.bind, org.glassfish.jaxb.runtime, org.apache.commons.lang3;
    opens net.opengis.gml._3 to jakarta.xml.bind, org.glassfish.jaxb.runtime, org.apache.commons.lang3;
    opens uk.org.siri.siri to jakarta.xml.bind, org.glassfish.jaxb.runtime, org.apache.commons.lang3;
}
