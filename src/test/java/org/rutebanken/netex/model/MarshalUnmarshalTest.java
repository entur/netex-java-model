package org.rutebanken.netex.model;

import org.junit.Test;

import javax.xml.bind.*;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.time.OffsetTime;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class MarshalUnmarshalTest {

    @Test
    public void publicationDeliveryWithOffsetDateTime() throws JAXBException {
        JAXBContext jaxbContext = JAXBContext.newInstance(PublicationDeliveryStructure.class);
        Marshaller marshaller = jaxbContext.createMarshaller();

        PublicationDeliveryStructure publicationDelivery = new PublicationDeliveryStructure()
                .withDescription(new MultilingualString().withValue("value").withLang("no").withTextIdType(""))
                .withPublicationTimestamp(OffsetDateTime.now())
                .withParticipantRef("participantRef");

        marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        marshaller.marshal(new ObjectFactory().createPublicationDelivery(publicationDelivery), byteArrayOutputStream);

        String xml = byteArrayOutputStream.toString();
        System.out.println(xml);

        Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();

        @SuppressWarnings("unchecked")
        JAXBElement<PublicationDeliveryStructure> jaxbElement =
                (JAXBElement<PublicationDeliveryStructure>) unmarshaller.unmarshal(new ByteArrayInputStream(xml.getBytes()));
        PublicationDeliveryStructure actual = jaxbElement.getValue();

        System.out.println(actual.getPublicationTimestamp());
        assertThat(actual.getPublicationTimestamp()).isEqualTo(publicationDelivery.getPublicationTimestamp());
        assertThat(actual.getDescription()).isNotNull();
        assertThat(actual.getDescription().getValue()).isEqualTo(publicationDelivery.getDescription().getValue());
        assertThat(actual.getParticipantRef()).isEqualTo(publicationDelivery.getParticipantRef());
    }

    @Test
    public void unmarshalPublicationDeliveryAndVerifyDatTimeWithTimeZone() throws JAXBException {

        String xml = "<?xml version=\"1.0\" encoding=\"utf-8\"?>" +
                "<PublicationDelivery version=\"1.0\" xmlns=\"http://www.netex.org.uk/netex\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xsi:schemaLocation=\"http://www.netex.org.uk/netex ../../xsd/NeTEx_publication.xsd\">" +
                " <PublicationTimestamp>2016-05-18T15:00:00.0+01:00</PublicationTimestamp>" +
                " <ParticipantRef>NHR</ParticipantRef>" +
                " <dataObjects>" +
                "  <SiteFrame version=\"01\" id=\"nhr:sf:1\">" +
                "   <stopPlaces>" +
                "    <StopPlace version=\"01\" created=\"2016-04-21T09:00:00.0Z\" id=\"nhr:sp:1\">" +
                "     <Centroid>" +
                "      <Location srsName=\"WGS84\">" +
                "       <Longitude>10.8577903</Longitude>" +
                "       <Latitude>59.910579</Latitude>" +
                "      </Location>" +
                "     </Centroid>" +
                "     <Name lang=\"no-NO\">Krokstien</Name>    " +
                "     <TransportMode>bus</TransportMode>" +
                "     <StopPlaceType>onstreetBus</StopPlaceType>" +
                "     <quays>" +
                "      <Quay version=\"01\" created=\"2016-04-21T09:01:00.0Z\" id=\"nhr:sp:1:q:1\">" +
                "       <Centroid>" +
                "        <Location srsName=\"WGS84\">" +
                "         <Longitude>10.8577903</Longitude>" +
                "         <Latitude>59.910579</Latitude>" +
                "        </Location>" +
                "       </Centroid>" +
                "       <Covered>outdoors</Covered>" +
                "       <Lighting>wellLit</Lighting>" +
                "       <QuayType>busStop</QuayType>" +
                "      </Quay>" +
                "     </quays>" +
                "    </StopPlace>" +
                "   </stopPlaces>" +
                "  </SiteFrame>" +
                " </dataObjects>" +
                "</PublicationDelivery>";

        JAXBContext jaxbContext = JAXBContext.newInstance(PublicationDeliveryStructure.class);
        Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();

        @SuppressWarnings("unchecked")
        JAXBElement<PublicationDeliveryStructure> jaxbElement = (JAXBElement<PublicationDeliveryStructure>) unmarshaller.unmarshal(new ByteArrayInputStream(xml.getBytes()));
        PublicationDeliveryStructure actual = jaxbElement.getValue();

        System.out.println(actual.getPublicationTimestamp());
        assertThat(actual.getPublicationTimestamp().getOffset().toString()).isEqualTo("+01:00");
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
    public void datedCallWithOffsetTime() throws JAXBException {
        JAXBContext jaxbContext = JAXBContext.newInstance(DatedCall.class);
        Marshaller marshaller = jaxbContext.createMarshaller();

        DatedCall datedCall = new DatedCall()
                .withLatestBookingTime(OffsetTime.now());

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
