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

class UnmarshalSiteFrameChouetteTest extends AbstractUnmarshalFrameTest {

    @Test
    void unmarshalStopPlaceWithDescriptionAndLandmark() throws JAXBException {

        String xml = "<?xml version=\"1.0\" encoding=\"utf-8\"?>"
                + "<PublicationDelivery version=\"1.0\" xmlns=\"http://www.netex.org.uk/netex\">"
                + " <PublicationTimestamp>2016-05-18T15:00:00.0+01:00</PublicationTimestamp>"
                + " <ParticipantRef>NHR</ParticipantRef>"
                + " <dataObjects>"
                + "  <SiteFrame version=\"01\" id=\"nhr:sf:1\">"
                + "   <stopPlaces>"
                + "    <StopPlace version=\"01\" id=\"TST:StopPlace:1\">"
                + "     <Name>Oslo S</Name>"
                + "     <Description>Main train station</Description>"
                + "     <Landmark>Central Station Clock Tower</Landmark>"
                + "     <TransportMode>rail</TransportMode>"
                + "     <PrivateCode>OSL001</PrivateCode>"
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
        assertEquals("Oslo S", getStringValue(stopPlace.getName()));
        assertEquals("Main train station", getStringValue(stopPlace.getDescription()));
        assertEquals("Central Station Clock Tower", getStringValue(stopPlace.getLandmark()));
        assertEquals(AllPublicTransportModesEnumeration.RAIL, stopPlace.getTransportMode());
        assertEquals("OSL001", stopPlace.getPrivateCode().getValue());
    }
}
