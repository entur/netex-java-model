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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class UnmarshalSiteFrameExtendedTest extends AbstractUnmarshalFrameTest {

    @Test
    void unmarshalStopPlaceWithAccessibilityAssessment() throws JAXBException {

        String xml = "<?xml version=\"1.0\" encoding=\"utf-8\"?>"
                + "<PublicationDelivery version=\"1.0\" xmlns=\"http://www.netex.org.uk/netex\">"
                + " <PublicationTimestamp>2016-05-18T15:00:00.0+01:00</PublicationTimestamp>"
                + " <ParticipantRef>NHR</ParticipantRef>"
                + " <dataObjects>"
                + "  <SiteFrame version=\"01\" id=\"nhr:sf:1\">"
                + "   <stopPlaces>"
                + "    <StopPlace version=\"01\" id=\"TST:StopPlace:1\">"
                + "     <Name>Accessible Stop</Name>"
                + "     <TransportMode>bus</TransportMode>"
                + "     <AccessibilityAssessment version=\"1\" id=\"TST:AccessibilityAssessment:1\">"
                + "      <MobilityImpairedAccess>true</MobilityImpairedAccess>"
                + "      <limitations>"
                + "       <AccessibilityLimitation>"
                + "        <WheelchairAccess>true</WheelchairAccess>"
                + "        <StepFreeAccess>true</StepFreeAccess>"
                + "       </AccessibilityLimitation>"
                + "      </limitations>"
                + "     </AccessibilityAssessment>"
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

        StopPlace stopPlace = siteFrame.getStopPlaces().getStopPlace().get(0);
        assertEquals("Accessible Stop", getStringValue(stopPlace.getName()));

        AccessibilityAssessment assessment = stopPlace.getAccessibilityAssessment();
        assertNotNull(assessment);
        assertEquals(LimitationStatusEnumeration.TRUE, assessment.getMobilityImpairedAccess());

        AccessibilityLimitation limitation = assessment.getLimitations().getAccessibilityLimitation();
        assertEquals(LimitationStatusEnumeration.TRUE, limitation.getWheelchairAccess());
        assertEquals(LimitationStatusEnumeration.TRUE, limitation.getStepFreeAccess());
    }

    @Test
    void unmarshalFlexibleStopPlace() throws JAXBException {

        String xml = "<?xml version=\"1.0\" encoding=\"utf-8\"?>"
                + "<PublicationDelivery version=\"1.0\" xmlns=\"http://www.netex.org.uk/netex\">"
                + " <PublicationTimestamp>2016-05-18T15:00:00.0+01:00</PublicationTimestamp>"
                + " <ParticipantRef>NHR</ParticipantRef>"
                + " <dataObjects>"
                + "  <SiteFrame version=\"01\" id=\"nhr:sf:1\">"
                + "   <flexibleStopPlaces>"
                + "    <FlexibleStopPlace version=\"1\" id=\"TST:FlexibleStopPlace:1\">"
                + "     <Name>Flexible Area North</Name>"
                + "     <TransportMode>bus</TransportMode>"
                + "    </FlexibleStopPlace>"
                + "   </flexibleStopPlaces>"
                + "  </SiteFrame>"
                + " </dataObjects>"
                + "</PublicationDelivery>";

        Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();

        @SuppressWarnings("unchecked")
        JAXBElement<PublicationDeliveryStructure> jaxbElement = (JAXBElement<PublicationDeliveryStructure>) unmarshaller
                .unmarshal(new ByteArrayInputStream(xml.getBytes()));

        PublicationDeliveryStructure publicationDeliveryStructure = jaxbElement.getValue();
        SiteFrame siteFrame = (SiteFrame) publicationDeliveryStructure.getDataObjects().getCompositeFrameOrCommonFrame().get(0).getValue();

        FlexibleStopPlace flexibleStopPlace = siteFrame.getFlexibleStopPlaces().getFlexibleStopPlace().get(0);
        assertEquals("Flexible Area North", getStringValue(flexibleStopPlace.getName()));
        assertEquals(AllPublicTransportModesEnumeration.BUS, flexibleStopPlace.getTransportMode());
    }

    @Test
    void unmarshalParking() throws JAXBException {

        String xml = "<?xml version=\"1.0\" encoding=\"utf-8\"?>"
                + "<PublicationDelivery version=\"1.0\" xmlns=\"http://www.netex.org.uk/netex\">"
                + " <PublicationTimestamp>2016-05-18T15:00:00.0+01:00</PublicationTimestamp>"
                + " <ParticipantRef>NHR</ParticipantRef>"
                + " <dataObjects>"
                + "  <SiteFrame version=\"01\" id=\"nhr:sf:1\">"
                + "   <parkings>"
                + "    <Parking version=\"1\" id=\"TST:Parking:1\">"
                + "     <Name>Central Parking</Name>"
                + "     <ParkingType>parkAndRide</ParkingType>"
                + "     <TotalCapacity>250</TotalCapacity>"
                + "    </Parking>"
                + "   </parkings>"
                + "  </SiteFrame>"
                + " </dataObjects>"
                + "</PublicationDelivery>";

        Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();

        @SuppressWarnings("unchecked")
        JAXBElement<PublicationDeliveryStructure> jaxbElement = (JAXBElement<PublicationDeliveryStructure>) unmarshaller
                .unmarshal(new ByteArrayInputStream(xml.getBytes()));

        PublicationDeliveryStructure publicationDeliveryStructure = jaxbElement.getValue();
        SiteFrame siteFrame = (SiteFrame) publicationDeliveryStructure.getDataObjects().getCompositeFrameOrCommonFrame().get(0).getValue();

        Parking parking = (Parking) siteFrame.getParkings().getParking_Dummy().get(0).getValue();
        assertEquals("Central Parking", getStringValue(parking.getName()));
        assertEquals(ParkingTypeEnumeration.PARK_AND_RIDE, parking.getParkingType());
        assertEquals(BigInteger.valueOf(250), parking.getTotalCapacity());
    }
}
