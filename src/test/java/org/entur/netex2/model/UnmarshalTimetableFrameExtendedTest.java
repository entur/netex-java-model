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
import java.time.Duration;
import java.time.LocalTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class UnmarshalTimetableFrameExtendedTest extends AbstractUnmarshalFrameTest {

    @Test
    void unmarshalServiceJourneyInterchange() throws JAXBException {

        String xml = """
            <?xml version="1.0" encoding="UTF-8"?>
            <PublicationDelivery xmlns="http://www.netex.org.uk/netex" version="1.13:NO-NeTEx-networktimetable:1.3">
              <PublicationTimestamp>2023-08-03T00:20:33.337</PublicationTimestamp>
              <ParticipantRef>RB</ParticipantRef>
              <dataObjects>
                <CompositeFrame version="1" id="TST:CompositeFrame:1">
                  <frames>
                    <TimetableFrame version="1" id="TST:TimetableFrame:1">
                      <journeyInterchanges>
                        <ServiceJourneyInterchange version="1" id="TST:ServiceJourneyInterchange:1">
                          <StaySeated>true</StaySeated>
                          <Guaranteed>true</Guaranteed>
                          <MaximumWaitTime>PT5M</MaximumWaitTime>
                          <FromPointRef ref="TST:ScheduledStopPoint:1"/>
                          <ToPointRef ref="TST:ScheduledStopPoint:2"/>
                          <FromJourneyRef ref="TST:ServiceJourney:1"/>
                          <ToJourneyRef ref="TST:ServiceJourney:2"/>
                        </ServiceJourneyInterchange>
                      </journeyInterchanges>
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
                          <Name>Flex Service</Name>
                          <TransportMode>bus</TransportMode>
                          <FlexibleServiceProperties version="1" id="TST:FlexibleServiceProperties:1">
                            <BookingAccess>public</BookingAccess>
                            <BookWhen>dayOfTravelOnly</BookWhen>
                            <LatestBookingTime>12:00:00</LatestBookingTime>
                            <BookingContact>
                              <Phone>+47 12345678</Phone>
                              <Url>https://booking.example.com</Url>
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
