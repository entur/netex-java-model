import org.junit.Test;
import org.xml.sax.InputSource;
import uk.org.netex.netex.MultilingualString;
import uk.org.netex.netex.ObjectFactory;
import uk.org.netex.netex.PublicationDeliveryStructure;
import uk.org.siri.siri.ParticipantRefStructure;


import javax.xml.bind.*;
import java.io.*;
import java.time.ZonedDateTime;

import static org.assertj.core.api.Assertions.assertThat;

public class MarshalUnmarshalTest {

    @Test
    public void marshalUnmarshalPublicationDelivery() throws JAXBException {
        JAXBContext jaxbContext = JAXBContext.newInstance(PublicationDeliveryStructure.class);
        Marshaller marshaller = jaxbContext.createMarshaller();

        PublicationDeliveryStructure publicationDeliveryStructure = new PublicationDeliveryStructure();

        MultilingualString description = new MultilingualString();

        description.setValue("value");
        description.setLang("no");
        description.setTextIdType("");

        publicationDeliveryStructure.setDescription(description);

        ZonedDateTime zonedDateTime = ZonedDateTime.now();
        publicationDeliveryStructure.setPublicationTimestamp(zonedDateTime);

        publicationDeliveryStructure.setParticipantRef("participantRef");

        marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        marshaller.marshal(new ObjectFactory().createPublicationDelivery(publicationDeliveryStructure), byteArrayOutputStream);

        String xml = byteArrayOutputStream.toString();
        System.out.println(xml);

        Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();

        JAXBElement<PublicationDeliveryStructure> jaxbElement = (JAXBElement<PublicationDeliveryStructure>) unmarshaller.unmarshal(new ByteArrayInputStream(xml.getBytes()));
        PublicationDeliveryStructure actual = jaxbElement.getValue();

        System.out.println(actual.getPublicationTimestamp());
        assertThat(actual.getPublicationTimestamp()).isEqualTo(publicationDeliveryStructure.getPublicationTimestamp());
        assertThat(actual.getDescription()).isNotNull();
        assertThat(actual.getDescription().getValue()).isEqualTo(description.getValue());
        assertThat(actual.getParticipantRef()).isEqualTo(publicationDeliveryStructure.getParticipantRef());
    }
}
