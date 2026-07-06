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

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.rutebanken.netex.model.PublicationDeliveryStructure;
import org.xml.sax.SAXParseException;

import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.JAXBException;
import jakarta.xml.bind.UnmarshalException;
import jakarta.xml.bind.Unmarshaller;
import javax.xml.validation.Schema;
import java.io.ByteArrayInputStream;
import java.nio.charset.StandardCharsets;

import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

/**
 * Verifies how the JAXB unmarshaller behaves for {@code xs:dateTime} valued elements
 * (e.g. {@code PublicationTimestamp}) once schema validation is enabled via
 * {@link Unmarshaller#setSchema}.
 *
 * <p>A number of ISO-8601-like timestamp spellings are commonly seen in the wild but are
 * NOT part of the {@code xs:dateTime} lexical space defined by XML Schema Part 2:
 * <ul>
 *   <li>time without seconds ({@code 2016-05-18T15:10+01:00})</li>
 *   <li>a space instead of the {@code 'T'} date/time separator</li>
 *   <li>an offset without a colon ({@code +0100})</li>
 *   <li>a comma instead of a dot as the fractional-second separator</li>
 *   <li>a trailing IANA zone id in brackets ({@code +01:00[Europe/Berlin]})</li>
 *   <li>a date-only value with no time component</li>
 * </ul>
 * These tests document that, with schema validation enabled, such values are rejected by
 * the NeTEx XSD - surfaced as an {@link UnmarshalException} whose root cause is a
 * {@link SAXParseException} - while a canonical {@code xs:dateTime} is accepted.
 */
class SchemaValidationDateTimeTest {

	private static JAXBContext jaxbContext;

	@BeforeAll
	static void initContext() throws JAXBException {
		jaxbContext = JAXBContext.newInstance(PublicationDeliveryStructure.class);
	}

	private static final String CANONICAL_XSD_DATE_TIME = "2016-11-29T13:32:06.869+01:00";

	private static String publicationDeliveryWithTimestamp(String publicationTimestamp) {
		return "<?xml version=\"1.0\" encoding=\"UTF-8\"?>"
				+ "<PublicationDelivery xmlns=\"http://www.netex.org.uk/netex\""
				+ " xmlns:ns2=\"http://www.opengis.net/gml/3.2\""
				+ " xmlns:ns3=\"http://www.siri.org.uk/siri\" version=\"any\">"
				+ "<PublicationTimestamp>" + publicationTimestamp + "</PublicationTimestamp>"
				+ "<ParticipantRef>NSR</ParticipantRef>"
				+ "</PublicationDelivery>";
	}

	private static Unmarshaller validatingUnmarshaller() throws Exception {
		Schema netexSchema = NeTExValidator.getNeTExValidator().getSchema();
		Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();
		unmarshaller.setSchema(netexSchema);
		return unmarshaller;
	}

	@Test
	void jaxbWithSchemaValidationAcceptsCanonicalDateTime() throws Exception {
		// Control case: a canonical, schema-valid xs:dateTime must pass validation.
		// This proves the surrounding document is valid, so any failure below is
		// attributable solely to the timestamp value.
		byte[] xml = publicationDeliveryWithTimestamp(CANONICAL_XSD_DATE_TIME)
				.getBytes(StandardCharsets.UTF_8);
		assertThatCode(() -> validatingUnmarshaller().unmarshal(new ByteArrayInputStream(xml)))
				.doesNotThrowAnyException();
	}

	@ParameterizedTest(name = "[{index}] {0}")
	@ValueSource(strings = {
			"2016-05-18T15:10+01:00",                    // time without seconds
			"2016-05-18 15:00:00+01:00",                 // space instead of 'T' separator
			"2016-05-18T15:00:00+0100",                  // offset without colon
			"2016-05-18T15:00:00,123+01:00",             // comma as fraction separator
			"2016-05-18T15:00:00+01:00[Europe/Berlin]",  // trailing IANA zone id in brackets
			"2016-05-18"                                 // date-only, no time component
	})
	void jaxbWithSchemaValidationRejectsNonXsdDateTimeFormats(String invalidDateTime) {
		byte[] xml = publicationDeliveryWithTimestamp(invalidDateTime)
				.getBytes(StandardCharsets.UTF_8);

		assertThatThrownBy(
				() -> validatingUnmarshaller().unmarshal(new ByteArrayInputStream(xml)))
				.as("PublicationTimestamp '%s' must be rejected as invalid xs:dateTime",
						invalidDateTime)
				// JAXB surfaces a schema-validation failure as an UnmarshalException
				// whose root cause is the SAX validation error - i.e. it is the XSD
				// that rejects the value.
				.isInstanceOf(UnmarshalException.class)
				.hasRootCauseInstanceOf(SAXParseException.class);
	}
}
