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

class UnmarshalTimetableFrameTest extends AbstractUnmarshalFrameTest {

    @Test
    void unmarshalTimetableFrame() throws JAXBException {

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
                    <TimetableFrame version="1" id="VYG:TimetableFrame:24806952">
                      <vehicleJourneys>          \s
                        <ServiceJourney version="11" id="VYG:ServiceJourney:96-KMB_87815-R">
                          <keyList>
                            <KeyValue>
                              <Key>ContentCheck</Key>
                              <Value>8357BD63</Value>
                            </KeyValue>
                          </keyList>
                          <Name>96</Name>
                          <PrivateCode>96</PrivateCode>
                          <TransportMode>rail</TransportMode>
                          <TransportSubmode>
                            <RailSubmode>regionalRail</RailSubmode>
                          </TransportSubmode>
                          <JourneyPatternRef ref="VYG:JourneyPattern:F8-1089" version="0"></JourneyPatternRef>
                          <OperatorRef ref="VYG:Operator:TAG"></OperatorRef>
                          <LineRef ref="VYG:Line:F8" version="3"></LineRef>
                          <passingTimes>
                            <TimetabledPassingTime version="0" id="VYG:TimetabledPassingTime:96-KMB_87815_1_KMB">
                              <StopPointInJourneyPatternRef ref="VYG:StopPointInJourneyPattern:F8-1089-1" version="0"></StopPointInJourneyPatternRef>
                              <DepartureTime>14:55:00</DepartureTime>
                            </TimetabledPassingTime>
                            <TimetabledPassingTime version="0" id="VYG:TimetabledPassingTime:96-KMB_87815_12_NK">
                              <StopPointInJourneyPatternRef ref="VYG:StopPointInJourneyPattern:F8-1089-12" version="0"></StopPointInJourneyPatternRef>
                              <ArrivalTime>17:43:00</ArrivalTime>
                            </TimetabledPassingTime>
                          </passingTimes>
                        </ServiceJourney>
                      \s
                        <DatedServiceJourney version="1" id="VYG:DatedServiceJourney:96_KMB-NK_23-12-09">
                          <ServiceAlteration>cancellation</ServiceAlteration>              <ServiceJourneyRef ref="VYG:ServiceJourney:96-KMB_87815-R" version="11"></ServiceJourneyRef>
                          <OperatingDayRef ref="VYG:OperatingDay:2023-12-09"/>
                          <replacedJourneys>                   <DatedVehicleJourneyRef ref="VYG:DatedServiceJourney:8916_KVG-DEG_23-10-19" version="1"/>
                          </replacedJourneys>
                        </DatedServiceJourney>
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
        ServiceJourney serviceJourney = (ServiceJourney) timetableFrame.getVehicleJourneys().getVehicleJourneyOrDatedVehicleJourneyOrNormalDatedVehicleJourney().get(0);
        assertEquals("96", getStringValue(serviceJourney.getName()));
        assertEquals("96", serviceJourney.getPrivateCode().getValue());
        assertEquals("rail", serviceJourney.getTransportMode().value());

        TimetabledPassingTime timetabledPassingTime = serviceJourney.getPassingTimes().getTimetabledPassingTime().get(0);
        assertEquals(LocalTime.of(14,55), timetabledPassingTime.getDepartureTime());

        DatedServiceJourney dsj = (DatedServiceJourney) timetableFrame.getVehicleJourneys().getVehicleJourneyOrDatedVehicleJourneyOrNormalDatedVehicleJourney().get(1);
        assertEquals("VYG:OperatingDay:2023-12-09", dsj.getOperatingDayRef().getRef());
        assertEquals("VYG:ServiceJourney:96-KMB_87815-R", dsj.getJourneyRef().getValue().getRef());
        assertEquals("VYG:DatedServiceJourney:8916_KVG-DEG_23-10-19", dsj.getReplacedJourneys().getDatedVehicleJourneyRefOrNormalDatedVehicleJourneyRef().get(0).getValue().getRef());
        assertEquals("cancellation", dsj.getServiceAlteration().value());




    }


}
