import no.rutebanken.netex.model.ArrivalStructure;
import no.rutebanken.netex.model.MultilingualString;
import no.rutebanken.netex.model.ObjectFactory;
import no.rutebanken.netex.model.PublicationDeliveryStructure;
import org.junit.Ignore;
import org.junit.Test;
import org.xml.sax.InputSource;


import javax.xml.bind.*;
import javax.xml.namespace.QName;
import java.io.*;
import java.time.LocalTime;
import java.time.ZonedDateTime;

import static org.assertj.core.api.Assertions.assertThat;

public class MarshalUnmarshalTest {

    @Test
    public void marshalUnmarshalPublicationDelivery() throws JAXBException {
        JAXBContext jaxbContext = JAXBContext.newInstance(PublicationDeliveryStructure.class);
        Marshaller marshaller = jaxbContext.createMarshaller();

        PublicationDeliveryStructure publicationDelivery = new PublicationDeliveryStructure()
                .withDescription(new MultilingualString().withValue("value").withLang("no").withTextIdType(""))
                .withPublicationTimestamp(ZonedDateTime.now())
                .withParticipantRef("participantRef");

        marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        marshaller.marshal(new ObjectFactory().createPublicationDelivery(publicationDelivery), byteArrayOutputStream);

        String xml = byteArrayOutputStream.toString();
        System.out.println(xml);

        Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();

        JAXBElement<PublicationDeliveryStructure> jaxbElement = (JAXBElement<PublicationDeliveryStructure>) unmarshaller.unmarshal(new ByteArrayInputStream(xml.getBytes()));
        PublicationDeliveryStructure actual = jaxbElement.getValue();

        System.out.println(actual.getPublicationTimestamp());
        assertThat(actual.getPublicationTimestamp()).isEqualTo(publicationDelivery.getPublicationTimestamp());
        assertThat(actual.getDescription()).isNotNull();
        assertThat(actual.getDescription().getValue()).isEqualTo(publicationDelivery.getDescription().getValue());
        assertThat(actual.getParticipantRef()).isEqualTo(publicationDelivery.getParticipantRef());
    }

    @Ignore
    @Test
    public void localTime() throws JAXBException {
        JAXBContext jaxbContext = JAXBContext.newInstance(PublicationDeliveryStructure.class);
        Marshaller marshaller = jaxbContext.createMarshaller();

        ArrivalStructure arrival = new ArrivalStructure()
                .withTime(LocalTime.NOON);

        marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        marshaller.marshal(new JAXBElement<>(new QName("http://www.netex.org.uk/netex", "Arrival"), ArrivalStructure.class, null, arrival), byteArrayOutputStream);

        String xml = byteArrayOutputStream.toString();
        System.out.println(xml);

        Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();

        JAXBElement<ArrivalStructure> jaxbElement = (JAXBElement<ArrivalStructure>) unmarshaller.unmarshal(new ByteArrayInputStream(xml.getBytes()));
        ArrivalStructure actual = jaxbElement.getValue();

        assertThat(actual.getTime()).hasSameHourAs(arrival.getTime());
    }

}
