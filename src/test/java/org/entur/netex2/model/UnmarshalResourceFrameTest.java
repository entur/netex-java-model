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

import static org.junit.jupiter.api.Assertions.assertEquals;

class UnmarshalResourceFrameTest extends  AbstractUnmarshalFrameTest {

    @Test
    void unmarshalResourceFrame() throws JAXBException {

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
                    <ResourceFrame version="1" id="VYG:ResourceFrame:24806954">
                      <organisations>
                        <Authority version="1" id="VYG:Authority:VYT">
                          <Name>Vy</Name>
                          <ContactDetails>
                            <Url>https://www.vy.no/</Url>
                          </ContactDetails>
                          <OrganisationType>authority</OrganisationType>
                        </Authority>
                        <Operator version="1" id="VYG:Operator:TAG">
                          <Name>Vy Tåg</Name>
                          <OrganisationType>operator</OrganisationType>
                          <CustomerServiceContactDetails>
                            <Url>https://www.vy.no/</Url>
                          </CustomerServiceContactDetails>
                        </Operator>
                        <Authority version="1" id="VYG:Authority:TAG">
                          <Name>Vy Tåg</Name>
                          <ContactDetails>
                            <Url>https://www.vy.no/</Url>
                          </ContactDetails>
                          <OrganisationType>authority</OrganisationType>
                        </Authority>
                        <Operator version="1" id="VYG:Operator:VYT">
                          <Name>Vy</Name>
                          <OrganisationType>operator</OrganisationType>
                          <CustomerServiceContactDetails>
                            <Url>https://www.vy.no/</Url>
                          </CustomerServiceContactDetails>
                        </Operator>
                      </organisations>
                    </ResourceFrame>
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

        ResourceFrame resourceFrame = (ResourceFrame) compositeFrame.getFrames().getCommonFrame().get(0).getValue();
        Authority authority = (Authority) resourceFrame.getOrganisations().getOrganisation_Dummy().get(0).getValue();
        assertEquals("Vy", getStringValue(authority.getName()));



    }


}
