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
import static org.junit.jupiter.api.Assertions.assertNotNull;

class UnmarshalResourceFrameExtendedTest extends AbstractUnmarshalFrameTest {

    @Test
    void unmarshalAuthorityWithContactDetails() throws JAXBException {

        String xml = """
            <?xml version="1.0" encoding="UTF-8"?>
            <PublicationDelivery xmlns="http://www.netex.org.uk/netex" version="1.13:NO-NeTEx-networktimetable:1.3">
              <PublicationTimestamp>2023-08-03T00:20:33.505</PublicationTimestamp>
              <ParticipantRef>RB</ParticipantRef>
              <dataObjects>
                <CompositeFrame version="1" id="TST:CompositeFrame:1">
                  <frames>
                    <ResourceFrame version="1" id="TST:ResourceFrame:1">
                      <organisations>
                        <Authority version="1" id="TST:Authority:1">
                          <Name>Test Authority</Name>
                          <ContactDetails>
                            <Phone>+47 99887766</Phone>
                            <Url>https://www.testauthority.no</Url>
                          </ContactDetails>
                        </Authority>
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
        CompositeFrame compositeFrame = (CompositeFrame) publicationDeliveryStructure.getDataObjects().getCompositeFrameOrCommonFrame().getFirst().getValue();

        ResourceFrame resourceFrame = (ResourceFrame) compositeFrame.getFrames().getCommonFrame().getFirst().getValue();

        Authority authority = (Authority) resourceFrame.getOrganisations().getOrganisation().getFirst().getValue();
        assertEquals("Test Authority", getStringValue(authority.getName()));
        assertEquals("+47 99887766", authority.getContactDetails().getPhone());
        assertEquals("https://www.testauthority.no", authority.getContactDetails().getUrl());
    }

    @Test
    void unmarshalOperatorWithBrandingRef() throws JAXBException {

        String xml = """
            <?xml version="1.0" encoding="UTF-8"?>
            <PublicationDelivery xmlns="http://www.netex.org.uk/netex" version="1.13:NO-NeTEx-networktimetable:1.3">
              <PublicationTimestamp>2023-08-03T00:20:33.505</PublicationTimestamp>
              <ParticipantRef>RB</ParticipantRef>
              <dataObjects>
                <CompositeFrame version="1" id="TST:CompositeFrame:1">
                  <frames>
                    <ResourceFrame version="1" id="TST:ResourceFrame:1">
                      <typesOfValue>
                        <Branding version="1" id="TST:Branding:1">
                          <Name>Test Brand</Name>
                          <Url>https://www.testbrand.no</Url>
                          <Image>https://www.testbrand.no/logo.png</Image>
                        </Branding>
                      </typesOfValue>
                      <organisations>
                        <Operator version="1" id="TST:Operator:1">
                          <BrandingRef ref="TST:Branding:1"/>
                          <Name>Test Operator</Name>
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
        CompositeFrame compositeFrame = (CompositeFrame) publicationDeliveryStructure.getDataObjects().getCompositeFrameOrCommonFrame().getFirst().getValue();

        ResourceFrame resourceFrame = (ResourceFrame) compositeFrame.getFrames().getCommonFrame().getFirst().getValue();

        Branding branding = (Branding) resourceFrame.getTypesOfValue().getValueSetOrTypeOfValue().getFirst().getValue();
        assertEquals("Test Brand", getStringValue(branding.getName()));
        assertEquals("https://www.testbrand.no", branding.getUrl());
        assertEquals("https://www.testbrand.no/logo.png", branding.getImage());

        Operator operator = (Operator) resourceFrame.getOrganisations().getOrganisation().getFirst().getValue();
        assertEquals("Test Operator", getStringValue(operator.getName()));
        assertNotNull(operator.getBrandingRef());
        assertEquals("TST:Branding:1", operator.getBrandingRef().getRef());
    }

    @Test
    void unmarshalResponsibilitySetWithAssociatedContract() throws JAXBException {

        String xml = """
            <?xml version="1.0" encoding="UTF-8"?>
            <PublicationDelivery xmlns="http://www.netex.org.uk/netex" version="1.0">
              <PublicationTimestamp>2026-09-10T10:00:00</PublicationTimestamp>
              <ParticipantRef>TST</ParticipantRef>
              <dataObjects>
                <ResourceFrame version="1" id="TST:ResourceFrame:1">
                  <responsibilitySets>
                    <ResponsibilitySet version="1" id="TST:ResponsibilitySet:1">
                      <Name>Contracted operation</Name>
                      <roles>
                        <ResponsibilityRoleAssignment version="1" id="TST:ResponsibilityRoleAssignment:1">
                          <AssociatedContract>
                            <ContractRef ref="TST:Contract:1" version="1"/>
                          </AssociatedContract>
                        </ResponsibilityRoleAssignment>
                      </roles>
                    </ResponsibilitySet>
                  </responsibilitySets>
                </ResourceFrame>
              </dataObjects>
            </PublicationDelivery>""";

        Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();

        @SuppressWarnings("unchecked")
        JAXBElement<PublicationDeliveryStructure> jaxbElement = (JAXBElement<PublicationDeliveryStructure>) unmarshaller
                .unmarshal(new ByteArrayInputStream(xml.getBytes()));

        PublicationDeliveryStructure publicationDeliveryStructure = jaxbElement.getValue();
        ResourceFrame resourceFrame = (ResourceFrame) publicationDeliveryStructure.getDataObjects().getCompositeFrameOrCommonFrame().getFirst().getValue();

        ResponsibilitySet responsibilitySet = resourceFrame.getResponsibilitySets().getResponsibilitySet().getFirst();
        assertEquals("Contracted operation", getStringValue(responsibilitySet.getName()));

        ResponsibilityRoleAssignment_VersionedChildStructure roleAssignment = responsibilitySet.getRoles().getResponsibilityRoleAssignment().getFirst();

        ContractRef_RelStructure associatedContract = roleAssignment.getAssociatedContract();
        assertNotNull(associatedContract);
        ContractRefStructure contractRef = associatedContract.getContractRef().getValue();
        assertNotNull(contractRef);
        assertEquals("TST:Contract:1", contractRef.getRef());
        assertEquals("1", contractRef.getVersion());
    }
}
