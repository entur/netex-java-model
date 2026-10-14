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
import java.time.LocalDate;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;

class UnmarshalServiceCalendarFrameTest extends AbstractUnmarshalFrameTest {

    @Test
    void unmarshalServiceCalendarFrame() throws JAXBException {

        String xml = """
            <?xml version="1.0" encoding="UTF-8"?>
            <PublicationDelivery xmlns="http://www.netex.org.uk/netex" xmlns:gis="http://www.opengis.net/gml/3.2" xmlns:siri="http://www.siri.org.uk/siri" version="1.13:NO-NeTEx-networktimetable:1.3">
              <PublicationTimestamp>2023-08-03T00:20:33.505</PublicationTimestamp>
              <ParticipantRef>RB</ParticipantRef>
              <Description>Shared data used across line files</Description>
              <dataObjects>
                <CompositeFrame created="2023-07-13T11:42:43.27" version="1" id="VYG:CompositeFrame:24806953">
                  <validityConditions>
                    <AvailabilityCondition version="1" id="VYG:AvailabilityCondition:24806949">
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
                    <ServiceCalendarFrame version="1" id="VYG:ServiceCalendarFrame:24806956">
                      <dayTypes>
                        <DayType version="1" id="BRA:DayType:356">
                          <properties>
                            <PropertyOfDay>
                              <DaysOfWeek>Tuesday Thursday Friday</DaysOfWeek>
                            </PropertyOfDay>
                          </properties>
                        </DayType>
                        <DayType version="1" id="BRA:DayType:355">
                          <properties>
                            <PropertyOfDay>
                              <DaysOfWeek>Tuesday Wednesday Thursday</DaysOfWeek>
                            </PropertyOfDay>
                          </properties>
                        </DayType>
                      </dayTypes>
                      <operatingDays>
                        <OperatingDay version="1" id="VYG:OperatingDay:2023-10-29">
                          <CalendarDate>2023-10-29</CalendarDate>
                        </OperatingDay>
                        <OperatingDay version="1" id="VYG:OperatingDay:2023-10-30">
                          <CalendarDate>2023-10-30</CalendarDate>
                        </OperatingDay>
                      </operatingDays>
                      <operatingPeriods>
                        <OperatingPeriod version="1" id="BRA:OperatingPeriod:355-1">
                          <FromDate>2023-08-17T00:00:00</FromDate>
                          <ToDate>2024-06-24T00:00:00</ToDate>
                        </OperatingPeriod>
                        <OperatingPeriod version="1" id="BRA:OperatingPeriod:311-1">
                          <FromDate>2023-08-16T00:00:00</FromDate>
                          <ToDate>2024-06-23T00:00:00</ToDate>
                        </OperatingPeriod>          </operatingPeriods>
                      <dayTypeAssignments>
                        <DayTypeAssignment order="1988" version="1" id="BRA:DayTypeAssignment:375-4">
                          <Date>2024-05-01</Date>
                          <DayTypeRef ref="BRA:DayType:375" version="1"></DayTypeRef>
                          <isAvailable>false</isAvailable>
                        </DayTypeAssignment>
                        <DayTypeAssignment order="1989" version="1" id="BRA:DayTypeAssignment:375-5">
                          <Date>2024-05-17</Date>
                          <DayTypeRef ref="BRA:DayType:375" version="1"></DayTypeRef>
                          <isAvailable>false</isAvailable>
                        </DayTypeAssignment>
                      </dayTypeAssignments>
                    </ServiceCalendarFrame>
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

        ServiceCalendarFrame serviceCalendarFrame = (ServiceCalendarFrame) compositeFrame.getFrames().getCommonFrame().getFirst().getValue();

        DayType dayType = (DayType) serviceCalendarFrame.getDayTypes().getDayType().getFirst().getValue();
        assertEquals(DayOfWeekEnumeration.TUESDAY, dayType.getProperties().getPropertyOfDay().getFirst().getDaysOfWeek().getFirst());

        OperatingDay operatingDay = serviceCalendarFrame.getOperatingDays().getOperatingDay().getFirst();
        assertEquals(LocalDate.of(2023, 10, 29), operatingDay.getCalendarDate());

        OperatingPeriod operatingPeriod = (OperatingPeriod) serviceCalendarFrame.getOperatingPeriods().getOperatingPeriodOrUicOperatingPeriod().getFirst();
        assertEquals(LocalDateTime.of(2023,8,17,0,0), operatingPeriod.getFromDate());

        DayTypeAssignment dayTypeAssignment = serviceCalendarFrame.getDayTypeAssignments().getDayTypeAssignment().getFirst();
        assertEquals(LocalDate.of(2024, 5, 1), dayTypeAssignment.getDate());


    }


}
