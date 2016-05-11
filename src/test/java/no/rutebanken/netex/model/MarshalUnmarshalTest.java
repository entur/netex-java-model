package no.rutebanken.netex.model;

import no.rutebanken.netex.model.*;
import org.junit.Test;

import javax.xml.bind.*;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.YearMonth;
import java.time.ZonedDateTime;

import static org.assertj.core.api.Assertions.assertThat;

public class MarshalUnmarshalTest {

    @Test
    public void publicationDeliveryWithZonedDateTime() throws JAXBException {
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

    @Test
    public void datedCallWithLocalDate() throws JAXBException {
        JAXBContext jaxbContext = JAXBContext.newInstance(DatedCall.class);
        Marshaller marshaller = jaxbContext.createMarshaller();

        DatedCall datedCall = new DatedCall()
                .withArrivalDate(LocalDate.now());

        marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();

        marshaller.marshal(datedCall, byteArrayOutputStream);

        String xml = byteArrayOutputStream.toString();
        System.out.println(xml);

        Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();

        DatedCall actual = (DatedCall) unmarshaller.unmarshal(new ByteArrayInputStream(xml.getBytes()));

        assertThat(actual.getArrivalDate()).isEqualTo(datedCall.getArrivalDate());

    }

    @Test
    public void datedCallWithLocalTime() throws JAXBException {
        JAXBContext jaxbContext = JAXBContext.newInstance(DatedCall.class);
        Marshaller marshaller = jaxbContext.createMarshaller();

        DatedCall datedCall = new DatedCall()
                .withLatestBookingTime(LocalTime.NOON);

        marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        marshaller.marshal(datedCall, byteArrayOutputStream);

        String xml = byteArrayOutputStream.toString();
        System.out.println(xml);

        Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();

        DatedCall actual = (DatedCall) unmarshaller.unmarshal(new ByteArrayInputStream(xml.getBytes()));

        assertThat(actual.getLatestBookingTime()).hasSameHourAs(datedCall.getLatestBookingTime());
        assertThat(actual.getLatestBookingTime()).isEqualToIgnoringNanos(datedCall.getLatestBookingTime());
    }
}
