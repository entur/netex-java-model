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

package org.rutebanken.netex.validation;

import org.junit.jupiter.api.Test;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;

import javax.xml.transform.stream.StreamSource;
import java.io.IOException;
import java.io.StringReader;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertEquals;

class NeTExValidatorTest {

    private final NeTExValidator neTExValidator = NeTExValidator.getNeTExValidator();
    
    public NeTExValidatorTest() throws IOException, SAXException {    }

    public static final String xml = "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>\n" +
            "<PublicationDelivery xmlns=\"http://www.netex.org.uk/netex\" xmlns:ns2=\"http://www.opengis.net/gml/3.2\" xmlns:ns3=\"http://www.siri.org.uk/siri\" version=\"any\">\n" +
            "    <PublicationTimestamp>2016-11-29T13:32:06.869+01:00</PublicationTimestamp>\n" +
            "    <ParticipantRef>NSR</ParticipantRef>\n" +
            "</PublicationDelivery>";

    
    @Test
    void validationFailsForInvalidXml() {
        String xml = "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>\n" +
                "<PublicationDelivery xmlns=\"http://www.netex.org.uk/netex\" xmlns:ns2=\"http://www.opengis.net/gml/3.2\" xmlns:ns3=\"http://www.siri.org.uk/siri\"></PublicationDelivery>";

        assertThatThrownBy(() -> neTExValidator.validate(new StreamSource(new StringReader(xml)))).isInstanceOf(SAXParseException.class);
    }

    @Test
    void validatePublicationDeliveryWithLatestVersion() throws IOException, SAXException {
        neTExValidator.validate(new StreamSource(new StringReader(xml)));

    }

    /**
     * AssociatedContract / ContractRef on a ResponsibilityRoleAssignment was added in NeTEx 1.16.1
     * (entur/NeTEx#63) and is therefore valid against the latest schema but not against 1.16.
     */
    private static final String RESPONSIBILITY_SET_WITH_ASSOCIATED_CONTRACT = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
                "<PublicationDelivery xmlns=\"http://www.netex.org.uk/netex\" version=\"1.0\">\n" +
                "  <PublicationTimestamp>2026-09-10T10:00:00</PublicationTimestamp>\n" +
                "  <ParticipantRef>TST</ParticipantRef>\n" +
                "  <dataObjects>\n" +
                "    <ResourceFrame version=\"1\" id=\"TST:ResourceFrame:1\">\n" +
                "      <responsibilitySets>\n" +
                "        <ResponsibilitySet version=\"1\" id=\"TST:ResponsibilitySet:1\">\n" +
                "          <Name>Contracted operation</Name>\n" +
                "          <roles>\n" +
                "            <ResponsibilityRoleAssignment version=\"1\" id=\"TST:ResponsibilityRoleAssignment:1\">\n" +
                "              <AssociatedContract>\n" +
                "                <ContractRef ref=\"TST:Contract:1\" version=\"1\"/>\n" +
                "              </AssociatedContract>\n" +
                "            </ResponsibilityRoleAssignment>\n" +
                "          </roles>\n" +
                "        </ResponsibilitySet>\n" +
                "      </responsibilitySets>\n" +
                "    </ResourceFrame>\n" +
                "  </dataObjects>\n" +
                "</PublicationDelivery>";

    @Test
    void latestVersionIs1_16_1() {
        assertEquals(NeTExValidator.NetexVersion.v1_16_1, NeTExValidator.LATEST);
    }

    @Test
    void validateResponsibilitySetWithAssociatedContractAgainstLatestVersion() throws IOException, SAXException {
        neTExValidator.validate(new StreamSource(new StringReader(RESPONSIBILITY_SET_WITH_ASSOCIATED_CONTRACT)));
    }

    @Test
    void validationOfAssociatedContractFailsAgainstVersion1_16() throws IOException, SAXException {
        NeTExValidator validator1_16 = NeTExValidator.getNeTExValidator(NeTExValidator.NetexVersion.v1_16);
        assertThatThrownBy(() -> validator1_16.validate(new StreamSource(new StringReader(RESPONSIBILITY_SET_WITH_ASSOCIATED_CONTRACT))))
                .isInstanceOf(SAXParseException.class)
                .hasMessageContaining("AssociatedContract");
    }

}
