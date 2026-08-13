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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

class UnmarshalServiceFrameChouetteTest extends AbstractUnmarshalFrameTest {

    @Test
    void unmarshalBookingArrangementsOnStopPointInJourneyPattern() throws JAXBException {

        String xml = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
                "<PublicationDelivery xmlns=\"http://www.netex.org.uk/netex\" version=\"1.13:NO-NeTEx-networktimetable:1.3\">\n" +
                "  <PublicationTimestamp>2023-08-03T00:20:33.337</PublicationTimestamp>\n" +
                "  <ParticipantRef>RB</ParticipantRef>\n" +
                "  <dataObjects>\n" +
                "    <CompositeFrame version=\"1\" id=\"TST:CompositeFrame:1\">\n" +
                "      <frames>\n" +
                "        <ServiceFrame version=\"1\" id=\"TST:ServiceFrame:1\">\n" +
                "          <journeyPatterns>\n" +
                "            <JourneyPattern version=\"1\" id=\"TST:JourneyPattern:1\">\n" +
                "              <pointsInSequence>\n" +
                "                <StopPointInJourneyPattern order=\"1\" version=\"1\" id=\"TST:StopPointInJourneyPattern:1\">\n" +
                "                  <ScheduledStopPointRef ref=\"TST:ScheduledStopPoint:1\"/>\n" +
                "                  <BookingArrangements>\n" +
                "                    <BookingAccess>public</BookingAccess>\n" +
                "                    <BookWhen>dayOfTravelOnly</BookWhen>\n" +
                "                    <BuyWhen>beforeBoarding</BuyWhen>\n" +
                "                    <BookingMethods>callOffice online</BookingMethods>\n" +
                "                    <LatestBookingTime>14:00:00</LatestBookingTime>\n" +
                "                    <MinimumBookingPeriod>PT2H</MinimumBookingPeriod>\n" +
                "                    <BookingContact>\n" +
                "                      <Phone>+47 55551234</Phone>\n" +
                "                      <Url>https://booking.example.com</Url>\n" +
                "                    </BookingContact>\n" +
                "                    <BookingNote>Call to book</BookingNote>\n" +
                "                  </BookingArrangements>\n" +
                "                </StopPointInJourneyPattern>\n" +
                "              </pointsInSequence>\n" +
                "            </JourneyPattern>\n" +
                "          </journeyPatterns>\n" +
                "        </ServiceFrame>\n" +
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

        String xml = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
                "<PublicationDelivery xmlns=\"http://www.netex.org.uk/netex\" version=\"1.13:NO-NeTEx-networktimetable:1.3\">\n" +
                "  <PublicationTimestamp>2023-08-03T00:20:33.337</PublicationTimestamp>\n" +
                "  <ParticipantRef>RB</ParticipantRef>\n" +
                "  <dataObjects>\n" +
                "    <CompositeFrame version=\"1\" id=\"TST:CompositeFrame:1\">\n" +
                "      <frames>\n" +
                "        <ServiceFrame version=\"1\" id=\"TST:ServiceFrame:1\">\n" +
                "          <destinationDisplays>\n" +
                "            <DestinationDisplay version=\"1\" id=\"TST:DestinationDisplay:1\">\n" +
                "              <FrontText>Bergen</FrontText>\n" +
                "              <vias>\n" +
                "                <Via>\n" +
                "                  <DestinationDisplayRef ref=\"TST:DestinationDisplay:Via1\"/>\n" +
                "                </Via>\n" +
                "                <Via>\n" +
                "                  <DestinationDisplayRef ref=\"TST:DestinationDisplay:Via2\"/>\n" +
                "                </Via>\n" +
                "              </vias>\n" +
                "            </DestinationDisplay>\n" +
                "          </destinationDisplays>\n" +
                "        </ServiceFrame>\n" +
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

        String xml = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
                "<PublicationDelivery xmlns=\"http://www.netex.org.uk/netex\" version=\"1.13:NO-NeTEx-networktimetable:1.3\">\n" +
                "  <PublicationTimestamp>2023-08-03T00:20:33.337</PublicationTimestamp>\n" +
                "  <ParticipantRef>RB</ParticipantRef>\n" +
                "  <dataObjects>\n" +
                "    <CompositeFrame version=\"1\" id=\"TST:CompositeFrame:1\">\n" +
                "      <frames>\n" +
                "        <ServiceFrame version=\"1\" id=\"TST:ServiceFrame:1\">\n" +
                "          <directions>\n" +
                "            <Direction version=\"1\" id=\"TST:Direction:Outbound\">\n" +
                "              <Name>Outbound</Name>\n" +
                "              <DirectionType>outbound</DirectionType>\n" +
                "            </Direction>\n" +
                "            <Direction version=\"1\" id=\"TST:Direction:Inbound\">\n" +
                "              <Name>Inbound</Name>\n" +
                "              <DirectionType>inbound</DirectionType>\n" +
                "            </Direction>\n" +
                "          </directions>\n" +
                "          <routes>\n" +
                "            <Route version=\"1\" id=\"TST:Route:1\">\n" +
                "              <Name>Main Route</Name>\n" +
                "              <DirectionType>outbound</DirectionType>\n" +
                "              <DirectionRef ref=\"TST:Direction:Outbound\"/>\n" +
                "              <InverseRouteRef ref=\"TST:Route:2\"/>\n" +
                "              <pointsInSequence>\n" +
                "                <PointOnRoute order=\"1\" version=\"1\" id=\"TST:PointOnRoute:1\">\n" +
                "                  <RoutePointRef ref=\"TST:RoutePoint:1\"/>\n" +
                "                </PointOnRoute>\n" +
                "              </pointsInSequence>\n" +
                "            </Route>\n" +
                "          </routes>\n" +
                "        </ServiceFrame>\n" +
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

        String xml = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
                "<PublicationDelivery xmlns=\"http://www.netex.org.uk/netex\" version=\"1.13:NO-NeTEx-networktimetable:1.3\">\n" +
                "  <PublicationTimestamp>2023-08-03T00:20:33.337</PublicationTimestamp>\n" +
                "  <ParticipantRef>RB</ParticipantRef>\n" +
                "  <dataObjects>\n" +
                "    <CompositeFrame version=\"1\" id=\"TST:CompositeFrame:1\">\n" +
                "      <frames>\n" +
                "        <ServiceFrame version=\"1\" id=\"TST:ServiceFrame:1\">\n" +
                "          <routePoints>\n" +
                "            <RoutePoint version=\"1\" id=\"TST:RoutePoint:1\">\n" +
                "              <projections>\n" +
                "                <PointProjection version=\"1\" id=\"TST:PointProjection:1\">\n" +
                "                  <ProjectToPointRef ref=\"TST:ScheduledStopPoint:1\"/>\n" +
                "                </PointProjection>\n" +
                "              </projections>\n" +
                "              <BorderCrossing>true</BorderCrossing>\n" +
                "            </RoutePoint>\n" +
                "          </routePoints>\n" +
                "        </ServiceFrame>\n" +
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
        ServiceFrame serviceFrame = (ServiceFrame) compositeFrame.getFrames().getCommonFrame().get(0).getValue();

        RoutePoint routePoint = serviceFrame.getRoutePoints().getRoutePoint().get(0);
        assertTrue(routePoint.isBorderCrossing());

        assertNotNull(routePoint.getProjections());
        PointProjection projection = (PointProjection) routePoint.getProjections().getProjectionRefOrProjection().get(0).getValue();
        assertEquals("TST:ScheduledStopPoint:1", projection.getProjectToPointRef().getRef());
    }

    @Test
    void unmarshalScheduledStopPointWithTimingPointStatus() throws JAXBException {

        String xml = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
                "<PublicationDelivery xmlns=\"http://www.netex.org.uk/netex\" version=\"1.13:NO-NeTEx-networktimetable:1.3\">\n" +
                "  <PublicationTimestamp>2023-08-03T00:20:33.337</PublicationTimestamp>\n" +
                "  <ParticipantRef>RB</ParticipantRef>\n" +
                "  <dataObjects>\n" +
                "    <CompositeFrame version=\"1\" id=\"TST:CompositeFrame:1\">\n" +
                "      <frames>\n" +
                "        <ServiceFrame version=\"1\" id=\"TST:ServiceFrame:1\">\n" +
                "          <scheduledStopPoints>\n" +
                "            <ScheduledStopPoint version=\"1\" id=\"TST:ScheduledStopPoint:1\">\n" +
                "              <Name>Timing Stop</Name>\n" +
                "              <TimingPointStatus>timingPoint</TimingPointStatus>\n" +
                "            </ScheduledStopPoint>\n" +
                "            <ScheduledStopPoint version=\"1\" id=\"TST:ScheduledStopPoint:2\">\n" +
                "              <Name>Non-Timing Stop</Name>\n" +
                "              <TimingPointStatus>notTimingPoint</TimingPointStatus>\n" +
                "            </ScheduledStopPoint>\n" +
                "          </scheduledStopPoints>\n" +
                "        </ServiceFrame>\n" +
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
        ServiceFrame serviceFrame = (ServiceFrame) compositeFrame.getFrames().getCommonFrame().get(0).getValue();

        ScheduledStopPoint ssp1 = serviceFrame.getScheduledStopPoints().getScheduledStopPoint().get(0);
        assertEquals("Timing Stop", getStringValue(ssp1.getName()));
        assertEquals(TimingPointStatusEnumeration.TIMING_POINT, ssp1.getTimingPointStatus());

        ScheduledStopPoint ssp2 = serviceFrame.getScheduledStopPoints().getScheduledStopPoint().get(1);
        assertEquals(TimingPointStatusEnumeration.NOT_TIMING_POINT, ssp2.getTimingPointStatus());
    }

    @Test
    void unmarshalLinePresentationAndPrivateCode() throws JAXBException {

        String xml = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
                "<PublicationDelivery xmlns=\"http://www.netex.org.uk/netex\" version=\"1.13:NO-NeTEx-networktimetable:1.3\">\n" +
                "  <PublicationTimestamp>2023-08-03T00:20:33.337</PublicationTimestamp>\n" +
                "  <ParticipantRef>RB</ParticipantRef>\n" +
                "  <dataObjects>\n" +
                "    <CompositeFrame version=\"1\" id=\"TST:CompositeFrame:1\">\n" +
                "      <frames>\n" +
                "        <ServiceFrame version=\"1\" id=\"TST:ServiceFrame:1\">\n" +
                "          <lines>\n" +
                "            <Line version=\"1\" id=\"TST:Line:1\">\n" +
                "              <Name>Line One</Name>\n" +
                "              <TransportMode>bus</TransportMode>\n" +
                "              <PrivateCode>PRIV001</PrivateCode>\n" +
                "              <Presentation>\n" +
                "                <Colour>0054A6</Colour>\n" +
                "                <TextColour>FFFFFF</TextColour>\n" +
                "              </Presentation>\n" +
                "            </Line>\n" +
                "          </lines>\n" +
                "        </ServiceFrame>\n" +
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

        String xml = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
                "<PublicationDelivery xmlns=\"http://www.netex.org.uk/netex\" version=\"1.13:NO-NeTEx-networktimetable:1.3\">\n" +
                "  <PublicationTimestamp>2023-08-03T00:20:33.337</PublicationTimestamp>\n" +
                "  <ParticipantRef>RB</ParticipantRef>\n" +
                "  <dataObjects>\n" +
                "    <CompositeFrame version=\"1\" id=\"TST:CompositeFrame:1\">\n" +
                "      <frames>\n" +
                "        <ServiceFrame version=\"1\" id=\"TST:ServiceFrame:1\">\n" +
                "          <notices>\n" +
                "            <Notice version=\"1\" id=\"TST:Notice:1\">\n" +
                "              <Text lang=\"no\">Stopper ikke alle holdeplasser</Text>\n" +
                "              <PublicCode>A</PublicCode>\n" +
                "              <alternativeTexts>\n" +
                "                <AlternativeText version=\"1\" id=\"TST:AlternativeText:1\">\n" +
                "                  <Text lang=\"en\">Does not stop at all stops</Text>\n" +
                "                </AlternativeText>\n" +
                "                <AlternativeText version=\"1\" id=\"TST:AlternativeText:2\">\n" +
                "                  <Text lang=\"sv\">Stannar inte vid alla hallplatser</Text>\n" +
                "                </AlternativeText>\n" +
                "              </alternativeTexts>\n" +
                "            </Notice>\n" +
                "          </notices>\n" +
                "        </ServiceFrame>\n" +
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

        String xml = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
                "<PublicationDelivery xmlns=\"http://www.netex.org.uk/netex\" xmlns:gis=\"http://www.opengis.net/gml/3.2\" version=\"1.13:NO-NeTEx-networktimetable:1.3\">\n" +
                "  <PublicationTimestamp>2023-08-03T00:20:33.337</PublicationTimestamp>\n" +
                "  <ParticipantRef>RB</ParticipantRef>\n" +
                "  <dataObjects>\n" +
                "    <CompositeFrame version=\"1\" id=\"TST:CompositeFrame:1\">\n" +
                "      <frames>\n" +
                "        <ServiceFrame version=\"1\" id=\"TST:ServiceFrame:1\">\n" +
                "          <serviceLinks>\n" +
                "            <ServiceLink version=\"1\" id=\"TST:ServiceLink:1\">\n" +
                "              <FromPointRef ref=\"TST:ScheduledStopPoint:1\"/>\n" +
                "              <ToPointRef ref=\"TST:ScheduledStopPoint:2\"/>\n" +
                "              <projections>\n" +
                "                <LinkSequenceProjection version=\"1\" id=\"TST:LinkSequenceProjection:1\">\n" +
                "                  <gis:LineString gis:id=\"GEN-LineString-1\">\n" +
                "                    <gis:posList>59.91 10.75 59.92 10.76</gis:posList>\n" +
                "                  </gis:LineString>\n" +
                "                </LinkSequenceProjection>\n" +
                "              </projections>\n" +
                "            </ServiceLink>\n" +
                "          </serviceLinks>\n" +
                "        </ServiceFrame>\n" +
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

        String xml = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
                "<PublicationDelivery xmlns=\"http://www.netex.org.uk/netex\" version=\"1.13:NO-NeTEx-networktimetable:1.3\">\n" +
                "  <PublicationTimestamp>2023-08-03T00:20:33.337</PublicationTimestamp>\n" +
                "  <ParticipantRef>RB</ParticipantRef>\n" +
                "  <dataObjects>\n" +
                "    <CompositeFrame version=\"1\" id=\"TST:CompositeFrame:1\">\n" +
                "      <codespaces>\n" +
                "        <Codespace id=\"tst\">\n" +
                "          <Xmlns>TST</Xmlns>\n" +
                "          <XmlnsUrl>http://www.rutebanken.org/ns/tst</XmlnsUrl>\n" +
                "        </Codespace>\n" +
                "        <Codespace id=\"nsr\">\n" +
                "          <Xmlns>NSR</Xmlns>\n" +
                "          <XmlnsUrl>http://www.rutebanken.org/ns/nsr</XmlnsUrl>\n" +
                "        </Codespace>\n" +
                "      </codespaces>\n" +
                "      <frames>\n" +
                "        <ServiceFrame version=\"1\" id=\"TST:ServiceFrame:1\"/>\n" +
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

        assertEquals(2, compositeFrame.getCodespaces().getCodespaceRefOrCodespace().size());
        Codespace codespace1 = (Codespace) compositeFrame.getCodespaces().getCodespaceRefOrCodespace().get(0);
        assertEquals("TST", codespace1.getXmlns());
        assertEquals("http://www.rutebanken.org/ns/tst", codespace1.getXmlnsUrl());

        Codespace codespace2 = (Codespace) compositeFrame.getCodespaces().getCodespaceRefOrCodespace().get(1);
        assertEquals("NSR", codespace2.getXmlns());
        assertEquals("http://www.rutebanken.org/ns/nsr", codespace2.getXmlnsUrl());
    }
}
