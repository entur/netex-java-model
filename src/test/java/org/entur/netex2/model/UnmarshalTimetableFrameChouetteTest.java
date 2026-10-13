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

package org.entur.netex2.model;

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

        String xml = """
            <?xml version="1.0" encoding="UTF-8"?>
            <PublicationDelivery xmlns="http://www.netex.org.uk/netex" version="1.13:NO-NeTEx-networktimetable:1.3">
              <PublicationTimestamp>2023-08-03T00:20:33.337</PublicationTimestamp>
              <ParticipantRef>RB</ParticipantRef>
              <dataObjects>
                <CompositeFrame version="1" id="TST:CompositeFrame:1">
                  <frames>
                    <TimetableFrame version="1" id="TST:TimetableFrame:1">
                      <vehicleJourneys>
                        <DeadRun version="1" id="TST:DeadRun:1">
                          <Name>Dead Run to Depot</Name>
                          <TransportMode>bus</TransportMode>
                          <dayTypes>
                            <DayTypeRef ref="TST:DayType:Weekday"/>
                          </dayTypes>
                          <JourneyPatternRef ref="TST:JourneyPattern:DR1"/>
                          <passingTimes>
                            <TimetabledPassingTime version="1" id="TST:TimetabledPassingTime:DR1-1">
                              <DepartureTime>23:30:00</DepartureTime>
                            </TimetabledPassingTime>
                            <TimetabledPassingTime version="1" id="TST:TimetabledPassingTime:DR1-2">
                              <ArrivalTime>23:55:00</ArrivalTime>
                            </TimetabledPassingTime>
                          </passingTimes>
                        </DeadRun>
                      </vehicleJourneys>
                    </TimetableFrame>
                  </frames>
                </CompositeFrame>
              </dataObjects>
            </PublicationDelivery>""";

        Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();

        @SuppressWarnings("unchecked")
        JAXBElement<PublicationDeliveryStructure> jaxbElement = (JAXBElement<PublicationDeliveryStructure>) unmarshaller
                .unmarshal(new ByteArrayInputStream(xml.getBytes()));

        PublicationDeliveryStructure publicationDeliveryStructure = jaxbElement.getValue();
        CompositeFrame compositeFrame = (CompositeFrame) publicationDeliveryStructure.getDataObjects().getCompositeFrameOrCommonFrame().getFirst().getValue();
        TimetableFrame timetableFrame = (TimetableFrame) compositeFrame.getFrames().getCommonFrame().getFirst().getValue();

        DeadRun deadRun = (DeadRun) timetableFrame.getVehicleJourneys()
                .getVehicleJourneyOrDatedVehicleJourneyOrNormalDatedVehicleJourney().getFirst();
        assertEquals("Dead Run to Depot", getStringValue(deadRun.getName()));
        assertEquals(AllPublicTransportModesEnumeration.BUS, deadRun.getTransportMode());

        assertNotNull(deadRun.getDayTypes());
        assertEquals("TST:DayType:Weekday", deadRun.getDayTypes().getDayTypeRef().getFirst().getValue().getRef());

        assertEquals("TST:JourneyPattern:DR1", deadRun.getJourneyPatternRef().getValue().getRef());

        assertNotNull(deadRun.getPassingTimes());
        assertEquals(2, deadRun.getPassingTimes().getTimetabledPassingTime().size());
        assertEquals(LocalTime.of(23, 30), deadRun.getPassingTimes().getTimetabledPassingTime().getFirst().getDepartureTime());
        assertEquals(LocalTime.of(23, 55), deadRun.getPassingTimes().getTimetabledPassingTime().get(1).getArrivalTime());
    }

    @Test
    void unmarshalFlexibleServicePropertiesExtended() throws JAXBException {

        String xml = """
            <?xml version="1.0" encoding="UTF-8"?>
            <PublicationDelivery xmlns="http://www.netex.org.uk/netex" version="1.13:NO-NeTEx-networktimetable:1.3">
              <PublicationTimestamp>2023-08-03T00:20:33.337</PublicationTimestamp>
              <ParticipantRef>RB</ParticipantRef>
              <dataObjects>
                <CompositeFrame version="1" id="TST:CompositeFrame:1">
                  <frames>
                    <TimetableFrame version="1" id="TST:TimetableFrame:1">
                      <vehicleJourneys>
                        <ServiceJourney version="1" id="TST:ServiceJourney:Flex1">
                          <Name>Flex Extended</Name>
                          <TransportMode>bus</TransportMode>
                          <FlexibleServiceProperties version="1" id="TST:FlexibleServiceProperties:1">
                            <FlexibleServiceType>dynamicPassingTimes</FlexibleServiceType>
                            <CancellationPossible>true</CancellationPossible>
                            <ChangeOfTimePossible>false</ChangeOfTimePossible>
                            <BookingAccess>public</BookingAccess>
                            <BookWhen>untilPreviousDay</BookWhen>
                            <BuyWhen>beforeBoarding onBoarding</BuyWhen>
                            <BookingNote>Advanced booking required</BookingNote>
                            <BookingContact>
                              <Phone>+47 99998888</Phone>
                            </BookingContact>
                          </FlexibleServiceProperties>
                        </ServiceJourney>
                      </vehicleJourneys>
                    </TimetableFrame>
                  </frames>
                </CompositeFrame>
              </dataObjects>
            </PublicationDelivery>""";

        Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();

        @SuppressWarnings("unchecked")
        JAXBElement<PublicationDeliveryStructure> jaxbElement = (JAXBElement<PublicationDeliveryStructure>) unmarshaller
                .unmarshal(new ByteArrayInputStream(xml.getBytes()));

        PublicationDeliveryStructure publicationDeliveryStructure = jaxbElement.getValue();
        CompositeFrame compositeFrame = (CompositeFrame) publicationDeliveryStructure.getDataObjects().getCompositeFrameOrCommonFrame().getFirst().getValue();
        TimetableFrame timetableFrame = (TimetableFrame) compositeFrame.getFrames().getCommonFrame().getFirst().getValue();

        ServiceJourney sj = (ServiceJourney) timetableFrame.getVehicleJourneys()
                .getVehicleJourneyOrDatedVehicleJourneyOrNormalDatedVehicleJourney().getFirst();

        FlexibleServiceProperties fsp = sj.getFlexibleServiceProperties();
        assertNotNull(fsp);
        assertEquals(FlexibleServiceEnumeration.DYNAMIC_PASSING_TIMES, fsp.getFlexibleServiceType());
        assertTrue(fsp.isCancellationPossible());
        assertFalse(fsp.isChangeOfTimePossible());
        assertEquals(BookingAccessEnumeration.PUBLIC, fsp.getBookingAccess());
        assertEquals(PurchaseWhenEnumeration.UNTIL_PREVIOUS_DAY, fsp.getBookWhen());

        assertEquals(2, fsp.getBuyWhen().size());
        assertEquals(PurchaseMomentEnumeration.BEFORE_BOARDING, fsp.getBuyWhen().getFirst());
        assertEquals(PurchaseMomentEnumeration.ON_BOARDING, fsp.getBuyWhen().get(1));

        assertEquals("Advanced booking required", getStringValue(fsp.getBookingNote()));
        assertEquals("+47 99998888", fsp.getBookingContact().getPhone());
    }
}
