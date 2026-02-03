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

import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.JAXBException;
import org.junit.jupiter.api.BeforeAll;

abstract class AbstractUnmarshalFrameTest {

    protected static JAXBContext jaxbContext;


    @BeforeAll
    public static void initContext() throws JAXBException {
        jaxbContext = JAXBContext.newInstance(PublicationDeliveryStructure.class);

    }

    /**
     * Helper method to extract string value from MultilingualString.
     * In NeTEx 2.0, MultilingualString uses mixed content instead of a simple value.
     */
    protected static String getStringValue(MultilingualString multilingualString) {
        if (multilingualString == null || multilingualString.getContent() == null) {
            return null;
        }
        return multilingualString.getContent().stream()
                .filter(String.class::isInstance)
                .map(String.class::cast)
                .findFirst()
                .orElse(null);
    }


}
