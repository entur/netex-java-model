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

	public enum NetexVersion {
		V1_0_4beta, V1_0_7
	};

	private final Schema neTExSchema;

	public NeTExValidator() throws IOException, SAXException {
		this(NetexVersion.V1_0_7); // Default to latest
	}

	public NeTExValidator(NetexVersion version) throws IOException, SAXException {
		SchemaFactory factory = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
		Source schemaFile;
		switch (version) {
		case V1_0_4beta:
			factory.setResourceResolver(new PredefinedSchemaListClasspathResourceResolver("/netex_schema_list_1_0_4beta.txt"));
			schemaFile = new StreamSource(getClass().getResourceAsStream("/NeTEx-XML-1.04beta/schema/xsd/NeTEx_publication.xsd"));
			break;
		case V1_0_7:
		default:
			factory.setResourceResolver(new PredefinedSchemaListClasspathResourceResolver("/netex_schema_list_1_0_7.txt"));
			schemaFile = new StreamSource(getClass().getResourceAsStream("/NeTEx-XML-1.07/schema/xsd/NeTEx_publication.xsd"));

		}

		neTExSchema = factory.newSchema(schemaFile);
	}

	public Schema getSchema() throws SAXException, IOException {
		return neTExSchema;
	}

	public void validate(Source source) throws IOException, SAXException {
		Validator validator = neTExSchema.newValidator();
		validator.validate(source);
	}
}
