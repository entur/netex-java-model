package org.rutebanken.netex.validation;

import org.xml.sax.SAXException;

import javax.xml.XMLConstants;
import javax.xml.transform.Source;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import javax.xml.validation.Validator;
import java.io.IOException;

public class NeTExValidator {

    private final Schema neTExSchema;

    public NeTExValidator() throws IOException, SAXException {
        SchemaFactory factory = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
        factory.setResourceResolver(new PredefinedSchemaListClasspathResourceResolver("/netex_schema_list.txt"));
        Source schemaFile = new StreamSource(getClass().getResourceAsStream("/NeTEx-XML-1.04beta/schema/xsd/NeTEx_publication.xsd"));
        neTExSchema = factory.newSchema(schemaFile);
    }

    public Schema getSchema() throws SAXException, IOException {
        return neTExSchema;
    }

    public void validate(StreamSource streamSource) throws IOException, SAXException {
        Validator validator = neTExSchema.newValidator();
        validator.validate(streamSource);
    }
}
