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
import static org.junit.jupiter.api.Assertions.assertNotNull;

class UnmarshalResourceFrameExtendedTest extends AbstractUnmarshalFrameTest {

    @Test
    void unmarshalAuthorityWithContactDetails() throws JAXBException {

        String xml = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
                "<PublicationDelivery xmlns=\"http://www.netex.org.uk/netex\" version=\"1.13:NO-NeTEx-networktimetable:1.3\">\n" +
                "  <PublicationTimestamp>2023-08-03T00:20:33.505</PublicationTimestamp>\n" +
                "  <ParticipantRef>RB</ParticipantRef>\n" +
                "  <dataObjects>\n" +
                "    <CompositeFrame version=\"1\" id=\"TST:CompositeFrame:1\">\n" +
                "      <frames>\n" +
                "        <ResourceFrame version=\"1\" id=\"TST:ResourceFrame:1\">\n" +
                "          <organisations>\n" +
                "            <Authority version=\"1\" id=\"TST:Authority:1\">\n" +
                "              <Name>Test Authority</Name>\n" +
                "              <ContactDetails>\n" +
                "                <Phone>+47 99887766</Phone>\n" +
                "                <Url>https://www.testauthority.no</Url>\n" +
                "              </ContactDetails>\n" +
                "            </Authority>\n" +
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
        assertEquals("Test Authority", authority.getName().getValue());
        assertEquals("+47 99887766", authority.getContactDetails().getPhone());
        assertEquals("https://www.testauthority.no", authority.getContactDetails().getUrl());
    }

    @Test
    void unmarshalOperatorWithBrandingRef() throws JAXBException {

        String xml = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
                "<PublicationDelivery xmlns=\"http://www.netex.org.uk/netex\" version=\"1.13:NO-NeTEx-networktimetable:1.3\">\n" +
                "  <PublicationTimestamp>2023-08-03T00:20:33.505</PublicationTimestamp>\n" +
                "  <ParticipantRef>RB</ParticipantRef>\n" +
                "  <dataObjects>\n" +
                "    <CompositeFrame version=\"1\" id=\"TST:CompositeFrame:1\">\n" +
                "      <frames>\n" +
                "        <ResourceFrame version=\"1\" id=\"TST:ResourceFrame:1\">\n" +
                "          <typesOfValue>\n" +
                "            <Branding version=\"1\" id=\"TST:Branding:1\">\n" +
                "              <Name>Test Brand</Name>\n" +
                "              <Url>https://www.testbrand.no</Url>\n" +
                "              <Image>https://www.testbrand.no/logo.png</Image>\n" +
                "            </Branding>\n" +
                "          </typesOfValue>\n" +
                "          <organisations>\n" +
                "            <Operator version=\"1\" id=\"TST:Operator:1\">\n" +
                "              <BrandingRef ref=\"TST:Branding:1\"/>\n" +
                "              <Name>Test Operator</Name>\n" +
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

        Branding branding = (Branding) resourceFrame.getTypesOfValue().getValueSetOrTypeOfValue().get(0).getValue();
        assertEquals("Test Brand", branding.getName().getValue());
        assertEquals("https://www.testbrand.no", branding.getUrl());
        assertEquals("https://www.testbrand.no/logo.png", branding.getImage());

        Operator operator = (Operator) resourceFrame.getOrganisations().getOrganisation_().get(0).getValue();
        assertEquals("Test Operator", operator.getName().getValue());
        assertNotNull(operator.getBrandingRef());
        assertEquals("TST:Branding:1", operator.getBrandingRef().getRef());
    }
}
