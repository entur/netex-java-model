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

class UnmarshalSiteFrameTest extends AbstractUnmarshalFrameTest{

    @Test
    void unmarshalSiteFrame() throws JAXBException {

        String xml = "<?xml version=\"1.0\" encoding=\"utf-8\"?>"
                + "<PublicationDelivery version=\"1.0\" xmlns=\"http://www.netex.org.uk/netex\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xsi:schemaLocation=\"http://www.netex.org.uk/netex ../../xsd/NeTEx_publication.xsd\">"
                + " <PublicationTimestamp>2016-05-18T15:00:00.0+01:00</PublicationTimestamp>"
                + " <ParticipantRef>NHR</ParticipantRef>"
                + " <dataObjects>"
                + "  <SiteFrame version=\"01\" id=\"nhr:sf:1\">"
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
        assertEquals("Krokstien", stopPlace.getName().getValue());
        assertEquals(VehicleModeEnumeration.BUS, stopPlace.getTransportMode());
        assertEquals(StopTypeEnumeration.ONSTREET_BUS, stopPlace.getStopPlaceType());

        Quay quay = (Quay) stopPlace.getQuays().getQuayRefOrQuay().get(0);
        assertEquals(BigDecimal.valueOf(59.910579), quay.getCentroid().getLocation().getLatitude());


    }


}
