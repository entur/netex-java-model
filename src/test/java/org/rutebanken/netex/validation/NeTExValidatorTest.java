package org.rutebanken.netex.validation;

import org.junit.Test;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;

import javax.xml.transform.stream.StreamSource;
import java.io.IOException;
import java.io.StringReader;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class NeTExValidatorTest {

    @Test
    public void validationFailsForInvalidXml() throws SAXException, IOException {

        NeTExValidator neTExValidator = new NeTExValidator();

        String xml = "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>\n" +
                "<PublicationDelivery xmlns=\"http://www.netex.org.uk/netex\" xmlns:ns2=\"http://www.opengis.net/gml/3.2\" xmlns:ns3=\"http://www.siri.org.uk/siri\"></PublicationDelivery>";

        assertThatThrownBy(() -> {
            neTExValidator.validate(new StreamSource(new StringReader(xml)));
        }).isInstanceOf(SAXParseException.class);

    }
}