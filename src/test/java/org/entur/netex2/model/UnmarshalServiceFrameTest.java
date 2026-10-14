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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

class UnmarshalServiceFrameTest extends AbstractUnmarshalFrameTest{

    @Test
    void unmarshalServiceFrame() throws JAXBException {

        String xml = """
            <?xml version="1.0" encoding="UTF-8"?>
            <PublicationDelivery xmlns="http://www.netex.org.uk/netex" xmlns:gis="http://www.opengis.net/gml/3.2" xmlns:siri="http://www.siri.org.uk/siri" version="1.13:NO-NeTEx-networktimetable:1.3">
              <PublicationTimestamp>2023-08-03T00:20:33.337</PublicationTimestamp>
              <ParticipantRef>RB</ParticipantRef>
              <Description>Narvik-Riksgränsen (Sverige)</Description>
              <dataObjects>
                <CompositeFrame created="2023-07-13T11:42:43.27" version="1" id="VYG:CompositeFrame:24806950">
                  <validityConditions>
                    <AvailabilityCondition version="1" id="VYG:AvailabilityCondition:24806862">
                      <FromDate>2023-07-31T00:00:00</FromDate>
                      <ToDate>2024-08-01T00:00:00</ToDate>
                    </AvailabilityCondition>
                  </validityConditions>
                  <codespaces>
                    <Codespace id="vyg">
                      <Xmlns>VYG</Xmlns>
                      <XmlnsUrl>http://www.rutebanken.org/ns/vyg</XmlnsUrl>
                    </Codespace>
                    <Codespace id="pen">
                      <Xmlns>PEN</Xmlns>
                      <XmlnsUrl>http://www.rutebanken.org/ns/pen</XmlnsUrl>
                    </Codespace>
                    <Codespace id="nsr">
                      <Xmlns>NSR</Xmlns>
                      <XmlnsUrl>http://www.rutebanken.org/ns/nsr</XmlnsUrl>
                    </Codespace>
                  </codespaces>
                  <FrameDefaults>
                    <DefaultLocale>
                      <TimeZone>Europe/Oslo</TimeZone>
                      <DefaultLanguage>no</DefaultLanguage>
                    </DefaultLocale>
                  </FrameDefaults>
                  <frames>
                    <ServiceFrame version="1" id="VYG:ServiceFrame:24806951">
                      <routes>
                        <Route version="0" id="VYG:Route:F8-R">
                          <Name>Riksgränsen-Narvik  Ofotbanen</Name>
                          <ShortName>KJ-NK</ShortName>
                          <LineRef ref="VYG:Line:F8" version="3"></LineRef>
                          <pointsInSequence>
                            <PointOnRoute order="1" version="1" id="VYG:PointOnRoute:24806875">
                              <RoutePointRef ref="VYG:RoutePoint:KJ"></RoutePointRef>
                            </PointOnRoute>
                            <PointOnRoute order="12" version="1" id="VYG:PointOnRoute:24806886">
                              <RoutePointRef ref="VYG:RoutePoint:NK"></RoutePointRef>
                            </PointOnRoute>
                          </pointsInSequence>
                          <InverseRouteRef ref="VYG:Route:F8-F" version="0"></InverseRouteRef>
                        </Route>
                      </routes>
                      <lines>
                        <Line version="3" id="VYG:Line:F8">
                          <Name>Narvik-Riksgränsen (Sverige)</Name>
                          <TransportMode>rail</TransportMode>
                          <PublicCode>F8</PublicCode>
                          <PrivateCode>F8</PrivateCode>
                          <OperatorRef ref="VYG:Operator:TAG"></OperatorRef>
                          <RepresentedByGroupRef ref="VYG:Network:TAG"></RepresentedByGroupRef>
                          <Presentation>
                            <Colour>0054A6</Colour>
                            <TextColour>FFFFFF</TextColour>
                          </Presentation>
                        </Line>
                      </lines>
                      <journeyPatterns>
                        <JourneyPattern version="0" id="VYG:JourneyPattern:F8-1089">
                          <Name>KMB-NK</Name>
                          <RouteRef ref="VYG:Route:F8-R" version="0"></RouteRef>
                          <pointsInSequence>
                            <StopPointInJourneyPattern order="1" version="0" id="VYG:StopPointInJourneyPattern:F8-1089-1">
                              <ScheduledStopPointRef ref="VYG:ScheduledStopPoint:KMB-1"></ScheduledStopPointRef>
                              <ForAlighting>false</ForAlighting>
                              <DestinationDisplayRef ref="VYG:DestinationDisplay:F8-NK"></DestinationDisplayRef>
                            </StopPointInJourneyPattern>
                            <StopPointInJourneyPattern order="12" version="0" id="VYG:StopPointInJourneyPattern:F8-1089-12">
                              <ScheduledStopPointRef ref="VYG:ScheduledStopPoint:NK-1"></ScheduledStopPointRef>
                              <ForBoarding>false</ForBoarding>
                            </StopPointInJourneyPattern>
                          </pointsInSequence>
                          <linksInSequence>
                            <ServiceLinkInJourneyPattern order="1" version="1" id="VYG:ServiceLinkInJourneyPattern:24806921">
                              <ServiceLinkRef ref="VYG:ServiceLink:KMB-1_ABOe-1_NULL"></ServiceLinkRef>
                            </ServiceLinkInJourneyPattern>
                            <ServiceLinkInJourneyPattern order="11" version="1" id="VYG:ServiceLinkInJourneyPattern:24806935">
                              <ServiceLinkRef ref="VYG:ServiceLink:ROM-1_NK-1_-860902437"></ServiceLinkRef>
                            </ServiceLinkInJourneyPattern>
                          </linksInSequence>
                        </JourneyPattern>
                      </journeyPatterns>
                      <stopAssignments>
                        <PassengerStopAssignment order="34" version="1" id="VYG:PassengerStopAssignment:HAL-1">
                          <ScheduledStopPointRef ref="VYG:ScheduledStopPoint:HAL-1" version="1"></ScheduledStopPointRef>
                          <QuayRef ref="NSR:Quay:111"></QuayRef>
                        </PassengerStopAssignment>
                        <PassengerStopAssignment order="64" version="1" id="VYG:PassengerStopAssignment:HAL-2">
                          <ScheduledStopPointRef ref="VYG:ScheduledStopPoint:HAL-2" version="1"></ScheduledStopPointRef>
                          <QuayRef ref="NSR:Quay:110"></QuayRef>
                        </PassengerStopAssignment>
                      </stopAssignments>
                    </ServiceFrame>
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

        ServiceFrame serviceFrame = (ServiceFrame) compositeFrame.getFrames().getCommonFrame().getFirst().getValue();
        Line line = (Line) serviceFrame.getLines().getLine().getFirst().getValue();
        assertEquals("Narvik-Riksgränsen (Sverige)", getStringValue(line.getName()));
        assertEquals("F8", line.getPublicCode().getValue());
        assertEquals("F8", line.getPrivateCode().getValue());
        assertEquals("rail", line.getTransportMode().value());

        Route route = (Route) serviceFrame.getRoutes().getRoute().getFirst().getValue();
        assertEquals("Riksgränsen-Narvik  Ofotbanen", getStringValue(route.getName()));
        assertEquals("KJ-NK", getStringValue(route.getShortName()));

        PointOnRoute pointOnRoute = route.getPointsInSequence().getPointOnRoute().getFirst();
        assertEquals("VYG:RoutePoint:KJ", pointOnRoute.getPointRef().getValue().getRef());

        JourneyPattern journeyPattern = (JourneyPattern) serviceFrame.getJourneyPatterns().getJourneyPattern().getFirst().getValue();
        assertEquals("KMB-NK", getStringValue(journeyPattern.getName()));
        assertEquals("VYG:Route:F8-R", journeyPattern.getRouteRef().getRef());

        StopPointInJourneyPattern stopPointInJourneyPattern = (StopPointInJourneyPattern) journeyPattern.getPointsInSequence().getPointInJourneyPatternOrStopPointInJourneyPatternOrTimingPointInJourneyPattern().getFirst();
        assertEquals("VYG:ScheduledStopPoint:KMB-1", stopPointInJourneyPattern.getScheduledStopPointRef().getValue().getRef());
        assertFalse(stopPointInJourneyPattern.isForAlighting());
        assertEquals("VYG:DestinationDisplay:F8-NK", stopPointInJourneyPattern.getDestinationDisplayRef().getRef());

        ServiceLinkInJourneyPattern_VersionedChildStructure serviceLinkInJourneyPattern = (ServiceLinkInJourneyPattern_VersionedChildStructure) journeyPattern.getLinksInSequence().getServiceLinkInJourneyPatternOrTimingLinkInJourneyPattern().getFirst();
        assertEquals("VYG:ServiceLink:KMB-1_ABOe-1_NULL",serviceLinkInJourneyPattern.getServiceLinkRef().getRef());

        PassengerStopAssignment passengerStopAssignment = (PassengerStopAssignment) serviceFrame.getStopAssignments().getStopAssignment().getFirst().getValue();
        assertEquals("NSR:Quay:111", passengerStopAssignment.getQuayRef().getValue().getRef());
        assertEquals("VYG:ScheduledStopPoint:HAL-1", passengerStopAssignment.getScheduledStopPointRef().getValue().getRef());


    }


}
