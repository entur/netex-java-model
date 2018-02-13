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
		Source schemaFile = new StreamSource(getClass().getResourceAsStream("/xsd/NeTEx_publication.xsd"));
		factory.setResourceResolver(new PredefinedSchemaListClasspathResourceResolver("/netex_schema_list.txt"));
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
