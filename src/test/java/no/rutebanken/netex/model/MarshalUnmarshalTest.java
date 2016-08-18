package no.rutebanken.netex.model;

import org.junit.Test;

import javax.xml.bind.*;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.time.*;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class MarshalUnmarshalTest {

    @Test
    public void publicationDeliveryWithZonedDateTime() throws JAXBException {
        JAXBContext jaxbContext = JAXBContext.newInstance(PublicationDeliveryStructure.class);
        Marshaller marshaller = jaxbContext.createMarshaller();

        PublicationDeliveryStructure publicationDelivery = new PublicationDeliveryStructure()
                .withDescription(new MultilingualString().withValue("value").withLang("no").withTextIdType(""))
                //.withPublicationTimestamp(ZonedDateTime.now())
                .withPublicationTimestamp(OffsetDateTime.now())
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
    public void timetableWithVehicleModes() throws JAXBException {
        ObjectFactory objectFactory = new ObjectFactory();
        JAXBContext jaxbContext = JAXBContext.newInstance(PublicationDeliveryStructure.class);
        Marshaller marshaller = jaxbContext.createMarshaller();

        TimetableFrame timetableFrame = objectFactory.createTimetableFrame()
                .withVersion("any")
                .withId("TimetableFrame")
                .withName(objectFactory.createMultilingualString().withValue("TimetableFrame"))
                .withVehicleModes(VehicleModeEnumeration.AIR);

        marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        marshaller.marshal(objectFactory.createTimetableFrame(timetableFrame), byteArrayOutputStream);

        String xml = byteArrayOutputStream.toString();
        System.out.println(xml);

        Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();

        @SuppressWarnings("unchecked")
        JAXBElement<TimetableFrame> jaxbElement = (JAXBElement<TimetableFrame>)
                unmarshaller.unmarshal(new ByteArrayInputStream(xml.getBytes()));
        TimetableFrame actual = jaxbElement.getValue();

        assertThat(actual.getVersion())
                .isNotNull()
                .isNotEmpty()
                .isEqualTo(timetableFrame.getVersion());
        assertThat(actual.getId())
                .isNotNull()
                .isNotEmpty()
                .isEqualTo(timetableFrame.getId());
        assertThat(actual.getName().getValue())
                .isNotNull()
                .isNotEmpty()
                .isEqualTo(timetableFrame.getName().getValue());
        assertThat(actual.getVehicleModes())
                .isNotNull()
                .isNotEmpty()
                .hasSize(1);
        assertThat(actual.getVehicleModes().get(0))
                .isNotNull()
                .isEqualTo(VehicleModeEnumeration.AIR);
    }

    @Test
    public void dayTypeWithPropertiesOfDay() throws JAXBException {
        ObjectFactory objectFactory = new ObjectFactory();
        JAXBContext jaxbContext = JAXBContext.newInstance(PublicationDeliveryStructure.class);
        Marshaller marshaller = jaxbContext.createMarshaller();

        List<DayOfWeekEnumeration> daysOfWeek = Arrays.asList(
                DayOfWeekEnumeration.MONDAY,
                DayOfWeekEnumeration.TUESDAY,
                DayOfWeekEnumeration.WEDNESDAY,
                DayOfWeekEnumeration.THURSDAY,
                DayOfWeekEnumeration.FRIDAY
        );
        PropertyOfDay propertyOfDay = objectFactory.createPropertyOfDay()
                .withDescription(objectFactory.createMultilingualString().withValue("PropertyOfDay"))
                .withName(objectFactory.createMultilingualString().withValue("PropertyOfDay"))
                .withDaysOfWeek(daysOfWeek);
        PropertiesOfDay_RelStructure propertiesOfDay = objectFactory.createPropertiesOfDay_RelStructure()
                .withPropertyOfDay(propertyOfDay);
        DayType dayType = objectFactory.createDayType()
                .withVersion("any")
                .withId(String.format("%s:dt:weekday", "SK4488"))
                .withName(objectFactory.createMultilingualString().withValue("Ukedager (mandag til fredag)"))
                .withProperties(propertiesOfDay);

        marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        marshaller.marshal(objectFactory.createDayType(dayType), byteArrayOutputStream);

        String xml = byteArrayOutputStream.toString();
        System.out.println(xml);

        Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();

        @SuppressWarnings("unchecked")
        JAXBElement<DayType> jaxbElement = (JAXBElement<DayType>)
                unmarshaller.unmarshal(new ByteArrayInputStream(xml.getBytes()));
        DayType actual = jaxbElement.getValue();

        assertThat(actual.getVersion())
                .isNotNull()
                .isNotEmpty()
                .isEqualTo(dayType.getVersion());
        assertThat(actual.getId())
                .isNotNull()
                .isNotEmpty()
                .isEqualTo(dayType.getId());
        assertThat(actual.getName().getValue())
                .isNotNull()
                .isNotEmpty()
                .isEqualTo(dayType.getName().getValue());
        assertThat(actual.getProperties().getPropertyOfDay().get(0).getDaysOfWeek())
                .isNotNull()
                .isNotEmpty()
                .hasSize(5)
                .hasOnlyElementsOfType(DayOfWeekEnumeration.class)
                .hasSameElementsAs(daysOfWeek);
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
