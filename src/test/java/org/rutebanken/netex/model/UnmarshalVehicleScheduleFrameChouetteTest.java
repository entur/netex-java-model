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
import java.math.BigInteger;
import java.time.LocalTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

class UnmarshalVehicleScheduleFrameChouetteTest extends AbstractUnmarshalFrameTest {

    @Test
    void unmarshalBlockInVehicleScheduleFrame() throws JAXBException {

        String xml = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
                "<PublicationDelivery xmlns=\"http://www.netex.org.uk/netex\" version=\"1.13:NO-NeTEx-networktimetable:1.3\">\n" +
                "  <PublicationTimestamp>2023-08-03T00:20:33.337</PublicationTimestamp>\n" +
                "  <ParticipantRef>RB</ParticipantRef>\n" +
                "  <dataObjects>\n" +
                "    <CompositeFrame version=\"1\" id=\"TST:CompositeFrame:1\">\n" +
                "      <frames>\n" +
                "        <VehicleScheduleFrame version=\"1\" id=\"TST:VehicleScheduleFrame:1\">\n" +
                "          <blocks>\n" +
                "            <Block version=\"1\" id=\"TST:Block:1\">\n" +
                "              <Name>Morning Block</Name>\n" +
                "              <Description>First block of the day</Description>\n" +
                "              <PrivateCode>BLK001</PrivateCode>\n" +
                "              <StartTime>05:30:00</StartTime>\n" +
                "              <EndTime>12:00:00</EndTime>\n" +
                "              <EndTimeDayOffset>0</EndTimeDayOffset>\n" +
                "              <StartPointRef ref=\"TST:ScheduledStopPoint:Depot\"/>\n" +
                "              <EndPointRef ref=\"TST:ScheduledStopPoint:Depot\"/>\n" +
                "              <dayTypes>\n" +
                "                <DayTypeRef ref=\"TST:DayType:Weekday\"/>\n" +
                "              </dayTypes>\n" +
                "              <journeys>\n" +
                "                <DeadRunRef ref=\"TST:DeadRun:ToStart\"/>\n" +
                "                <VehicleJourneyRef ref=\"TST:ServiceJourney:1\"/>\n" +
                "                <VehicleJourneyRef ref=\"TST:ServiceJourney:2\"/>\n" +
                "                <DeadRunRef ref=\"TST:DeadRun:ToDepot\"/>\n" +
                "              </journeys>\n" +
                "            </Block>\n" +
                "          </blocks>\n" +
                "        </VehicleScheduleFrame>\n" +
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
        VehicleScheduleFrame vsFrame = (VehicleScheduleFrame) compositeFrame.getFrames().getCommonFrame().get(0).getValue();

        assertNotNull(vsFrame.getBlocks());
        assertEquals(1, vsFrame.getBlocks().getBlockOrCompoundBlockOrTrainBlock().size());

        Block block = (Block) vsFrame.getBlocks().getBlockOrCompoundBlockOrTrainBlock().get(0);
        assertEquals("Morning Block", getStringValue(block.getName()));
        assertEquals("First block of the day", getStringValue(block.getDescription()));
        assertEquals("BLK001", block.getPrivateCode().getValue());
        assertEquals(LocalTime.of(5, 30), block.getStartTime());
        assertEquals(LocalTime.of(12, 0), block.getEndTime());
        assertEquals(BigInteger.ZERO, block.getEndTimeDayOffset());
        assertEquals("TST:ScheduledStopPoint:Depot", block.getStartPointRef().getRef());
        assertEquals("TST:ScheduledStopPoint:Depot", block.getEndPointRef().getRef());

        assertNotNull(block.getDayTypes());
        assertEquals("TST:DayType:Weekday", block.getDayTypes().getDayTypeRef().get(0).getValue().getRef());

        assertNotNull(block.getJourneys());
        assertEquals(4, block.getJourneys().getJourneyRefOrJourneyDesignatorOrServiceDesignator().size());

        Object firstRef = block.getJourneys().getJourneyRefOrJourneyDesignatorOrServiceDesignator().get(0).getValue();
        assertTrue(firstRef instanceof DeadRunRefStructure);
        assertEquals("TST:DeadRun:ToStart", ((DeadRunRefStructure) firstRef).getRef());

        Object secondRef = block.getJourneys().getJourneyRefOrJourneyDesignatorOrServiceDesignator().get(1).getValue();
        assertTrue(secondRef instanceof VehicleJourneyRefStructure);
        assertEquals("TST:ServiceJourney:1", ((VehicleJourneyRefStructure) secondRef).getRef());
    }
}
