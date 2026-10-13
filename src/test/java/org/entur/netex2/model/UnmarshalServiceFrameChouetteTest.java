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
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

class UnmarshalServiceFrameChouetteTest extends AbstractUnmarshalFrameTest {

    @Test
    void unmarshalBookingArrangementsOnStopPointInJourneyPattern() throws JAXBException {

        String xml = """
            <?xml version="1.0" encoding="UTF-8"?>
            <PublicationDelivery xmlns="http://www.netex.org.uk/netex" version="1.13:NO-NeTEx-networktimetable:1.3">
              <PublicationTimestamp>2023-08-03T00:20:33.337</PublicationTimestamp>
              <ParticipantRef>RB</ParticipantRef>
              <dataObjects>
                <CompositeFrame version="1" id="TST:CompositeFrame:1">
                  <frames>
                    <ServiceFrame version="1" id="TST:ServiceFrame:1">
                      <journeyPatterns>
                        <JourneyPattern version="1" id="TST:JourneyPattern:1">
                          <pointsInSequence>
                            <StopPointInJourneyPattern order="1" version="1" id="TST:StopPointInJourneyPattern:1">
                              <ScheduledStopPointRef ref="TST:ScheduledStopPoint:1"/>
                              <BookingArrangements>
                                <BookingAccess>public</BookingAccess>
                                <BookWhen>dayOfTravelOnly</BookWhen>
                                <BuyWhen>beforeBoarding</BuyWhen>
                                <BookingMethods>callOffice online</BookingMethods>
                                <LatestBookingTime>14:00:00</LatestBookingTime>
                                <MinimumBookingPeriod>PT2H</MinimumBookingPeriod>
                                <BookingContact>
                                  <Phone>+47 55551234</Phone>
                                  <Url>https://booking.example.com</Url>
                                </BookingContact>
                                <BookingNote>Call to book</BookingNote>
                              </BookingArrangements>
                            </StopPointInJourneyPattern>
                          </pointsInSequence>
                        </JourneyPattern>
                      </journeyPatterns>
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
        CompositeFrame compositeFrame = (CompositeFrame) publicationDeliveryStructure.getDataObjects().getCompositeFrameOrCommonFrame().get(0).getValue();
        ServiceFrame serviceFrame = (ServiceFrame) compositeFrame.getFrames().getCommonFrame().get(0).getValue();

        JourneyPattern journeyPattern = (JourneyPattern) serviceFrame.getJourneyPatterns().getJourneyPattern_Dummy().get(0).getValue();
        StopPointInJourneyPattern stopPoint = (StopPointInJourneyPattern) journeyPattern.getPointsInSequence()
                .getPointInJourneyPatternOrStopPointInJourneyPatternOrTimingPointInJourneyPattern().get(0);

        BookingArrangementsStructure booking = stopPoint.getBookingArrangements();
        assertNotNull(booking);
        assertEquals(BookingAccessEnumeration.PUBLIC, booking.getBookingAccess());
        assertEquals(PurchaseWhenEnumeration.DAY_OF_TRAVEL_ONLY, booking.getBookWhen());
        assertEquals(1, booking.getBuyWhen().size());
        assertEquals(PurchaseMomentEnumeration.BEFORE_BOARDING, booking.getBuyWhen().get(0));
        assertEquals(java.time.LocalTime.of(14, 0), booking.getLatestBookingTime());
        assertEquals(java.time.Duration.ofHours(2), booking.getMinimumBookingPeriod());
        assertEquals("+47 55551234", booking.getBookingContact().getPhone());
        assertEquals("https://booking.example.com", booking.getBookingContact().getUrl());
        assertEquals("Call to book", getStringValue(booking.getBookingNote()));
        assertEquals(2, booking.getBookingMethods().size());
        assertEquals(BookingMethodEnumeration.CALL_OFFICE, booking.getBookingMethods().get(0));
        assertEquals(BookingMethodEnumeration.ONLINE, booking.getBookingMethods().get(1));
    }

    @Test
    void unmarshalDestinationDisplayWithVias() throws JAXBException {

        String xml = """
            <?xml version="1.0" encoding="UTF-8"?>
            <PublicationDelivery xmlns="http://www.netex.org.uk/netex" version="1.13:NO-NeTEx-networktimetable:1.3">
              <PublicationTimestamp>2023-08-03T00:20:33.337</PublicationTimestamp>
              <ParticipantRef>RB</ParticipantRef>
              <dataObjects>
                <CompositeFrame version="1" id="TST:CompositeFrame:1">
                  <frames>
                    <ServiceFrame version="1" id="TST:ServiceFrame:1">
                      <destinationDisplays>
                        <DestinationDisplay version="1" id="TST:DestinationDisplay:1">
                          <FrontText>Bergen</FrontText>
                          <vias>
                            <Via>
                              <DestinationDisplayRef ref="TST:DestinationDisplay:Via1"/>
                            </Via>
                            <Via>
                              <DestinationDisplayRef ref="TST:DestinationDisplay:Via2"/>
                            </Via>
                          </vias>
                        </DestinationDisplay>
                      </destinationDisplays>
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
        CompositeFrame compositeFrame = (CompositeFrame) publicationDeliveryStructure.getDataObjects().getCompositeFrameOrCommonFrame().get(0).getValue();
        ServiceFrame serviceFrame = (ServiceFrame) compositeFrame.getFrames().getCommonFrame().get(0).getValue();

        DestinationDisplay dd = serviceFrame.getDestinationDisplays().getDestinationDisplay().get(0);
        assertEquals("Bergen", getStringValue(dd.getFrontText()));
        assertNotNull(dd.getVias());
        assertEquals(2, dd.getVias().getVia().size());

        Via_VersionedChildStructure via1 = dd.getVias().getVia().get(0);
        assertEquals("TST:DestinationDisplay:Via1", via1.getDestinationDisplayRef().getRef());
        Via_VersionedChildStructure via2 = dd.getVias().getVia().get(1);
        assertEquals("TST:DestinationDisplay:Via2", via2.getDestinationDisplayRef().getRef());
    }

    @Test
    void unmarshalDirectionAndRouteDirectionType() throws JAXBException {

        String xml = """
            <?xml version="1.0" encoding="UTF-8"?>
            <PublicationDelivery xmlns="http://www.netex.org.uk/netex" version="1.13:NO-NeTEx-networktimetable:1.3">
              <PublicationTimestamp>2023-08-03T00:20:33.337</PublicationTimestamp>
              <ParticipantRef>RB</ParticipantRef>
              <dataObjects>
                <CompositeFrame version="1" id="TST:CompositeFrame:1">
                  <frames>
                    <ServiceFrame version="1" id="TST:ServiceFrame:1">
                      <directions>
                        <Direction version="1" id="TST:Direction:Outbound">
                          <Name>Outbound</Name>
                          <DirectionType>outbound</DirectionType>
                        </Direction>
                        <Direction version="1" id="TST:Direction:Inbound">
                          <Name>Inbound</Name>
                          <DirectionType>inbound</DirectionType>
                        </Direction>
                      </directions>
                      <routes>
                        <Route version="1" id="TST:Route:1">
                          <Name>Main Route</Name>
                          <DirectionType>outbound</DirectionType>
                          <DirectionRef ref="TST:Direction:Outbound"/>
                          <InverseRouteRef ref="TST:Route:2"/>
                          <pointsInSequence>
                            <PointOnRoute order="1" version="1" id="TST:PointOnRoute:1">
                              <RoutePointRef ref="TST:RoutePoint:1"/>
                            </PointOnRoute>
                          </pointsInSequence>
                        </Route>
                      </routes>
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
        CompositeFrame compositeFrame = (CompositeFrame) publicationDeliveryStructure.getDataObjects().getCompositeFrameOrCommonFrame().get(0).getValue();
        ServiceFrame serviceFrame = (ServiceFrame) compositeFrame.getFrames().getCommonFrame().get(0).getValue();

        assertEquals(2, serviceFrame.getDirections().getDirection().size());
        Direction outbound = serviceFrame.getDirections().getDirection().get(0);
        assertEquals("Outbound", getStringValue(outbound.getName()));
        assertEquals(DirectionTypeEnumeration.OUTBOUND, outbound.getDirectionType());
        Direction inbound = serviceFrame.getDirections().getDirection().get(1);
        assertEquals(DirectionTypeEnumeration.INBOUND, inbound.getDirectionType());

        Route route = (Route) serviceFrame.getRoutes().getRoute_Dummy().get(0).getValue();
        assertEquals(DirectionTypeEnumeration.OUTBOUND, route.getDirectionType());
        assertEquals("TST:Direction:Outbound", route.getDirectionRef().getRef());
        assertEquals("TST:Route:2", route.getInverseRouteRef().getRef());
    }

    @Test
    void unmarshalRoutePointWithProjection() throws JAXBException {

        String xml = """
            <?xml version="1.0" encoding="UTF-8"?>
            <PublicationDelivery xmlns="http://www.netex.org.uk/netex" version="1.13:NO-NeTEx-networktimetable:1.3">
              <PublicationTimestamp>2023-08-03T00:20:33.337</PublicationTimestamp>
              <ParticipantRef>RB</ParticipantRef>
              <dataObjects>
                <CompositeFrame version="1" id="TST:CompositeFrame:1">
                  <frames>
                    <ServiceFrame version="1" id="TST:ServiceFrame:1">
                      <routePoints>
                        <RoutePoint version="1" id="TST:RoutePoint:1">
                          <projections>
                            <PointProjection version="1" id="TST:PointProjection:1">
                              <ProjectToPointRef ref="TST:ScheduledStopPoint:1"/>
                            </PointProjection>
                          </projections>
                          <BorderCrossing>true</BorderCrossing>
                        </RoutePoint>
                      </routePoints>
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
        CompositeFrame compositeFrame = (CompositeFrame) publicationDeliveryStructure.getDataObjects().getCompositeFrameOrCommonFrame().get(0).getValue();
        ServiceFrame serviceFrame = (ServiceFrame) compositeFrame.getFrames().getCommonFrame().get(0).getValue();

        RoutePoint routePoint = serviceFrame.getRoutePoints().getRoutePoint().get(0);
        assertTrue(routePoint.isBorderCrossing());

        assertNotNull(routePoint.getProjections());
        PointProjection projection = (PointProjection) routePoint.getProjections().getProjectionRefOrProjection().get(0).getValue();
        assertEquals("TST:ScheduledStopPoint:1", projection.getProjectToPointRef().getRef());
    }

    @Test
    void unmarshalScheduledStopPointWithTimingPointStatus() throws JAXBException {

        String xml = """
            <?xml version="1.0" encoding="UTF-8"?>
            <PublicationDelivery xmlns="http://www.netex.org.uk/netex" version="1.13:NO-NeTEx-networktimetable:1.3">
              <PublicationTimestamp>2023-08-03T00:20:33.337</PublicationTimestamp>
              <ParticipantRef>RB</ParticipantRef>
              <dataObjects>
                <CompositeFrame version="1" id="TST:CompositeFrame:1">
                  <frames>
                    <ServiceFrame version="1" id="TST:ServiceFrame:1">
                      <scheduledStopPoints>
                        <ScheduledStopPoint version="1" id="TST:ScheduledStopPoint:1">
                          <Name>Timing Stop</Name>
                          <TimingPointStatus>timingPoint</TimingPointStatus>
                        </ScheduledStopPoint>
                        <ScheduledStopPoint version="1" id="TST:ScheduledStopPoint:2">
                          <Name>Non-Timing Stop</Name>
                          <TimingPointStatus>notTimingPoint</TimingPointStatus>
                        </ScheduledStopPoint>
                      </scheduledStopPoints>
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
        CompositeFrame compositeFrame = (CompositeFrame) publicationDeliveryStructure.getDataObjects().getCompositeFrameOrCommonFrame().get(0).getValue();
        ServiceFrame serviceFrame = (ServiceFrame) compositeFrame.getFrames().getCommonFrame().get(0).getValue();

        ScheduledStopPoint ssp1 = serviceFrame.getScheduledStopPoints().getScheduledStopPoint().get(0);
        assertEquals("Timing Stop", getStringValue(ssp1.getName()));
        assertEquals(TimingPointStatusEnumeration.TIMING_POINT, ssp1.getTimingPointStatus());

        ScheduledStopPoint ssp2 = serviceFrame.getScheduledStopPoints().getScheduledStopPoint().get(1);
        assertEquals(TimingPointStatusEnumeration.NOT_TIMING_POINT, ssp2.getTimingPointStatus());
    }

    @Test
    void unmarshalLinePresentationAndPrivateCode() throws JAXBException {

        String xml = """
            <?xml version="1.0" encoding="UTF-8"?>
            <PublicationDelivery xmlns="http://www.netex.org.uk/netex" version="1.13:NO-NeTEx-networktimetable:1.3">
              <PublicationTimestamp>2023-08-03T00:20:33.337</PublicationTimestamp>
              <ParticipantRef>RB</ParticipantRef>
              <dataObjects>
                <CompositeFrame version="1" id="TST:CompositeFrame:1">
                  <frames>
                    <ServiceFrame version="1" id="TST:ServiceFrame:1">
                      <lines>
                        <Line version="1" id="TST:Line:1">
                          <Name>Line One</Name>
                          <TransportMode>bus</TransportMode>
                          <PrivateCode>PRIV001</PrivateCode>
                          <Presentation>
                            <Colour>0054A6</Colour>
                            <TextColour>FFFFFF</TextColour>
                          </Presentation>
                        </Line>
                      </lines>
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
        CompositeFrame compositeFrame = (CompositeFrame) publicationDeliveryStructure.getDataObjects().getCompositeFrameOrCommonFrame().get(0).getValue();
        ServiceFrame serviceFrame = (ServiceFrame) compositeFrame.getFrames().getCommonFrame().get(0).getValue();

        Line line = (Line) serviceFrame.getLines().getLine_Dummy().get(0).getValue();
        assertEquals("Line One", getStringValue(line.getName()));
        assertEquals("PRIV001", line.getPrivateCode().getValue());

        PresentationStructure presentation = line.getPresentation();
        assertNotNull(presentation);
        assertNotNull(presentation.getColour());
        assertNotNull(presentation.getTextColour());
    }

    @Test
    void unmarshalNoticeWithAlternativeTexts() throws JAXBException {

        String xml = """
            <?xml version="1.0" encoding="UTF-8"?>
            <PublicationDelivery xmlns="http://www.netex.org.uk/netex" version="1.13:NO-NeTEx-networktimetable:1.3">
              <PublicationTimestamp>2023-08-03T00:20:33.337</PublicationTimestamp>
              <ParticipantRef>RB</ParticipantRef>
              <dataObjects>
                <CompositeFrame version="1" id="TST:CompositeFrame:1">
                  <frames>
                    <ServiceFrame version="1" id="TST:ServiceFrame:1">
                      <notices>
                        <Notice version="1" id="TST:Notice:1">
                          <Text lang="no">Stopper ikke alle holdeplasser</Text>
                          <PublicCode>A</PublicCode>
                          <alternativeTexts>
                            <AlternativeText version="1" id="TST:AlternativeText:1">
                              <Text lang="en">Does not stop at all stops</Text>
                            </AlternativeText>
                            <AlternativeText version="1" id="TST:AlternativeText:2">
                              <Text lang="sv">Stannar inte vid alla hallplatser</Text>
                            </AlternativeText>
                          </alternativeTexts>
                        </Notice>
                      </notices>
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
        CompositeFrame compositeFrame = (CompositeFrame) publicationDeliveryStructure.getDataObjects().getCompositeFrameOrCommonFrame().get(0).getValue();
        ServiceFrame serviceFrame = (ServiceFrame) compositeFrame.getFrames().getCommonFrame().get(0).getValue();

        Notice notice = serviceFrame.getNotices().getNotice().get(0);
        assertEquals("Stopper ikke alle holdeplasser", getStringValue(notice.getText()));
        assertEquals("no", notice.getText().getLang());

        assertNotNull(notice.getAlternativeTexts());
        assertEquals(2, notice.getAlternativeTexts().getAlternativeText().size());

        AlternativeText alt1 = notice.getAlternativeTexts().getAlternativeText().get(0);
        assertEquals("Does not stop at all stops", getStringValue(alt1.getText()));
        assertEquals("en", alt1.getText().getLang());

        AlternativeText alt2 = notice.getAlternativeTexts().getAlternativeText().get(1);
        assertEquals("Stannar inte vid alla hallplatser", getStringValue(alt2.getText()));
        assertEquals("sv", alt2.getText().getLang());
    }

    @Test
    void unmarshalServiceLinkWithLinkSequenceProjection() throws JAXBException {

        String xml = """
            <?xml version="1.0" encoding="UTF-8"?>
            <PublicationDelivery xmlns="http://www.netex.org.uk/netex" xmlns:gis="http://www.opengis.net/gml/3.2" version="1.13:NO-NeTEx-networktimetable:1.3">
              <PublicationTimestamp>2023-08-03T00:20:33.337</PublicationTimestamp>
              <ParticipantRef>RB</ParticipantRef>
              <dataObjects>
                <CompositeFrame version="1" id="TST:CompositeFrame:1">
                  <frames>
                    <ServiceFrame version="1" id="TST:ServiceFrame:1">
                      <serviceLinks>
                        <ServiceLink version="1" id="TST:ServiceLink:1">
                          <FromPointRef ref="TST:ScheduledStopPoint:1"/>
                          <ToPointRef ref="TST:ScheduledStopPoint:2"/>
                          <projections>
                            <LinkSequenceProjection version="1" id="TST:LinkSequenceProjection:1">
                              <gis:LineString gis:id="GEN-LineString-1">
                                <gis:posList>59.91 10.75 59.92 10.76</gis:posList>
                              </gis:LineString>
                            </LinkSequenceProjection>
                          </projections>
                        </ServiceLink>
                      </serviceLinks>
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
        CompositeFrame compositeFrame = (CompositeFrame) publicationDeliveryStructure.getDataObjects().getCompositeFrameOrCommonFrame().get(0).getValue();
        ServiceFrame serviceFrame = (ServiceFrame) compositeFrame.getFrames().getCommonFrame().get(0).getValue();

        ServiceLink serviceLink = serviceFrame.getServiceLinks().getServiceLink().get(0);
        assertEquals("TST:ScheduledStopPoint:1", serviceLink.getFromPointRef().getRef());
        assertEquals("TST:ScheduledStopPoint:2", serviceLink.getToPointRef().getRef());

        assertNotNull(serviceLink.getProjections());
        LinkSequenceProjection_VersionStructure projection = (LinkSequenceProjection_VersionStructure)
                serviceLink.getProjections().getProjectionRefOrProjection().get(0).getValue();
        assertNotNull(projection.getLineString());
    }

    @Test
    void unmarshalCodespace() throws JAXBException {

        String xml = """
            <?xml version="1.0" encoding="UTF-8"?>
            <PublicationDelivery xmlns="http://www.netex.org.uk/netex" version="1.13:NO-NeTEx-networktimetable:1.3">
              <PublicationTimestamp>2023-08-03T00:20:33.337</PublicationTimestamp>
              <ParticipantRef>RB</ParticipantRef>
              <dataObjects>
                <CompositeFrame version="1" id="TST:CompositeFrame:1">
                  <codespaces>
                    <Codespace id="tst">
                      <Xmlns>TST</Xmlns>
                      <XmlnsUrl>http://www.rutebanken.org/ns/tst</XmlnsUrl>
                    </Codespace>
                    <Codespace id="nsr">
                      <Xmlns>NSR</Xmlns>
                      <XmlnsUrl>http://www.rutebanken.org/ns/nsr</XmlnsUrl>
                    </Codespace>
                  </codespaces>
                  <frames>
                    <ServiceFrame version="1" id="TST:ServiceFrame:1"/>
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

        assertEquals(2, compositeFrame.getCodespaces().getCodespaceRefOrCodespace().size());
        Codespace codespace1 = (Codespace) compositeFrame.getCodespaces().getCodespaceRefOrCodespace().get(0);
        assertEquals("TST", codespace1.getXmlns());
        assertEquals("http://www.rutebanken.org/ns/tst", codespace1.getXmlnsUrl());

        Codespace codespace2 = (Codespace) compositeFrame.getCodespaces().getCodespaceRefOrCodespace().get(1);
        assertEquals("NSR", codespace2.getXmlns());
        assertEquals("http://www.rutebanken.org/ns/nsr", codespace2.getXmlnsUrl());
    }
}
