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
import static org.junit.jupiter.api.Assertions.assertNotNull;

class UnmarshalServiceFrameExtendedTest extends AbstractUnmarshalFrameTest {

    @Test
    void unmarshalNetworkWithAuthorityRef() throws JAXBException {

        String xml = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
                "<PublicationDelivery xmlns=\"http://www.netex.org.uk/netex\" version=\"1.13:NO-NeTEx-networktimetable:1.3\">\n" +
                "  <PublicationTimestamp>2023-08-03T00:20:33.337</PublicationTimestamp>\n" +
                "  <ParticipantRef>RB</ParticipantRef>\n" +
                "  <dataObjects>\n" +
                "    <CompositeFrame version=\"1\" id=\"TST:CompositeFrame:1\">\n" +
                "      <frames>\n" +
                "        <ServiceFrame version=\"1\" id=\"TST:ServiceFrame:1\">\n" +
                "          <Network version=\"1\" id=\"TST:Network:1\">\n" +
                "            <Name>Test Network</Name>\n" +
                "            <AuthorityRef ref=\"TST:Authority:1\"/>\n" +
                "          </Network>\n" +
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
        Network network = serviceFrame.getNetwork();
        assertNotNull(network);
        assertEquals("Test Network", getStringValue(network.getName()));
        assertEquals("TST:Authority:1", network.getTransportOrganisationRef().getValue().getRef());
    }

    @Test
    void unmarshalDestinationDisplayAndScheduledStopPoint() throws JAXBException {

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
                "              <FrontText>Oslo S</FrontText>\n" +
                "            </DestinationDisplay>\n" +
                "          </destinationDisplays>\n" +
                "          <scheduledStopPoints>\n" +
                "            <ScheduledStopPoint version=\"1\" id=\"TST:ScheduledStopPoint:1\">\n" +
                "              <Name>Jernbanetorget</Name>\n" +
                "            </ScheduledStopPoint>\n" +
                "            <ScheduledStopPoint version=\"1\" id=\"TST:ScheduledStopPoint:2\">\n" +
                "              <Name>Majorstuen</Name>\n" +
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

        DestinationDisplay destinationDisplay = serviceFrame.getDestinationDisplays().getDestinationDisplay().get(0);
        assertEquals("Oslo S", getStringValue(destinationDisplay.getFrontText()));

        assertEquals(2, serviceFrame.getScheduledStopPoints().getScheduledStopPoint().size());
        ScheduledStopPoint ssp1 = serviceFrame.getScheduledStopPoints().getScheduledStopPoint().get(0);
        assertEquals("Jernbanetorget", getStringValue(ssp1.getName()));
        ScheduledStopPoint ssp2 = serviceFrame.getScheduledStopPoints().getScheduledStopPoint().get(1);
        assertEquals("Majorstuen", getStringValue(ssp2.getName()));
    }

    @Test
    void unmarshalServiceLink() throws JAXBException {

        String xml = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
                "<PublicationDelivery xmlns=\"http://www.netex.org.uk/netex\" version=\"1.13:NO-NeTEx-networktimetable:1.3\">\n" +
                "  <PublicationTimestamp>2023-08-03T00:20:33.337</PublicationTimestamp>\n" +
                "  <ParticipantRef>RB</ParticipantRef>\n" +
                "  <dataObjects>\n" +
                "    <CompositeFrame version=\"1\" id=\"TST:CompositeFrame:1\">\n" +
                "      <frames>\n" +
                "        <ServiceFrame version=\"1\" id=\"TST:ServiceFrame:1\">\n" +
                "          <serviceLinks>\n" +
                "            <ServiceLink version=\"1\" id=\"TST:ServiceLink:1\">\n" +
                "              <Distance>1234.5</Distance>\n" +
                "              <FromPointRef ref=\"TST:ScheduledStopPoint:1\"/>\n" +
                "              <ToPointRef ref=\"TST:ScheduledStopPoint:2\"/>\n" +
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

        assertEquals(1, serviceFrame.getServiceLinks().getServiceLink().size());
        ServiceLink serviceLink = serviceFrame.getServiceLinks().getServiceLink().get(0);
        assertEquals("TST:ScheduledStopPoint:1", serviceLink.getFromPointRef().getRef());
        assertEquals("TST:ScheduledStopPoint:2", serviceLink.getToPointRef().getRef());
        assertEquals(new BigDecimal("1234.5"), serviceLink.getDistance());
    }

    @Test
    void unmarshalFlexibleLineAndGroupOfLines() throws JAXBException {

        String xml = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
                "<PublicationDelivery xmlns=\"http://www.netex.org.uk/netex\" version=\"1.13:NO-NeTEx-networktimetable:1.3\">\n" +
                "  <PublicationTimestamp>2023-08-03T00:20:33.337</PublicationTimestamp>\n" +
                "  <ParticipantRef>RB</ParticipantRef>\n" +
                "  <dataObjects>\n" +
                "    <CompositeFrame version=\"1\" id=\"TST:CompositeFrame:1\">\n" +
                "      <frames>\n" +
                "        <ServiceFrame version=\"1\" id=\"TST:ServiceFrame:1\">\n" +
                "          <lines>\n" +
                "            <FlexibleLine version=\"1\" id=\"TST:FlexibleLine:1\">\n" +
                "              <Name>Flex Route 1</Name>\n" +
                "              <TransportMode>bus</TransportMode>\n" +
                "              <FlexibleLineType>flexibleAreasOnly</FlexibleLineType>\n" +
                "            </FlexibleLine>\n" +
                "          </lines>\n" +
                "          <groupsOfLines>\n" +
                "            <GroupOfLines version=\"1\" id=\"TST:GroupOfLines:1\">\n" +
                "              <Name>Line Group A</Name>\n" +
                "              <members>\n" +
                "                <LineRef ref=\"TST:Line:1\"/>\n" +
                "                <LineRef ref=\"TST:Line:2\"/>\n" +
                "              </members>\n" +
                "            </GroupOfLines>\n" +
                "          </groupsOfLines>\n" +
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

        FlexibleLine flexibleLine = (FlexibleLine) serviceFrame.getLines().getLine_Dummy().get(0).getValue();
        assertEquals("Flex Route 1", getStringValue(flexibleLine.getName()));
        assertEquals(AllPublicTransportModesEnumeration.BUS, flexibleLine.getTransportMode());
        assertEquals(FlexibleLineTypeEnumeration.FLEXIBLE_AREAS_ONLY, flexibleLine.getFlexibleLineType());

        GroupOfLines groupOfLines = serviceFrame.getGroupsOfLines().getGroupOfLines().get(0);
        assertEquals("Line Group A", getStringValue(groupOfLines.getName()));
        assertEquals(2, groupOfLines.getMembers().getLineRef().size());
        assertEquals("TST:Line:1", groupOfLines.getMembers().getLineRef().get(0).getValue().getRef());
        assertEquals("TST:Line:2", groupOfLines.getMembers().getLineRef().get(1).getValue().getRef());
    }

    @Test
    void unmarshalNoticeAndNoticeAssignment() throws JAXBException {

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
                "              <Text>Does not stop at all stops</Text>\n" +
                "              <PublicCode>A</PublicCode>\n" +
                "            </Notice>\n" +
                "          </notices>\n" +
                "          <noticeAssignments>\n" +
                "            <NoticeAssignment version=\"1\" id=\"TST:NoticeAssignment:1\" order=\"1\">\n" +
                "              <NoticeRef ref=\"TST:Notice:1\"/>\n" +
                "              <NoticedObjectRef ref=\"TST:ServiceJourney:1\"/>\n" +
                "            </NoticeAssignment>\n" +
                "          </noticeAssignments>\n" +
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

        assertEquals(1, serviceFrame.getNotices().getNotice().size());
        Notice notice = serviceFrame.getNotices().getNotice().get(0);
        assertEquals("Does not stop at all stops", getStringValue(notice.getText()));
        assertEquals("A", notice.getPublicCode().getValue());

        NoticeAssignment noticeAssignment = (NoticeAssignment) serviceFrame.getNoticeAssignments().getNoticeAssignment_Dummy().get(0).getValue();
        assertEquals("TST:Notice:1", noticeAssignment.getNoticeRef().getRef());
        assertEquals("TST:ServiceJourney:1", noticeAssignment.getNoticedObjectRef().getRef());
    }
}
