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
import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;

class UnmarshalSiteFrameTest extends AbstractUnmarshalFrameTest {

    @Test
    void unmarshalSiteFrame() throws JAXBException {

        String xml = "<?xml version=\"1.0\" encoding=\"utf-8\"?>"
                + "<PublicationDelivery version=\"1.0\" xmlns=\"http://www.netex.org.uk/netex\" xmlns:ns2=\"http://www.opengis.net/gml/3.2\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xsi:schemaLocation=\"http://www.netex.org.uk/netex ../../xsd/NeTEx_publication.xsd\">"
                + " <PublicationTimestamp>2016-05-18T15:00:00.0+01:00</PublicationTimestamp>"
                + " <ParticipantRef>NHR</ParticipantRef>"
                + " <dataObjects>"
                + "  <SiteFrame version=\"01\" id=\"nhr:sf:1\">" +
                "      <groupsOfStopPlaces>\n" +
                "                <GroupOfStopPlaces created=\"2018-04-06T12:29:30.966\" modification=\"new\" version=\"1\" id=\"NSR:GroupOfStopPlaces:36\">\n" +
                "                    <Name lang=\"nor\">Lillehammer</Name>\n" +
                "                    <Description lang=\"nor\"></Description>\n" +
                "                    <members>\n" +
                "                        <StopPlaceRef ref=\"NSR:StopPlace:420\"/>\n" +
                "                        <StopPlaceRef ref=\"NSR:StopPlace:14115\"/>\n" +
                "                        <StopPlaceRef ref=\"NSR:StopPlace:14358\"/>\n" +
                "                        <StopPlaceRef ref=\"NSR:StopPlace:422\"/>\n" +
                "                    </members>\n" +
                "                    <Centroid>\n" +
                "                        <Location>\n" +
                "                            <Longitude>10.461604</Longitude>\n" +
                "                            <Latitude>61.114889</Latitude>\n" +
                "                        </Location>\n" +
                "                    </Centroid>\n" +
                "                </GroupOfStopPlaces>\n" +
                "                <GroupOfStopPlaces created=\"2018-04-06T12:44:35.508\" modification=\"new\" version=\"1\" id=\"NSR:GroupOfStopPlaces:45\">\n" +
                "                    <Name lang=\"nor\">Narvik</Name>\n" +
                "                    <Description lang=\"nor\"></Description>\n" +
                "                    <members>\n" +
                "                        <StopPlaceRef ref=\"NSR:StopPlace:47427\"/>\n" +
                "                        <StopPlaceRef ref=\"NSR:StopPlace:58234\"/>\n" +
                "                        <StopPlaceRef ref=\"NSR:StopPlace:49267\"/>\n" +
                "                    </members>\n" +
                "                    <Centroid>\n" +
                "                        <Location>\n" +
                "                            <Longitude>17.434792</Longitude>\n" +
                "                            <Latitude>68.440156</Latitude>\n" +
                "                        </Location>\n" +
                "                    </Centroid>\n" +
                "                </GroupOfStopPlaces>" +
                "      </groupsOfStopPlaces>\n"
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
                + "     <tariffZones>\n" +
                "         <TariffZoneRef ref=\"BRA:TariffZone:311\" version=\"6\"/>\n" +
                "         <TariffZoneRef ref=\"RUT:TariffZone:1\" version=\"8\"/>\n" +
                "       </tariffZones>"
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
                + "   </stopPlaces>" +
                "     <tariffZones>\n" +
                "                <TariffZone created=\"2020-12-11T10:05:19.737\" modification=\"new\" version=\"1\" id=\"OST:TariffZone:227\">\n" +
                "                    <ValidBetween>\n" +
                "                        <FromDate>2020-12-11T10:05:19.737</FromDate>\n" +
                "                    </ValidBetween>\n" +
                "                    <keyList>\n" +
                "                        <KeyValue>\n" +
                "                            <Key>zone_type</Key>\n" +
                "                            <Value>external</Value>\n" +
                "                        </KeyValue>\n" +
                "                        <KeyValue>\n" +
                "                            <Key>imported-id</Key>\n" +
                "                            <Value></Value>\n" +
                "                        </KeyValue>\n" +
                "                    </keyList>\n" +
                "                    <Name lang=\"nor\">1</Name>\n" +
                "                    <ns2:Polygon ns2:id=\"GEN-PolygonType-614620\">\n" +
                "                        <ns2:exterior>\n" +
                "                            <ns2:LinearRing>\n" +
                "<ns2:posList>59.82905206496 10.76591487408 59.84434061779 10.77278128624 59.85878172188 10.7820510006 59.87257035803 10.77767363548 59.88506137242 10.76522818565 59.89664364788 10.75205321789 59.90553214085 10.75819013357 59.9071460207 10.75720308065 59.90858768703 10.75040099859 59.91232066638 10.75396297216 59.91342867802 10.75784674644 59.91263263624 10.76381202221 59.90413339229 10.77188010693 59.88867858273 10.78677168846 59.86171241467 10.79784358978 59.82424195011 10.78273768902 59.82672258934 10.77484126568 59.82523422797 10.77406878948 59.82299077502 10.77501292706 59.82260246975 10.77170844555 59.8253205116 10.77042098522 59.82905206496 10.76591487408</ns2:posList>\n" +
                "                            </ns2:LinearRing>\n" +
                "                        </ns2:exterior>\n" +
                "                    </ns2:Polygon>\n" +
                "                </TariffZone>\n" +
                "                <TariffZone changed=\"2022-02-01T12:14:14.239\" modification=\"new\" version=\"6\" id=\"BRA:TariffZone:311\">\n" +
                "                    <ValidBetween>\n" +
                "                        <FromDate>2022-02-01T12:14:14.239</FromDate>\n" +
                "                    </ValidBetween>\n" +
                "                    <keyList>\n" +
                "                        <KeyValue>\n" +
                "                            <Key>zone_type</Key>\n" +
                "                            <Value>external</Value>\n" +
                "                        </KeyValue>\n" +
                "                    </keyList>\n" +
                "                    <Name lang=\"nor\">Oslo</Name>\n" +
                "                    <ns2:Polygon ns2:id=\"GEN-PolygonType-614621\">\n" +
                "                        <ns2:exterior>\n" +
                "                            <ns2:LinearRing>\n" +
                "<ns2:posList>59.91199795775 10.64158912182 59.91406329688 10.64070937872 59.91460113858 10.64007773233 59.9207425438 10.6681537199 59.92364614029 10.67858214855 59.92401176035 10.68300242901 59.92272131861 10.68866725445 59.91984989492 10.69587705374 59.91853778084 10.69527623892 59.91429994335 10.70132730246 59.91286928215 10.70604799032 59.91261111115 10.71173427343 59.91441826595 10.71802137136 59.91549390663 10.71857927084 59.91727939319 10.72229144812 59.92068876034 10.72806356192 59.91895723278 10.73186156988 59.91865608833 10.73209760427 59.91926912951 10.73379276037 59.91963479781 10.74001548529 59.91834418584 10.74057338476 59.91409556696 10.74834106207 59.91277246826 10.75859782934 59.91148158944 10.76353309393 59.9057688589 10.75793262005 59.90015198557 10.74398513317 59.90202438224 10.72875018597 59.9062853053 10.70458884716 59.90301434241 10.70518966198 59.89647147151 10.69398871422 59.8936515825 10.67596431255 59.89479249528 10.66935530663 59.90172308416 10.66115851879 59.90936229862 10.64764018536 59.91199795775 10.64158912182</ns2:posList>\n" +
                "                            </ns2:LinearRing>\n" +
                "                        </ns2:exterior>\n" +
                "                    </ns2:Polygon>\n" +
                "                </TariffZone>\n" +
                "              </tariffZones>\n"
                + "  </SiteFrame>"
                + " </dataObjects>"
                + "</PublicationDelivery>";

        Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();

        @SuppressWarnings("unchecked")
        JAXBElement<PublicationDeliveryStructure> jaxbElement = (JAXBElement<PublicationDeliveryStructure>) unmarshaller
                .unmarshal(new ByteArrayInputStream(xml.getBytes()));

        PublicationDeliveryStructure publicationDeliveryStructure = jaxbElement.getValue();
        SiteFrame siteFrame = (SiteFrame) publicationDeliveryStructure.getDataObjects().getCompositeFrameOrCommonFrame().get(0).getValue();

        StopPlace stopPlace = (StopPlace) siteFrame.getStopPlaces().getStopPlace_().get(0).getValue();
        assertEquals("Krokstien", stopPlace.getName().getValue());
        assertEquals(AllVehicleModesOfTransportEnumeration.BUS, stopPlace.getTransportMode());
        assertEquals(StopTypeEnumeration.ONSTREET_BUS, stopPlace.getStopPlaceType());
        assertEquals("BRA:TariffZone:311", stopPlace.getTariffZones().getTariffZoneRef_().get(0).getValue().getRef());

        Quay quay = (Quay) stopPlace.getQuays().getQuayRefOrQuay().get(0).getValue();
        assertEquals(BigDecimal.valueOf(59.910579), quay.getCentroid().getLocation().getLatitude());
        TariffZone tariffZone = (TariffZone) siteFrame.getTariffZones().getTariffZone().get(0).getValue();
        assertEquals("1", tariffZone.getName().getValue());

        GroupOfStopPlaces groupOfStopPlaces = siteFrame.getGroupsOfStopPlaces().getGroupOfStopPlaces().get(0);
        assertEquals("Lillehammer", groupOfStopPlaces.getName().getValue());
        assertEquals("NSR:StopPlace:420", groupOfStopPlaces.getMembers().getStopPlaceRef().get(0).getValue().getRef());



    }


}
