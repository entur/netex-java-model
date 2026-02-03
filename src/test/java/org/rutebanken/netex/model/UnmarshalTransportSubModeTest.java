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

class UnmarshalTransportSubModeTest extends AbstractUnmarshalFrameTest {

    @Test
    void unmarshalStopPlaceWithBusSubmode() throws JAXBException {

        String xml = "<?xml version=\"1.0\" encoding=\"utf-8\"?>"
                + "<PublicationDelivery version=\"1.0\" xmlns=\"http://www.netex.org.uk/netex\">"
                + " <PublicationTimestamp>2016-05-18T15:00:00.0+01:00</PublicationTimestamp>"
                + " <ParticipantRef>NHR</ParticipantRef>"
                + " <dataObjects>"
                + "  <SiteFrame version=\"01\" id=\"nhr:sf:1\">"
                + "   <stopPlaces>"
                + "    <StopPlace version=\"1\" id=\"TST:StopPlace:Bus1\">"
                + "     <Name>Bus Stop</Name>"
                + "     <TransportMode>bus</TransportMode>"
                + "     <BusSubmode>expressBus</BusSubmode>"
                + "    </StopPlace>"
                + "    <StopPlace version=\"1\" id=\"TST:StopPlace:Rail1\">"
                + "     <Name>Rail Station</Name>"
                + "     <TransportMode>rail</TransportMode>"
                + "     <RailSubmode>regionalRail</RailSubmode>"
                + "    </StopPlace>"
                + "    <StopPlace version=\"1\" id=\"TST:StopPlace:Water1\">"
                + "     <Name>Ferry Terminal</Name>"
                + "     <TransportMode>water</TransportMode>"
                + "     <WaterSubmode>localCarFerry</WaterSubmode>"
                + "    </StopPlace>"
                + "    <StopPlace version=\"1\" id=\"TST:StopPlace:Tram1\">"
                + "     <Name>Tram Stop</Name>"
                + "     <TransportMode>tram</TransportMode>"
                + "     <TramSubmode>cityTram</TramSubmode>"
                + "    </StopPlace>"
                + "    <StopPlace version=\"1\" id=\"TST:StopPlace:Metro1\">"
                + "     <Name>Metro Station</Name>"
                + "     <TransportMode>metro</TransportMode>"
                + "     <MetroSubmode>metro</MetroSubmode>"
                + "    </StopPlace>"
                + "   </stopPlaces>"
                + "  </SiteFrame>"
                + " </dataObjects>"
                + "</PublicationDelivery>";

        Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();

        @SuppressWarnings("unchecked")
        JAXBElement<PublicationDeliveryStructure> jaxbElement = (JAXBElement<PublicationDeliveryStructure>) unmarshaller
                .unmarshal(new ByteArrayInputStream(xml.getBytes()));

        PublicationDeliveryStructure publicationDeliveryStructure = jaxbElement.getValue();
        SiteFrame siteFrame = (SiteFrame) publicationDeliveryStructure.getDataObjects().getCompositeFrameOrCommonFrame().get(0).getValue();

        StopPlace busStop = siteFrame.getStopPlaces().getStopPlace().get(0);
        assertEquals(AllPublicTransportModesEnumeration.BUS, busStop.getTransportMode());
        assertEquals(BusSubmodeEnumeration.EXPRESS_BUS, busStop.getBusSubmode());
        assertEquals("expressBus", busStop.getBusSubmode().value());

        StopPlace railStop = siteFrame.getStopPlaces().getStopPlace().get(1);
        assertEquals(AllPublicTransportModesEnumeration.RAIL, railStop.getTransportMode());
        assertEquals(RailSubmodeEnumeration.REGIONAL_RAIL, railStop.getRailSubmode());
        assertEquals("regionalRail", railStop.getRailSubmode().value());

        StopPlace waterStop = siteFrame.getStopPlaces().getStopPlace().get(2);
        assertEquals(AllPublicTransportModesEnumeration.WATER, waterStop.getTransportMode());
        assertEquals(WaterSubmodeEnumeration.LOCAL_CAR_FERRY, waterStop.getWaterSubmode());
        assertEquals("localCarFerry", waterStop.getWaterSubmode().value());

        StopPlace tramStop = siteFrame.getStopPlaces().getStopPlace().get(3);
        assertEquals(AllPublicTransportModesEnumeration.TRAM, tramStop.getTransportMode());
        assertEquals(TramSubmodeEnumeration.CITY_TRAM, tramStop.getTramSubmode());
        assertEquals("cityTram", tramStop.getTramSubmode().value());

        StopPlace metroStop = siteFrame.getStopPlaces().getStopPlace().get(4);
        assertEquals(AllPublicTransportModesEnumeration.METRO, metroStop.getTransportMode());
        assertEquals(MetroSubmodeEnumeration.METRO, metroStop.getMetroSubmode());
        assertEquals("metro", metroStop.getMetroSubmode().value());
    }

    @Test
    void unmarshalServiceJourneyWithTransportSubmode() throws JAXBException {

        String xml = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
                "<PublicationDelivery xmlns=\"http://www.netex.org.uk/netex\" version=\"1.13:NO-NeTEx-networktimetable:1.3\">\n" +
                "  <PublicationTimestamp>2023-08-03T00:20:33.337</PublicationTimestamp>\n" +
                "  <ParticipantRef>RB</ParticipantRef>\n" +
                "  <dataObjects>\n" +
                "    <CompositeFrame version=\"1\" id=\"TST:CompositeFrame:1\">\n" +
                "      <frames>\n" +
                "        <TimetableFrame version=\"1\" id=\"TST:TimetableFrame:1\">\n" +
                "          <vehicleJourneys>\n" +
                "            <ServiceJourney version=\"1\" id=\"TST:ServiceJourney:1\">\n" +
                "              <Name>Express Bus 100</Name>\n" +
                "              <TransportMode>bus</TransportMode>\n" +
                "              <TransportSubmode>\n" +
                "                <BusSubmode>expressBus</BusSubmode>\n" +
                "              </TransportSubmode>\n" +
                "            </ServiceJourney>\n" +
                "            <ServiceJourney version=\"1\" id=\"TST:ServiceJourney:2\">\n" +
                "              <Name>Regional Rail 200</Name>\n" +
                "              <TransportMode>rail</TransportMode>\n" +
                "              <TransportSubmode>\n" +
                "                <RailSubmode>regionalRail</RailSubmode>\n" +
                "              </TransportSubmode>\n" +
                "            </ServiceJourney>\n" +
                "            <ServiceJourney version=\"1\" id=\"TST:ServiceJourney:3\">\n" +
                "              <Name>Local Ferry</Name>\n" +
                "              <TransportMode>water</TransportMode>\n" +
                "              <TransportSubmode>\n" +
                "                <WaterSubmode>localPassengerFerry</WaterSubmode>\n" +
                "              </TransportSubmode>\n" +
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

        ServiceJourney busSj = (ServiceJourney) timetableFrame.getVehicleJourneys()
                .getVehicleJourneyOrDatedVehicleJourneyOrNormalDatedVehicleJourney().get(0);
        assertEquals(AllPublicTransportModesEnumeration.BUS, busSj.getTransportMode());
        assertNotNull(busSj.getTransportSubmode());
        assertEquals(BusSubmodeEnumeration.EXPRESS_BUS, busSj.getTransportSubmode().getBusSubmode());
        assertEquals("expressBus", busSj.getTransportSubmode().getBusSubmode().value());

        ServiceJourney railSj = (ServiceJourney) timetableFrame.getVehicleJourneys()
                .getVehicleJourneyOrDatedVehicleJourneyOrNormalDatedVehicleJourney().get(1);
        assertEquals(AllPublicTransportModesEnumeration.RAIL, railSj.getTransportMode());
        assertNotNull(railSj.getTransportSubmode());
        assertEquals(RailSubmodeEnumeration.REGIONAL_RAIL, railSj.getTransportSubmode().getRailSubmode());
        assertEquals("regionalRail", railSj.getTransportSubmode().getRailSubmode().value());

        ServiceJourney waterSj = (ServiceJourney) timetableFrame.getVehicleJourneys()
                .getVehicleJourneyOrDatedVehicleJourneyOrNormalDatedVehicleJourney().get(2);
        assertEquals(AllPublicTransportModesEnumeration.WATER, waterSj.getTransportMode());
        assertNotNull(waterSj.getTransportSubmode());
        assertEquals(WaterSubmodeEnumeration.LOCAL_PASSENGER_FERRY, waterSj.getTransportSubmode().getWaterSubmode());
        assertEquals("localPassengerFerry", waterSj.getTransportSubmode().getWaterSubmode().value());
    }

    @Test
    void unmarshalRouteWithLineRef() throws JAXBException {

        String xml = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
                "<PublicationDelivery xmlns=\"http://www.netex.org.uk/netex\" version=\"1.13:NO-NeTEx-networktimetable:1.3\">\n" +
                "  <PublicationTimestamp>2023-08-03T00:20:33.337</PublicationTimestamp>\n" +
                "  <ParticipantRef>RB</ParticipantRef>\n" +
                "  <dataObjects>\n" +
                "    <CompositeFrame version=\"1\" id=\"TST:CompositeFrame:1\">\n" +
                "      <frames>\n" +
                "        <ServiceFrame version=\"1\" id=\"TST:ServiceFrame:1\">\n" +
                "          <routes>\n" +
                "            <Route version=\"1\" id=\"TST:Route:1\">\n" +
                "              <Name>Main Route</Name>\n" +
                "              <LineRef ref=\"TST:Line:100\" version=\"1\"/>\n" +
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

        Route route = (Route) serviceFrame.getRoutes().getRoute_Dummy().get(0).getValue();
        assertEquals("Main Route", getStringValue(route.getName()));
        assertNotNull(route.getLineRef());
        assertEquals("TST:Line:100", route.getLineRef().getValue().getRef());
    }

    @Test
    void unmarshalStopPlaceWithCoachFunicularAndTelecabinSubmodes() throws JAXBException {

        String xml = "<?xml version=\"1.0\" encoding=\"utf-8\"?>"
                + "<PublicationDelivery version=\"1.0\" xmlns=\"http://www.netex.org.uk/netex\">"
                + " <PublicationTimestamp>2016-05-18T15:00:00.0+01:00</PublicationTimestamp>"
                + " <ParticipantRef>NHR</ParticipantRef>"
                + " <dataObjects>"
                + "  <SiteFrame version=\"01\" id=\"nhr:sf:1\">"
                + "   <stopPlaces>"
                + "    <StopPlace version=\"1\" id=\"TST:StopPlace:Coach1\">"
                + "     <Name>Coach Terminal</Name>"
                + "     <TransportMode>coach</TransportMode>"
                + "     <CoachSubmode>internationalCoach</CoachSubmode>"
                + "    </StopPlace>"
                + "    <StopPlace version=\"1\" id=\"TST:StopPlace:Funicular1\">"
                + "     <Name>Funicular Station</Name>"
                + "     <TransportMode>funicular</TransportMode>"
                + "     <FunicularSubmode>funicular</FunicularSubmode>"
                + "    </StopPlace>"
                + "    <StopPlace version=\"1\" id=\"TST:StopPlace:Telecabin1\">"
                + "     <Name>Cable Car Station</Name>"
                + "     <TransportMode>cableway</TransportMode>"
                + "     <TelecabinSubmode>telecabin</TelecabinSubmode>"
                + "    </StopPlace>"
                + "    <StopPlace version=\"1\" id=\"TST:StopPlace:Air1\">"
                + "     <Name>Airport</Name>"
                + "     <TransportMode>air</TransportMode>"
                + "     <AirSubmode>domesticFlight</AirSubmode>"
                + "    </StopPlace>"
                + "   </stopPlaces>"
                + "  </SiteFrame>"
                + " </dataObjects>"
                + "</PublicationDelivery>";

        Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();

        @SuppressWarnings("unchecked")
        JAXBElement<PublicationDeliveryStructure> jaxbElement = (JAXBElement<PublicationDeliveryStructure>) unmarshaller
                .unmarshal(new ByteArrayInputStream(xml.getBytes()));

        PublicationDeliveryStructure publicationDeliveryStructure = jaxbElement.getValue();
        SiteFrame siteFrame = (SiteFrame) publicationDeliveryStructure.getDataObjects().getCompositeFrameOrCommonFrame().get(0).getValue();

        StopPlace coachStop = siteFrame.getStopPlaces().getStopPlace().get(0);
        assertEquals(AllPublicTransportModesEnumeration.COACH, coachStop.getTransportMode());
        assertEquals(CoachSubmodeEnumeration.INTERNATIONAL_COACH, coachStop.getCoachSubmode());
        assertEquals("internationalCoach", coachStop.getCoachSubmode().value());

        StopPlace funicularStop = siteFrame.getStopPlaces().getStopPlace().get(1);
        assertEquals(AllPublicTransportModesEnumeration.FUNICULAR, funicularStop.getTransportMode());
        assertEquals(FunicularSubmodeEnumeration.FUNICULAR, funicularStop.getFunicularSubmode());
        assertEquals("funicular", funicularStop.getFunicularSubmode().value());

        StopPlace telecabinStop = siteFrame.getStopPlaces().getStopPlace().get(2);
        assertEquals(AllPublicTransportModesEnumeration.CABLEWAY, telecabinStop.getTransportMode());
        assertEquals(TelecabinSubmodeEnumeration.TELECABIN, telecabinStop.getTelecabinSubmode());
        assertEquals("telecabin", telecabinStop.getTelecabinSubmode().value());

        StopPlace airStop = siteFrame.getStopPlaces().getStopPlace().get(3);
        assertEquals(AllPublicTransportModesEnumeration.AIR, airStop.getTransportMode());
        assertEquals(AirSubmodeEnumeration.DOMESTIC_FLIGHT, airStop.getAirSubmode());
        assertEquals("domesticFlight", airStop.getAirSubmode().value());
    }
}
