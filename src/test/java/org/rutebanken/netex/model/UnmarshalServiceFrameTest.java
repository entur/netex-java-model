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

import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.JAXBElement;
import jakarta.xml.bind.JAXBException;
import jakarta.xml.bind.Unmarshaller;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

class UnmarshalServiceFrameTest {

    private static JAXBContext jaxbContext;

    @BeforeAll
    public static void initContext() throws JAXBException {
        jaxbContext = JAXBContext.newInstance(PublicationDeliveryStructure.class);

    }

    @Test
    void unmarshalServiceFrame() throws JAXBException {

        String xml = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
                "<PublicationDelivery xmlns=\"http://www.netex.org.uk/netex\" xmlns:gis=\"http://www.opengis.net/gml/3.2\" xmlns:siri=\"http://www.siri.org.uk/siri\" version=\"1.13:NO-NeTEx-networktimetable:1.3\">\n" +
                "  <PublicationTimestamp>2023-08-03T00:20:33.337</PublicationTimestamp>\n" +
                "  <ParticipantRef>RB</ParticipantRef>\n" +
                "  <Description>Narvik-Riksgränsen (Sverige)</Description>\n" +
                "  <dataObjects>\n" +
                "    <CompositeFrame created=\"2023-07-13T11:42:43.27\" version=\"1\" id=\"VYG:CompositeFrame:24806950\">\n" +
                "      <validityConditions>\n" +
                "        <AvailabilityCondition version=\"1\" id=\"VYG:AvailabilityCondition:24806862\">\n" +
                "          <FromDate>2023-07-31T00:00:00</FromDate>\n" +
                "          <ToDate>2024-08-01T00:00:00</ToDate>\n" +
                "        </AvailabilityCondition>\n" +
                "      </validityConditions>\n" +
                "      <codespaces>\n" +
                "        <Codespace id=\"vyg\">\n" +
                "          <Xmlns>VYG</Xmlns>\n" +
                "          <XmlnsUrl>http://www.rutebanken.org/ns/vyg</XmlnsUrl>\n" +
                "        </Codespace>\n" +
                "        <Codespace id=\"pen\">\n" +
                "          <Xmlns>PEN</Xmlns>\n" +
                "          <XmlnsUrl>http://www.rutebanken.org/ns/pen</XmlnsUrl>\n" +
                "        </Codespace>\n" +
                "        <Codespace id=\"nsr\">\n" +
                "          <Xmlns>NSR</Xmlns>\n" +
                "          <XmlnsUrl>http://www.rutebanken.org/ns/nsr</XmlnsUrl>\n" +
                "        </Codespace>\n" +
                "      </codespaces>\n" +
                "      <FrameDefaults>\n" +
                "        <DefaultLocale>\n" +
                "          <TimeZone>Europe/Oslo</TimeZone>\n" +
                "          <DefaultLanguage>no</DefaultLanguage>\n" +
                "        </DefaultLocale>\n" +
                "      </FrameDefaults>\n" +
                "      <frames>\n" +
                "        <ServiceFrame version=\"1\" id=\"VYG:ServiceFrame:24806951\">\n" +
                "          <routes>\n" +
                "            <Route version=\"0\" id=\"VYG:Route:F8-R\">\n" +
                "              <Name>Riksgränsen-Narvik  Ofotbanen</Name>\n" +
                "              <ShortName>KJ-NK</ShortName>\n" +
                "              <LineRef ref=\"VYG:Line:F8\" version=\"3\"></LineRef>\n" +
                "              <pointsInSequence>\n" +
                "                <PointOnRoute order=\"1\" version=\"1\" id=\"VYG:PointOnRoute:24806875\">\n" +
                "                  <RoutePointRef ref=\"VYG:RoutePoint:KJ\"></RoutePointRef>\n" +
                "                </PointOnRoute>\n" +
                "                <PointOnRoute order=\"12\" version=\"1\" id=\"VYG:PointOnRoute:24806886\">\n" +
                "                  <RoutePointRef ref=\"VYG:RoutePoint:NK\"></RoutePointRef>\n" +
                "                </PointOnRoute>\n" +
                "              </pointsInSequence>\n" +
                "              <InverseRouteRef ref=\"VYG:Route:F8-F\" version=\"0\"></InverseRouteRef>\n" +
                "            </Route>\n" +
                "          </routes>\n" +
                "          <lines>\n" +
                "            <Line version=\"3\" id=\"VYG:Line:F8\">\n" +
                "              <Name>Narvik-Riksgränsen (Sverige)</Name>\n" +
                "              <TransportMode>rail</TransportMode>\n" +
                "              <PublicCode>F8</PublicCode>\n" +
                "              <PrivateCode>F8</PrivateCode>\n" +
                "              <OperatorRef ref=\"VYG:Operator:TAG\"></OperatorRef>\n" +
                "              <RepresentedByGroupRef ref=\"VYG:Network:TAG\"></RepresentedByGroupRef>\n" +
                "              <Presentation>\n" +
                "                <Colour>0054A6</Colour>\n" +
                "                <TextColour>FFFFFF</TextColour>\n" +
                "              </Presentation>\n" +
                "            </Line>\n" +
                "          </lines>\n" +
                "          <journeyPatterns>\n" +
                "            <JourneyPattern version=\"0\" id=\"VYG:JourneyPattern:F8-1089\">\n" +
                "              <Name>KMB-NK</Name>\n" +
                "              <RouteRef ref=\"VYG:Route:F8-R\" version=\"0\"></RouteRef>\n" +
                "              <pointsInSequence>\n" +
                "                <StopPointInJourneyPattern order=\"1\" version=\"0\" id=\"VYG:StopPointInJourneyPattern:F8-1089-1\">\n" +
                "                  <ScheduledStopPointRef ref=\"VYG:ScheduledStopPoint:KMB-1\"></ScheduledStopPointRef>\n" +
                "                  <ForAlighting>false</ForAlighting>\n" +
                "                  <DestinationDisplayRef ref=\"VYG:DestinationDisplay:F8-NK\"></DestinationDisplayRef>\n" +
                "                </StopPointInJourneyPattern>\n" +
                "                <StopPointInJourneyPattern order=\"12\" version=\"0\" id=\"VYG:StopPointInJourneyPattern:F8-1089-12\">\n" +
                "                  <ScheduledStopPointRef ref=\"VYG:ScheduledStopPoint:NK-1\"></ScheduledStopPointRef>\n" +
                "                  <ForBoarding>false</ForBoarding>\n" +
                "                </StopPointInJourneyPattern>\n" +
                "              </pointsInSequence>\n" +
                "              <linksInSequence>\n" +
                "                <ServiceLinkInJourneyPattern order=\"1\" version=\"1\" id=\"VYG:ServiceLinkInJourneyPattern:24806921\">\n" +
                "                  <ServiceLinkRef ref=\"VYG:ServiceLink:KMB-1_ABOe-1_NULL\"></ServiceLinkRef>\n" +
                "                </ServiceLinkInJourneyPattern>\n" +
                "                <ServiceLinkInJourneyPattern order=\"11\" version=\"1\" id=\"VYG:ServiceLinkInJourneyPattern:24806935\">\n" +
                "                  <ServiceLinkRef ref=\"VYG:ServiceLink:ROM-1_NK-1_-860902437\"></ServiceLinkRef>\n" +
                "                </ServiceLinkInJourneyPattern>\n" +
                "              </linksInSequence>\n" +
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
        Line line = (Line) serviceFrame.getLines().getLine_().get(0).getValue();
        assertEquals("Narvik-Riksgränsen (Sverige)", line.getName().getValue());
        assertEquals("F8", line.getPublicCode());
        assertEquals("F8", line.getPrivateCode().getValue());
        assertEquals("rail", line.getTransportMode().value());

        Route route = (Route) serviceFrame.getRoutes().getRoute_().get(0).getValue();
        assertEquals("Riksgränsen-Narvik  Ofotbanen", route.getName().getValue());
        assertEquals("KJ-NK", route.getShortName().getValue());

        PointOnRoute pointOnRoute = route.getPointsInSequence().getPointOnRoute().get(0);
        assertEquals("VYG:RoutePoint:KJ", pointOnRoute.getPointRef().getValue().getRef());

        JourneyPattern journeyPattern = (JourneyPattern) serviceFrame.getJourneyPatterns().getJourneyPattern_OrJourneyPatternView().get(0).getValue();
        assertEquals("KMB-NK", journeyPattern.getName().getValue());
        assertEquals("VYG:Route:F8-R", journeyPattern.getRouteRef().getRef());

        StopPointInJourneyPattern stopPointInJourneyPattern = (StopPointInJourneyPattern) journeyPattern.getPointsInSequence().getPointInJourneyPatternOrStopPointInJourneyPatternOrTimingPointInJourneyPattern().get(0);
        assertEquals("VYG:ScheduledStopPoint:KMB-1", stopPointInJourneyPattern.getScheduledStopPointRef().getValue().getRef());
        assertFalse(stopPointInJourneyPattern.isForAlighting());
        assertEquals("VYG:DestinationDisplay:F8-NK", stopPointInJourneyPattern.getDestinationDisplayRef().getRef());

        ServiceLinkInJourneyPattern_VersionedChildStructure serviceLinkInJourneyPattern = (ServiceLinkInJourneyPattern_VersionedChildStructure) journeyPattern.getLinksInSequence().getServiceLinkInJourneyPatternOrTimingLinkInJourneyPattern().get(0);
        assertEquals("VYG:ServiceLink:KMB-1_ABOe-1_NULL",serviceLinkInJourneyPattern.getServiceLinkRef().getRef());


    }


}
