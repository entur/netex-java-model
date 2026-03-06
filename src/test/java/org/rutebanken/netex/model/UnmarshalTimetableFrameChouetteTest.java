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
import java.time.LocalTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

class UnmarshalTimetableFrameChouetteTest extends AbstractUnmarshalFrameTest {

    @Test
    void unmarshalDeadRun() throws JAXBException {

        String xml = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
                "<PublicationDelivery xmlns=\"http://www.netex.org.uk/netex\" version=\"1.13:NO-NeTEx-networktimetable:1.3\">\n" +
                "  <PublicationTimestamp>2023-08-03T00:20:33.337</PublicationTimestamp>\n" +
                "  <ParticipantRef>RB</ParticipantRef>\n" +
                "  <dataObjects>\n" +
                "    <CompositeFrame version=\"1\" id=\"TST:CompositeFrame:1\">\n" +
                "      <frames>\n" +
                "        <TimetableFrame version=\"1\" id=\"TST:TimetableFrame:1\">\n" +
                "          <vehicleJourneys>\n" +
                "            <DeadRun version=\"1\" id=\"TST:DeadRun:1\">\n" +
                "              <Name>Dead Run to Depot</Name>\n" +
                "              <TransportMode>bus</TransportMode>\n" +
                "              <dayTypes>\n" +
                "                <DayTypeRef ref=\"TST:DayType:Weekday\"/>\n" +
                "              </dayTypes>\n" +
                "              <JourneyPatternRef ref=\"TST:JourneyPattern:DR1\"/>\n" +
                "              <passingTimes>\n" +
                "                <TimetabledPassingTime version=\"1\" id=\"TST:TimetabledPassingTime:DR1-1\">\n" +
                "                  <DepartureTime>23:30:00</DepartureTime>\n" +
                "                </TimetabledPassingTime>\n" +
                "                <TimetabledPassingTime version=\"1\" id=\"TST:TimetabledPassingTime:DR1-2\">\n" +
                "                  <ArrivalTime>23:55:00</ArrivalTime>\n" +
                "                </TimetabledPassingTime>\n" +
                "              </passingTimes>\n" +
                "            </DeadRun>\n" +
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

        DeadRun deadRun = (DeadRun) timetableFrame.getVehicleJourneys()
                .getVehicleJourneyOrDatedVehicleJourneyOrNormalDatedVehicleJourney().get(0);
        assertEquals("Dead Run to Depot", deadRun.getName().getValue());
        assertEquals(AllVehicleModesOfTransportEnumeration.BUS, deadRun.getTransportMode());

        assertNotNull(deadRun.getDayTypes());
        assertEquals("TST:DayType:Weekday", deadRun.getDayTypes().getDayTypeRef().get(0).getValue().getRef());

        assertEquals("TST:JourneyPattern:DR1", deadRun.getJourneyPatternRef().getValue().getRef());

        assertNotNull(deadRun.getPassingTimes());
        assertEquals(2, deadRun.getPassingTimes().getTimetabledPassingTime().size());
        assertEquals(LocalTime.of(23, 30), deadRun.getPassingTimes().getTimetabledPassingTime().get(0).getDepartureTime());
        assertEquals(LocalTime.of(23, 55), deadRun.getPassingTimes().getTimetabledPassingTime().get(1).getArrivalTime());
    }

    @Test
    void unmarshalFlexibleServicePropertiesExtended() throws JAXBException {

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
                "              <Name>Flex Extended</Name>\n" +
                "              <TransportMode>bus</TransportMode>\n" +
                "              <FlexibleServiceProperties version=\"1\" id=\"TST:FlexibleServiceProperties:1\">\n" +
                "                <FlexibleServiceType>dynamicPassingTimes</FlexibleServiceType>\n" +
                "                <CancellationPossible>true</CancellationPossible>\n" +
                "                <ChangeOfTimePossible>false</ChangeOfTimePossible>\n" +
                "                <BookingAccess>public</BookingAccess>\n" +
                "                <BookWhen>untilPreviousDay</BookWhen>\n" +
                "                <BuyWhen>beforeBoarding onBoarding</BuyWhen>\n" +
                "                <BookingNote>Advanced booking required</BookingNote>\n" +
                "                <BookingContact>\n" +
                "                  <Phone>+47 99998888</Phone>\n" +
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

        ServiceJourney sj = (ServiceJourney) timetableFrame.getVehicleJourneys()
                .getVehicleJourneyOrDatedVehicleJourneyOrNormalDatedVehicleJourney().get(0);

        FlexibleServiceProperties fsp = sj.getFlexibleServiceProperties();
        assertNotNull(fsp);
        assertEquals(FlexibleServiceEnumeration.DYNAMIC_PASSING_TIMES, fsp.getFlexibleServiceType());
        assertTrue(fsp.isCancellationPossible());
        assertFalse(fsp.isChangeOfTimePossible());
        assertEquals(BookingAccessEnumeration.PUBLIC, fsp.getBookingAccess());
        assertEquals(PurchaseWhenEnumeration.UNTIL_PREVIOUS_DAY, fsp.getBookWhen());

        assertEquals(2, fsp.getBuyWhen().size());
        assertEquals(PurchaseMomentEnumeration.BEFORE_BOARDING, fsp.getBuyWhen().get(0));
        assertEquals(PurchaseMomentEnumeration.ON_BOARDING, fsp.getBuyWhen().get(1));

        assertEquals("Advanced booking required", fsp.getBookingNote().getValue());
        assertEquals("+47 99998888", fsp.getBookingContact().getPhone());
    }
}
