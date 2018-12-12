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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xml.sax.SAXException;

import javax.xml.XMLConstants;
import javax.xml.transform.Source;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import javax.xml.validation.Validator;
import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

public class NeTExValidator {

	private static final Logger logger = LoggerFactory.getLogger(NeTExValidator.class);
	
	public enum NetexVersion {
		V1_0_4beta ("1.04beta"),
		V1_0_7 ("1.07"),
		v1_0_8 ("1.08"),
		v1_0_9 ("1.09");

		private final String folderName;

		NetexVersion(String folderName) {
			this.folderName = folderName;
		}

		public String toString() {
			return folderName;
		}
	}
	private final Schema neTExSchema;

	public static final NetexVersion LATEST = NetexVersion.v1_0_9;

	private static final Map<NetexVersion, NeTExValidator> VALIDATORS_PER_VERSION = new HashMap<>();

	private static synchronized NeTExValidator createValidator(NeTExValidator.NetexVersion version) throws IOException, SAXException {
		NeTExValidator validator = VALIDATORS_PER_VERSION.get(version);
		if (validator == null) {
			validator = new NeTExValidator(version);
			VALIDATORS_PER_VERSION.put(version, validator);
		}
		return validator;
	}

	public static NeTExValidator getNeTExValidator(NeTExValidator.NetexVersion version) throws IOException, SAXException {
		if (version == null) {
			version = NeTExValidator.LATEST;
		}
		NeTExValidator validator = VALIDATORS_PER_VERSION.get(version);
		if (validator == null) {
			validator = createValidator(version);

		}
		return validator;
	}

	public static NeTExValidator getNeTExValidator() throws IOException, SAXException {
		return getNeTExValidator(null);
	}


	/**
	 * "Use static getNeTExValidator to avoid initializing more schemas than needed.
	 */
	public NeTExValidator() throws IOException, SAXException {
		this(LATEST);
	}

	/**
	 * "Use static getNeTExValidator to avoid initializing more schemas than needed.
	 */
	public NeTExValidator(NetexVersion version) throws IOException, SAXException {
		SchemaFactory factory = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);

		String resourceName = "xsd/"+ version +"/NeTEx_publication.xsd";
		logger.info("Loading resource: {}", resourceName);
		URL resource = getClass().getClassLoader().getResource(resourceName);
		if(resource == null) {
			throw new IOException("Cannot load resource " + resourceName);
		}
		neTExSchema = factory.newSchema(resource);
	}

	public Schema getSchema() throws SAXException, IOException {
		return neTExSchema;
	}

	public void validate(Source source) throws IOException, SAXException {
		Validator validator = neTExSchema.newValidator();
		validator.validate(source);
	}


}
