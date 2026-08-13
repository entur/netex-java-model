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

import jakarta.xml.bind.JAXBElement;
import jakarta.xml.bind.JAXBException;
import jakarta.xml.bind.Unmarshaller;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.time.Duration;
import java.time.LocalTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class UnmarshalTimetableFrameExtendedTest extends AbstractUnmarshalFrameTest {

    @Test
    void unmarshalServiceJourneyInterchange() throws JAXBException {

        String xml = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
                "<PublicationDelivery xmlns=\"http://www.netex.org.uk/netex\" version=\"1.13:NO-NeTEx-networktimetable:1.3\">\n" +
                "  <PublicationTimestamp>2023-08-03T00:20:33.337</PublicationTimestamp>\n" +
                "  <ParticipantRef>RB</ParticipantRef>\n" +
                "  <dataObjects>\n" +
                "    <CompositeFrame version=\"1\" id=\"TST:CompositeFrame:1\">\n" +
                "      <frames>\n" +
                "        <TimetableFrame version=\"1\" id=\"TST:TimetableFrame:1\">\n" +
                "          <journeyInterchanges>\n" +
                "            <ServiceJourneyInterchange version=\"1\" id=\"TST:ServiceJourneyInterchange:1\">\n" +
                "              <StaySeated>true</StaySeated>\n" +
                "              <Guaranteed>true</Guaranteed>\n" +
                "              <MaximumWaitTime>PT5M</MaximumWaitTime>\n" +
                "              <FromPointRef ref=\"TST:ScheduledStopPoint:1\"/>\n" +
                "              <ToPointRef ref=\"TST:ScheduledStopPoint:2\"/>\n" +
                "              <FromJourneyRef ref=\"TST:ServiceJourney:1\"/>\n" +
                "              <ToJourneyRef ref=\"TST:ServiceJourney:2\"/>\n" +
                "            </ServiceJourneyInterchange>\n" +
                "          </journeyInterchanges>\n" +
                "        </TimetableFrame>\n" +
                "      </frames>\n" +
                "    </CompositeFrame>\n" +
                "  </dataObjects>\n" +
                "</PublicationDelivery>";

        Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();

        @SuppressWarnings("unchecked")
        JAXBElement<PublicationDeliveryStructure> jaxbElement = (JAXBElement<PublicationDeliveryStructure>) unmarshaller
                .unmarshal(new ByteArrayInputStream(xml.getBytes()));

        PublicationDeliveryStructure publicationDeliveryStructure = jaxbElement.getValue();
        CompositeFrame compositeFrame = (CompositeFrame) publicationDeliveryStructure.getDataObjects().getCompositeFrameOrCommonFrame().get(0).getValue();

        TimetableFrame timetableFrame = (TimetableFrame) compositeFrame.getFrames().getCommonFrame().get(0).getValue();

        ServiceJourneyInterchange interchange = (ServiceJourneyInterchange) timetableFrame.getJourneyInterchanges()
                .getServiceJourneyPatternInterchangeOrServiceJourneyInterchange().get(0);
        assertEquals("TST:ScheduledStopPoint:1", interchange.getFromPointRef().getRef());
        assertEquals("TST:ScheduledStopPoint:2", interchange.getToPointRef().getRef());
        assertEquals("TST:ServiceJourney:1", interchange.getFromJourneyRef().getRef());
        assertEquals("TST:ServiceJourney:2", interchange.getToJourneyRef().getRef());
        assertTrue(interchange.isStaySeated());
        assertTrue(interchange.isGuaranteed());
        assertEquals(Duration.ofMinutes(5), interchange.getMaximumWaitTime());
    }

    @Test
    void unmarshalServiceJourneyWithFlexibleServiceProperties() throws JAXBException {

        String xml = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
                "<PublicationDelivery xmlns=\"http://www.netex.org.uk/netex\" version=\"1.13:NO-NeTEx-networktimetable:1.3\">\n" +
                "  <PublicationTimestamp>2023-08-03T00:20:33.337</PublicationTimestamp>\n" +
                "  <ParticipantRef>RB</ParticipantRef>\n" +
                "  <dataObjects>\n" +
                "    <CompositeFrame version=\"1\" id=\"TST:CompositeFrame:1\">\n" +
                "      <frames>\n" +
                "        <TimetableFrame version=\"1\" id=\"TST:TimetableFrame:1\">\n" +
                "          <vehicleJourneys>\n" +
                "            <ServiceJourney version=\"1\" id=\"TST:ServiceJourney:Flex1\">\n" +
                "              <Name>Flex Service</Name>\n" +
                "              <TransportMode>bus</TransportMode>\n" +
                "              <FlexibleServiceProperties version=\"1\" id=\"TST:FlexibleServiceProperties:1\">\n" +
                "                <BookingAccess>public</BookingAccess>\n" +
                "                <BookWhen>dayOfTravelOnly</BookWhen>\n" +
                "                <LatestBookingTime>12:00:00</LatestBookingTime>\n" +
                "                <BookingContact>\n" +
                "                  <Phone>+47 12345678</Phone>\n" +
                "                  <Url>https://booking.example.com</Url>\n" +
                "                </BookingContact>\n" +
                "              </FlexibleServiceProperties>\n" +
                "            </ServiceJourney>\n" +
                "          </vehicleJourneys>\n" +
                "        </TimetableFrame>\n" +
                "      </frames>\n" +
                "    </CompositeFrame>\n" +
                "  </dataObjects>\n" +
                "</PublicationDelivery>";

        Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();

        @SuppressWarnings("unchecked")
        JAXBElement<PublicationDeliveryStructure> jaxbElement = (JAXBElement<PublicationDeliveryStructure>) unmarshaller
                .unmarshal(new ByteArrayInputStream(xml.getBytes()));

        PublicationDeliveryStructure publicationDeliveryStructure = jaxbElement.getValue();
        CompositeFrame compositeFrame = (CompositeFrame) publicationDeliveryStructure.getDataObjects().getCompositeFrameOrCommonFrame().get(0).getValue();

        TimetableFrame timetableFrame = (TimetableFrame) compositeFrame.getFrames().getCommonFrame().get(0).getValue();

        ServiceJourney serviceJourney = (ServiceJourney) timetableFrame.getVehicleJourneys()
                .getVehicleJourneyOrDatedVehicleJourneyOrNormalDatedVehicleJourney().get(0);
        assertEquals("Flex Service", getStringValue(serviceJourney.getName()));

        FlexibleServiceProperties flexProps = serviceJourney.getFlexibleServiceProperties();
        assertEquals(BookingAccessEnumeration.PUBLIC, flexProps.getBookingAccess());
        assertEquals(PurchaseWhenEnumeration.DAY_OF_TRAVEL_ONLY, flexProps.getBookWhen());
        assertEquals(LocalTime.of(12, 0), flexProps.getLatestBookingTime());
        assertEquals("+47 12345678", flexProps.getBookingContact().getPhone());
        assertEquals("https://booking.example.com", flexProps.getBookingContact().getUrl());
    }
}
