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

package org.rutebanken.netex.client;

import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.JAXBElement;
import jakarta.xml.bind.JAXBException;
import jakarta.xml.bind.Marshaller;
import jakarta.xml.bind.Unmarshaller;
import org.rutebanken.netex.model.ObjectFactory;
import org.rutebanken.netex.model.PublicationDeliveryStructure;
import org.rutebanken.netex.validation.NeTExValidator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xml.sax.SAXException;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class PublicationDeliveryClient {

    private static final Logger LOGGER = LoggerFactory.getLogger(PublicationDeliveryClient.class);

    private final ObjectFactory objectFactory = new ObjectFactory();

    private final String publicationDeliveryUrl;

    private final JAXBContext jaxbContext;
    private final boolean validateAgainstSchema;

    private NeTExValidator neTExValidator;

    /**
     *
     * @param publicationDeliveryUrl Url to where to POST the publication delivery XML
     * @param validateAgainstSchema If serialized XML should be validated against the NeTEx schema.
     */
    public PublicationDeliveryClient(String publicationDeliveryUrl, boolean validateAgainstSchema) throws JAXBException, IOException, SAXException {
        this.publicationDeliveryUrl = publicationDeliveryUrl;
        this.jaxbContext = JAXBContext.newInstance(PublicationDeliveryStructure.class);
        this.validateAgainstSchema = validateAgainstSchema;
        if(validateAgainstSchema) {
            neTExValidator = new NeTExValidator();
        }
    }

    public PublicationDeliveryClient(String publicationDeliveryUrl) throws JAXBException, IOException, SAXException {
        this(publicationDeliveryUrl, false);
    }

    public PublicationDeliveryStructure sendPublicationDelivery(PublicationDeliveryStructure publicationDelivery) throws JAXBException, IOException {

        Marshaller marshaller = jaxbContext.createMarshaller();
        Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();
        marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);

        if(validateAgainstSchema) {
            marshaller.setSchema(neTExValidator.getSchema());
        }

        URL url = new URL(publicationDeliveryUrl);
        HttpURLConnection connection = null;
        try  {
            connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Content-type", "application/xml");
            connection.setDoOutput(true);

            LOGGER.info("About to start marshalling publication delivery to output stream {}", publicationDelivery);
            marshaller.marshal(objectFactory.createPublicationDelivery(publicationDelivery), connection.getOutputStream());
            LOGGER.info("Done marshalling publication delivery to output stream. (Schema validation was set to {}) {}", validateAgainstSchema, publicationDelivery);

            int responseCode = connection.getResponseCode();
            LOGGER.info("Got response code {} after posting publication delivery to URL : {}", responseCode, url);

            InputStream inputStream = connection.getInputStream();
            JAXBElement<PublicationDeliveryStructure> element = (JAXBElement<PublicationDeliveryStructure>) unmarshaller.unmarshal(inputStream);

            return element.getValue();
        } catch (Exception e) {
            throw new IOException("Error posting XML to " + publicationDeliveryUrl, e);
        } finally {
            if(connection != null) {
                connection.disconnect();
            }
        }


    }
}
