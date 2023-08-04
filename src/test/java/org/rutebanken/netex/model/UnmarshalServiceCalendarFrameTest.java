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
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;

class UnmarshalServiceCalendarFrameTest extends AbstractUnmarshalFrameTest {

    @Test
    void unmarshalServiceCalendarFrame() throws JAXBException {

        String xml = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
                "<PublicationDelivery xmlns=\"http://www.netex.org.uk/netex\" xmlns:gis=\"http://www.opengis.net/gml/3.2\" xmlns:siri=\"http://www.siri.org.uk/siri\" version=\"1.13:NO-NeTEx-networktimetable:1.3\">\n" +
                "  <PublicationTimestamp>2023-08-03T00:20:33.505</PublicationTimestamp>\n" +
                "  <ParticipantRef>RB</ParticipantRef>\n" +
                "  <Description>Shared data used across line files</Description>\n" +
                "  <dataObjects>\n" +
                "    <CompositeFrame created=\"2023-07-13T11:42:43.27\" version=\"1\" id=\"VYG:CompositeFrame:24806953\">\n" +
                "      <validityConditions>\n" +
                "        <AvailabilityCondition version=\"1\" id=\"VYG:AvailabilityCondition:24806949\">\n" +
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
                "        <ServiceCalendarFrame version=\"1\" id=\"VYG:ServiceCalendarFrame:24806956\">\n" +
                "          <dayTypes>\n" +
                "            <DayType version=\"1\" id=\"BRA:DayType:356\">\n" +
                "              <properties>\n" +
                "                <PropertyOfDay>\n" +
                "                  <DaysOfWeek>Tuesday Thursday Friday</DaysOfWeek>\n" +
                "                </PropertyOfDay>\n" +
                "              </properties>\n" +
                "            </DayType>\n" +
                "            <DayType version=\"1\" id=\"BRA:DayType:355\">\n" +
                "              <properties>\n" +
                "                <PropertyOfDay>\n" +
                "                  <DaysOfWeek>Tuesday Wednesday Thursday</DaysOfWeek>\n" +
                "                </PropertyOfDay>\n" +
                "              </properties>\n" +
                "            </DayType>\n" +
                "          </dayTypes>\n" +
                "          <operatingDays>\n" +
                "            <OperatingDay version=\"1\" id=\"VYG:OperatingDay:2023-10-29\">\n" +
                "              <CalendarDate>2023-10-29</CalendarDate>\n" +
                "            </OperatingDay>\n" +
                "            <OperatingDay version=\"1\" id=\"VYG:OperatingDay:2023-10-30\">\n" +
                "              <CalendarDate>2023-10-30</CalendarDate>\n" +
                "            </OperatingDay>\n" +
                "          </operatingDays>\n" +
                "          <operatingPeriods>\n" +
                "            <OperatingPeriod version=\"1\" id=\"BRA:OperatingPeriod:355-1\">\n" +
                "              <FromDate>2023-08-17T00:00:00</FromDate>\n" +
                "              <ToDate>2024-06-24T00:00:00</ToDate>\n" +
                "            </OperatingPeriod>\n" +
                "            <OperatingPeriod version=\"1\" id=\"BRA:OperatingPeriod:311-1\">\n" +
                "              <FromDate>2023-08-16T00:00:00</FromDate>\n" +
                "              <ToDate>2024-06-23T00:00:00</ToDate>\n" +
                "            </OperatingPeriod>" +
                "          </operatingPeriods>\n" +
                "          <dayTypeAssignments>\n" +
                "            <DayTypeAssignment order=\"1988\" version=\"1\" id=\"BRA:DayTypeAssignment:375-4\">\n" +
                "              <Date>2024-05-01</Date>\n" +
                "              <DayTypeRef ref=\"BRA:DayType:375\" version=\"1\"></DayTypeRef>\n" +
                "              <isAvailable>false</isAvailable>\n" +
                "            </DayTypeAssignment>\n" +
                "            <DayTypeAssignment order=\"1989\" version=\"1\" id=\"BRA:DayTypeAssignment:375-5\">\n" +
                "              <Date>2024-05-17</Date>\n" +
                "              <DayTypeRef ref=\"BRA:DayType:375\" version=\"1\"></DayTypeRef>\n" +
                "              <isAvailable>false</isAvailable>\n" +
                "            </DayTypeAssignment>\n" +
                "          </dayTypeAssignments>\n" +
                "        </ServiceCalendarFrame>\n" +
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

        ServiceCalendarFrame serviceCalendarFrame = (ServiceCalendarFrame) compositeFrame.getFrames().getCommonFrame().get(0).getValue();

        DayType dayType = (DayType) serviceCalendarFrame.getDayTypes().getDayType_().get(0).getValue();
        assertEquals(DayOfWeekEnumeration.TUESDAY, dayType.getProperties().getPropertyOfDay().get(0).getDaysOfWeek().get(0));

        OperatingDay operatingDay = serviceCalendarFrame.getOperatingDays().getOperatingDay().get(0);
        assertEquals(LocalDateTime.of(2023,10,29,0,0), operatingDay.getCalendarDate());

        OperatingPeriod operatingPeriod = (OperatingPeriod) serviceCalendarFrame.getOperatingPeriods().getOperatingPeriodOrUicOperatingPeriod().get(0);
        assertEquals(LocalDateTime.of(2023,8,17,0,0), operatingPeriod.getFromDate());

        DayTypeAssignment dayTypeAssignment = serviceCalendarFrame.getDayTypeAssignments().getDayTypeAssignment().get(0);
        assertEquals(LocalDateTime.of(2024,5,1,0,0), dayTypeAssignment.getDate());


    }


}
