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

import org.junit.jupiter.api.Test;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import java.io.ByteArrayInputStream;

import static org.junit.jupiter.api.Assertions.assertEquals;

class UnmarshalResourceFrameTest extends  AbstractUnmarshalFrameTest {

    @Test
    void unmarshalResourceFrame() throws JAXBException {

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
                "        <ResourceFrame version=\"1\" id=\"VYG:ResourceFrame:24806954\">\n" +
                "          <organisations>\n" +
                "            <Authority version=\"1\" id=\"VYG:Authority:VYT\">\n" +
                "              <Name>Vy</Name>\n" +
                "              <ContactDetails>\n" +
                "                <Url>https://www.vy.no/</Url>\n" +
                "              </ContactDetails>\n" +
                "              <OrganisationType>authority</OrganisationType>\n" +
                "            </Authority>\n" +
                "            <Operator version=\"1\" id=\"VYG:Operator:TAG\">\n" +
                "              <Name>Vy Tåg</Name>\n" +
                "              <OrganisationType>operator</OrganisationType>\n" +
                "              <CustomerServiceContactDetails>\n" +
                "                <Url>https://www.vy.no/</Url>\n" +
                "              </CustomerServiceContactDetails>\n" +
                "            </Operator>\n" +
                "            <Authority version=\"1\" id=\"VYG:Authority:TAG\">\n" +
                "              <Name>Vy Tåg</Name>\n" +
                "              <ContactDetails>\n" +
                "                <Url>https://www.vy.no/</Url>\n" +
                "              </ContactDetails>\n" +
                "              <OrganisationType>authority</OrganisationType>\n" +
                "            </Authority>\n" +
                "            <Operator version=\"1\" id=\"VYG:Operator:VYT\">\n" +
                "              <Name>Vy</Name>\n" +
                "              <OrganisationType>operator</OrganisationType>\n" +
                "              <CustomerServiceContactDetails>\n" +
                "                <Url>https://www.vy.no/</Url>\n" +
                "              </CustomerServiceContactDetails>\n" +
                "            </Operator>\n" +
                "          </organisations>\n" +
                "        </ResourceFrame>\n" +
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

        ResourceFrame resourceFrame = (ResourceFrame) compositeFrame.getFrames().getCommonFrame().get(0).getValue();
        Authority authority = (Authority) resourceFrame.getOrganisations().getOrganisation_().get(0).getValue();
        assertEquals("Vy", authority.getName().getValue());



    }


}
