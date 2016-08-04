package no.rutebanken.netex.client;

import no.rutebanken.netex.model.ObjectFactory;
import no.rutebanken.netex.model.PublicationDeliveryStructure;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.xml.bind.*;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;

public class PublicationDeliveryClient {

    private static final Logger logger = LoggerFactory.getLogger(PublicationDeliveryClient.class);

    private final ObjectFactory objectFactory = new ObjectFactory();

    private final String publicationDeliveryUrl;

    private final JAXBContext jaxbContext;

    public PublicationDeliveryClient(String publicationDeliveryUrl) throws JAXBException {
        this.publicationDeliveryUrl = publicationDeliveryUrl;
        this.jaxbContext = JAXBContext.newInstance(PublicationDeliveryStructure.class);
    }

    public PublicationDeliveryStructure sendPublicationDelivery(PublicationDeliveryStructure publicationDelivery) throws JAXBException, IOException {

        Marshaller marshaller = jaxbContext.createMarshaller();
        Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();
        marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);

        try {
            URL url = new URL(publicationDeliveryUrl);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();

            connection.setRequestMethod("POST");
            connection.setRequestProperty("Content-type", "application/xml");
            connection.setDoOutput(true);

            marshaller.marshal(objectFactory.createPublicationDelivery(publicationDelivery), connection.getOutputStream());

            int responseCode = connection.getResponseCode();
            logger.info("Got response code {} after posting publication delivery to URL : {}", responseCode, url);

            JAXBElement<PublicationDeliveryStructure> element = (JAXBElement<PublicationDeliveryStructure>) unmarshaller.unmarshal(connection.getInputStream());

            return element.getValue();
        } catch (IOException e) {
            throw new IOException("Error posting XML to " + publicationDeliveryUrl, e);
        }

    }
}
