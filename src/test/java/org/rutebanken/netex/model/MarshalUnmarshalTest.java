/*
 * Licensed under the EUPL, Version 1.2 or - as soon they will be approved by
 * the European Commission - subsequent versions of the EUPL (the "Licence");
 * You may not use this work except in compliance with the Licence.
 * You may obtain a copy of the Licence at:
 *
 *   https://joinup.ec.europa.eu/software/page/eupl
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the Licence is distributed on an "AS IS" basis,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the Licence for the specific language governing permissions and
 * limitations under the Licence.
 */

package org.rutebanken.netex.model;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.JAXBElement;
import jakarta.xml.bind.JAXBException;
import jakarta.xml.bind.Marshaller;
import jakarta.xml.bind.Unmarshaller;
import javax.xml.stream.XMLOutputFactory;
import javax.xml.stream.XMLStreamWriter;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.StringWriter;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertFalse;

class MarshalUnmarshalTest {

	private static JAXBContext jaxbContext;
	
	private static final ObjectFactory factory = new ObjectFactory();

	@BeforeAll
	public static void initContext() throws JAXBException {
		jaxbContext = JAXBContext.newInstance(PublicationDeliveryStructure.class);

	}

	@Test
	void publicationDeliveryWithOffsetDateTime() throws JAXBException {
		Marshaller marshaller = jaxbContext.createMarshaller();

		PublicationDeliveryStructure publicationDelivery = new PublicationDeliveryStructure()
				.withDescription(new MultilingualString().withContent("value").withLang("no").withTextIdType("")).withPublicationTimestamp(LocalDateTime.now().withNano(0))
				.withParticipantRef("participantRef");

		marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
		ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
		marshaller.marshal(factory.createPublicationDelivery(publicationDelivery), byteArrayOutputStream);

		String xml = byteArrayOutputStream.toString();
		System.out.println(xml);

		Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();

		@SuppressWarnings("unchecked")
		JAXBElement<PublicationDeliveryStructure> jaxbElement = (JAXBElement<PublicationDeliveryStructure>) unmarshaller
				.unmarshal(new ByteArrayInputStream(xml.getBytes()));
		PublicationDeliveryStructure actual = jaxbElement.getValue();

		System.out.println(actual.getPublicationTimestamp());
		assertThat(actual.getPublicationTimestamp()).isEqualTo(publicationDelivery.getPublicationTimestamp());
		assertThat(actual.getDescription()).isNotNull();
		assertThat(actual.getDescription().getContent()).isEqualTo(publicationDelivery.getDescription().getContent());
		assertThat(actual.getParticipantRef()).isEqualTo(publicationDelivery.getParticipantRef());
	}

	@Test
	void unmarshalPublicationDeliveryAndVerifyDateTime() throws JAXBException {

		String xml = "<?xml version=\"1.0\" encoding=\"utf-8\"?>"
				+ "<PublicationDelivery version=\"1.0\" xmlns=\"http://www.netex.org.uk/netex\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xsi:schemaLocation=\"http://www.netex.org.uk/netex ../../xsd/NeTEx_publication.xsd\">"
				+ " <PublicationTimestamp>2016-05-18T15:00:00.0+01:00</PublicationTimestamp>"
				+ " <ParticipantRef>NHR</ParticipantRef>"
				+ " <dataObjects>"
				+ "  <SiteFrame version=\"01\" id=\"nhr:sf:1\">"
				+ "   <stopPlaces>"
				+ "    <StopPlace version=\"01\" created=\"2016-04-21T09:00:00.0Z\" id=\"nhr:sp:1\">"
				+ "     <Centroid>"
				+ "      <Location srsName=\"WGS84\">"
				+ "       <Longitude>10.8577903</Longitude>"
				+ "       <Latitude>59.910579</Latitude>"
				+ "      </Location>"
				+ "     </Centroid>"
				+ "     <Name lang=\"no-NO\">Krokstien</Name>    "
				+ "     <TransportMode>bus</TransportMode>"
				+ "     <StopPlaceType>onstreetBus</StopPlaceType>"
				+ "     <quays>"
				+ "      <Quay version=\"01\" created=\"2016-04-21T09:01:00.0Z\" id=\"nhr:sp:1:q:1\">"
				+ "       <Centroid>"
				+ "        <Location srsName=\"WGS84\">"
				+ "         <Longitude>10.8577903</Longitude>"
				+ "         <Latitude>59.910579</Latitude>"
				+ "        </Location>"
				+ "       </Centroid>"
				+ "       <Covered>outdoors</Covered>"
				+ "       <Lighting>wellLit</Lighting>"
				+ "       <QuayType>busStop</QuayType>"
				+ "      </Quay>"
				+ "     </quays>"
				+ "    </StopPlace>"
				+ "   </stopPlaces>"
				+ "  </SiteFrame>"
				+ " </dataObjects>"
				+ "</PublicationDelivery>";

		Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();

		@SuppressWarnings("unchecked")
		JAXBElement<PublicationDeliveryStructure> jaxbElement = (JAXBElement<PublicationDeliveryStructure>) unmarshaller
				.unmarshal(new ByteArrayInputStream(xml.getBytes()));
		PublicationDeliveryStructure actual = jaxbElement.getValue();

		System.out.println(actual.getPublicationTimestamp());
		assertThat(actual.getPublicationTimestamp().getHour()).isEqualTo(15);
	}

	@Test
	void timetableWithVehicleModes() throws JAXBException {
		Marshaller marshaller = jaxbContext.createMarshaller();

		TimetableFrame timetableFrame = factory.createTimetableFrame().withVersion("any").withId("TimetableFrame")
				.withName(factory.createMultilingualString().withContent("TimetableFrame")).withVehicleModes(AllPublicTransportModesEnumeration.AIR);

		marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
		ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
		marshaller.marshal(factory.createTimetableFrame(timetableFrame), byteArrayOutputStream);

		String xml = byteArrayOutputStream.toString();
		System.out.println(xml);

		Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();

		@SuppressWarnings("unchecked")
		JAXBElement<TimetableFrame> jaxbElement = (JAXBElement<TimetableFrame>) unmarshaller.unmarshal(new ByteArrayInputStream(xml.getBytes()));
		TimetableFrame actual = jaxbElement.getValue();

		assertThat(actual.getVersion()).isNotNull().isNotEmpty().isEqualTo(timetableFrame.getVersion());
		assertThat(actual.getId()).isNotNull().isNotEmpty().isEqualTo(timetableFrame.getId());
		assertThat(actual.getName().getContent()).isNotNull().isNotEmpty().isEqualTo(timetableFrame.getName().getContent());
		assertThat(actual.getVehicleModes()).isNotNull().isNotEmpty().hasSize(1);
		assertThat(actual.getVehicleModes().get(0)).isNotNull().isEqualTo(AllPublicTransportModesEnumeration.AIR);
	}

	@Test
	void dayTypeWithPropertiesOfDay() throws JAXBException {
		Marshaller marshaller = jaxbContext.createMarshaller();

		List<DayOfWeekEnumeration> daysOfWeek = Arrays.asList(DayOfWeekEnumeration.MONDAY, DayOfWeekEnumeration.TUESDAY, DayOfWeekEnumeration.WEDNESDAY,
				DayOfWeekEnumeration.THURSDAY, DayOfWeekEnumeration.FRIDAY);
		PropertyOfDay propertyOfDay = factory.createPropertyOfDay().withDescription(factory.createMultilingualString().withContent("PropertyOfDay"))
				.withName(factory.createMultilingualString().withContent("PropertyOfDay")).withDaysOfWeek(daysOfWeek);
		PropertiesOfDay_RelStructure propertiesOfDay = factory.createPropertiesOfDay_RelStructure().withPropertyOfDay(propertyOfDay);
		DayType dayType = factory.createDayType().withVersion("any").withId(String.format("%s:dt:weekday", "SK4488"))
				.withName(factory.createMultilingualString().withContent("Ukedager (mandag til fredag)")).withProperties(propertiesOfDay);

		marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
		ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
		marshaller.marshal(factory.createDayType(dayType), byteArrayOutputStream);

		String xml = byteArrayOutputStream.toString();
		System.out.println(xml);

		Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();

		@SuppressWarnings("unchecked")
		JAXBElement<DayType> jaxbElement = (JAXBElement<DayType>) unmarshaller.unmarshal(new ByteArrayInputStream(xml.getBytes()));
		DayType actual = jaxbElement.getValue();

		assertThat(actual.getVersion()).isNotNull().isNotEmpty().isEqualTo(dayType.getVersion());
		assertThat(actual.getId()).isNotNull().isNotEmpty().isEqualTo(dayType.getId());
		assertThat(actual.getName().getContent()).isNotNull().isNotEmpty().isEqualTo(dayType.getName().getContent());
		assertThat(actual.getProperties().getPropertyOfDay().get(0).getDaysOfWeek()).isNotNull().isNotEmpty().hasSize(5)
				.hasOnlyElementsOfType(DayOfWeekEnumeration.class).hasSameElementsAs(daysOfWeek);
	}

	@Test
	void availabilityConditionWithLocalDateTime() throws JAXBException {
		Marshaller marshaller = jaxbContext.createMarshaller();

		AvailabilityCondition condition = new AvailabilityCondition()
				.withFromDate(LocalDateTime.now())
				.withToDate(LocalDateTime.now().plusDays(1))
				.withId("test:condition:1")
				.withVersion("1");

		marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
		ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
		marshaller.marshal(factory.createAvailabilityCondition(condition), byteArrayOutputStream);

		String xml = byteArrayOutputStream.toString();
		System.out.println(xml);

		Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();

		@SuppressWarnings("unchecked")
		JAXBElement<AvailabilityCondition> actual = (JAXBElement<AvailabilityCondition>) unmarshaller.unmarshal(new ByteArrayInputStream(xml.getBytes()));

		assertThat(actual.getValue().getFromDate().getHour()).isEqualTo(condition.getFromDate().getHour());
		assertThat(actual.getValue().getFromDate()).isEqualToIgnoringNanos(condition.getFromDate());
	}


	@Test
	void marshalledNamespacePrefixes() throws JAXBException {
		Marshaller marshaller = jaxbContext.createMarshaller();

		PublicationDeliveryStructure publicationDeliveryStructure = new PublicationDeliveryStructure();


		marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
		ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();

		marshaller.marshal(factory.createPublicationDelivery(publicationDeliveryStructure), byteArrayOutputStream);

		String xml = byteArrayOutputStream.toString();
		System.out.println(xml);

		assertThat(xml)
				.as("Namespace declaration without prefix for netex").contains("xmlns=\"http://www.netex.org.uk/netex\"")
				.as("<PublicationDelivery without namespace prefix").contains("<PublicationDelivery");
	}

	@Test
	void availabilityConditionWithCreatedLocalDateTime() throws JAXBException {
		Marshaller marshaller = jaxbContext.createMarshaller();

		AvailabilityCondition condition = new AvailabilityCondition()
				.withCreated(LocalDateTime.now())
				.withId("test:condition:2")
				.withVersion("1");

		marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
		ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
		marshaller.marshal(factory.createAvailabilityCondition(condition), byteArrayOutputStream);

		String xml = byteArrayOutputStream.toString();
		System.out.println(xml);

		Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();

		@SuppressWarnings("unchecked")
		JAXBElement<AvailabilityCondition> actual = (JAXBElement<AvailabilityCondition>) unmarshaller.unmarshal(new ByteArrayInputStream(xml.getBytes()));

		assertThat(actual.getValue().getCreated().getHour()).isEqualTo(condition.getCreated().getHour());
		assertThat(actual.getValue().getCreated()).isEqualToIgnoringNanos(condition.getCreated());
	}

	@Test
	void unmarshalPublicationDeliveryAndVerifyValidBetween() throws JAXBException, IOException {

		Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();

		@SuppressWarnings("unchecked")
		JAXBElement<PublicationDeliveryStructure> jaxbElement = (JAXBElement<PublicationDeliveryStructure>) unmarshaller
				.unmarshal(Files.newInputStream(Paths.get("src/test/resources/date_time_examples.xml")));
		PublicationDeliveryStructure actual = jaxbElement.getValue();
		CompositeFrame compositeFrame = (CompositeFrame) actual.dataObjects.compositeFrameOrCommonFrame
				.get(0).getValue();
		ValidityConditions_RelStructure validityConditions = compositeFrame.getValidityConditions();
		ValidBetween validBetweenWithTimezone = (ValidBetween) validityConditions.getValidityConditionRefOrValidBetweenOrValidityCondition_Dummy().get(0);
		assertThat(validBetweenWithTimezone.getFromDate()).isNotNull();
		assertThat(validBetweenWithTimezone.getToDate()).isNotNull();
		assertThat(validBetweenWithTimezone.getToDate()).hasToString("2017-01-01T11:00");

		ValidBetween validBetweenWithoutTimezone = (ValidBetween) validityConditions.getValidityConditionRefOrValidBetweenOrValidityCondition_Dummy().get(1);
		assertThat(validBetweenWithoutTimezone.getFromDate()).isNotNull();
		assertThat(validBetweenWithoutTimezone.getToDate()).isNotNull();

		assertThat(validBetweenWithoutTimezone.getToDate()).hasToString("2017-01-01T12:00");

		Timetable_VersionFrameStructure timetableFrame = (Timetable_VersionFrameStructure) compositeFrame.getFrames().getCommonFrame().get(1).getValue();
		ServiceJourney_VersionStructure serviceJourney = (ServiceJourney_VersionStructure) timetableFrame.getVehicleJourneys()
				.getVehicleJourneyOrDatedVehicleJourneyOrNormalDatedVehicleJourney().get(0);
		assertThat(serviceJourney.getDepartureTime()).isNotNull();
		// Specified as local time
		assertThat(serviceJourney.getDepartureTime()).hasToString("07:55");

		LocalTime departureTimeZulu = serviceJourney.getPassingTimes().getTimetabledPassingTime().get(0).getDepartureTime();
		assertThat(departureTimeZulu).isNotNull().hasToString("07:55");

		LocalTime departureTimeOffset = serviceJourney.getPassingTimes().getTimetabledPassingTime().get(1).getArrivalTime();
		assertThat(departureTimeOffset).isNotNull().hasToString("08:40");
	}

	@Test
	void fragmentShouldNotContainNetexNamespace() throws Exception {
		JAXBContext netexJaxBContext = JAXBContext.newInstance("net.opengis.gml._3:org.rutebanken.netex.model:uk.org.siri.siri");
		Marshaller marshaller = netexJaxBContext.createMarshaller();

		marshaller.setProperty(jakarta.xml.bind.Marshaller.JAXB_ENCODING, StandardCharsets.UTF_8.name());
		marshaller.setProperty(jakarta.xml.bind.Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
		marshaller.setProperty(Marshaller.JAXB_FRAGMENT, Boolean.TRUE);

		StringWriter stringWriter = new StringWriter();
		AvailabilityCondition availabilityCondition = new AvailabilityCondition().withFromDate(LocalDateTime.now()).withToDate(LocalDateTime.now()).withId("NSR:AvailabilityCondition:2").withVersion("v1");


		String netexNamespace="http://www.netex.org.uk/netex";

		XMLOutputFactory outputFactory = XMLOutputFactory.newFactory();
		XMLStreamWriter xmlStreamWriter = outputFactory.createXMLStreamWriter(stringWriter);
		xmlStreamWriter.setDefaultNamespace(netexNamespace);

		marshaller.marshal(new ObjectFactory().createAvailabilityCondition(availabilityCondition), xmlStreamWriter);
		String xml = stringWriter.toString();
		System.out.println(xml);
		assertFalse(xml.contains(netexNamespace));
	}

}
