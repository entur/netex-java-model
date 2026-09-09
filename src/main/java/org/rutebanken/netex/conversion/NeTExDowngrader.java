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

package org.rutebanken.netex.conversion;

import org.rutebanken.netex.validation.NeTExValidator.NetexVersion;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.xml.XMLConstants;
import javax.xml.transform.Result;
import javax.xml.transform.Source;
import javax.xml.transform.Templates;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.stream.StreamSource;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.EnumMap;
import java.util.Map;

/**
 * Converts a NeTEx document from a newer schema version to an older one by applying an XSLT stylesheet
 * bundled with this library.
 * <p>
 * The stylesheet is compiled once per {@link Conversion} and reused; a new {@link Transformer} is created
 * for every call to {@link #downgrade(Source, Result)}, so an instance can be shared between threads.
 * <p>
 * The transformation is run by the JAXP {@link TransformerFactory} available at runtime (the JDK built-in
 * XSLT 1.0 processor unless another one is on the classpath). The JDK processor builds the whole document
 * in memory, so converting large exports requires a correspondingly large heap.
 * <p>
 * Only the schema differences listed on each {@link Conversion} are handled: the output is guaranteed to be
 * valid against the target schema only if the input does not use other constructs introduced in the source
 * version.
 */
public class NeTExDowngrader {

	private static final Logger LOGGER = LoggerFactory.getLogger(NeTExDowngrader.class);

	/**
	 * Supported conversions.
	 */
	public enum Conversion {
		/**
		 * NeTEx 1.16 to NeTEx 1.15. Reverts the changes made to {@code DatedServiceJourney} in 1.16:
		 * <ul>
		 *     <li>{@code replacedJourneys/DatedVehicleJourneyRef} and {@code replacedJourneys/NormalDatedVehicleJourneyRef}
		 *     become additional {@code DatedServiceJourneyRef} elements following the journey reference;</li>
		 *     <li>{@code UicOperatingPeriod} is dropped when an {@code OperatingDayRef} is present (the two were mutually
		 *     exclusive in 1.15);</li>
		 *     <li>the version prefix of {@code PublicationDelivery/@version} is rewritten from {@code 1.16} to {@code 1.15}.</li>
		 * </ul>
		 */
		V1_16_TO_V1_15("xslt/netex-1.16-to-1.15.xsl", NetexVersion.v1_16, NetexVersion.v1_15);

		private final String stylesheet;
		private final NetexVersion sourceVersion;
		private final NetexVersion targetVersion;

		Conversion(String stylesheet, NetexVersion sourceVersion, NetexVersion targetVersion) {
			this.stylesheet = stylesheet;
			this.sourceVersion = sourceVersion;
			this.targetVersion = targetVersion;
		}

		/**
		 * @return classpath location of the XSLT stylesheet implementing this conversion.
		 */
		public String getStylesheet() {
			return stylesheet;
		}

		public NetexVersion getSourceVersion() {
			return sourceVersion;
		}

		public NetexVersion getTargetVersion() {
			return targetVersion;
		}
	}

	/**
	 * Conversion from the latest NeTEx version supported by this library to the previous one.
	 */
	public static final Conversion LATEST = Conversion.V1_16_TO_V1_15;

	private static final Map<Conversion, NeTExDowngrader> DOWNGRADERS_PER_CONVERSION = new EnumMap<>(Conversion.class);

	private static synchronized NeTExDowngrader createDowngrader(Conversion conversion) throws IOException, TransformerConfigurationException {
		NeTExDowngrader downgrader = DOWNGRADERS_PER_CONVERSION.get(conversion);
		if (downgrader == null) {
			downgrader = new NeTExDowngrader(conversion);
			DOWNGRADERS_PER_CONVERSION.put(conversion, downgrader);
		}
		return downgrader;
	}

	/**
	 * Return a shared, thread-safe downgrader for the given conversion, compiling the stylesheet on first use.
	 *
	 * @param conversion the conversion to perform, {@link #LATEST} if null.
	 */
	public static NeTExDowngrader getNeTExDowngrader(Conversion conversion) throws IOException, TransformerConfigurationException {
		if (conversion == null) {
			conversion = LATEST;
		}
		NeTExDowngrader downgrader = DOWNGRADERS_PER_CONVERSION.get(conversion);
		if (downgrader == null) {
			downgrader = createDowngrader(conversion);
		}
		return downgrader;
	}

	/**
	 * Return a shared, thread-safe downgrader for the {@link #LATEST} conversion.
	 */
	public static NeTExDowngrader getNeTExDowngrader() throws IOException, TransformerConfigurationException {
		return getNeTExDowngrader(null);
	}

	private final Conversion conversion;
	private final Templates templates;

	/**
	 * Use static getNeTExDowngrader to avoid compiling more stylesheets than needed.
	 */
	public NeTExDowngrader() throws IOException, TransformerConfigurationException {
		this(LATEST);
	}

	/**
	 * Use static getNeTExDowngrader to avoid compiling more stylesheets than needed.
	 */
	public NeTExDowngrader(Conversion conversion) throws IOException, TransformerConfigurationException {
		this.conversion = conversion;
		String resourceName = conversion.getStylesheet();
		LOGGER.info("Loading resource: {}", resourceName);
		URL resource = getClass().getClassLoader().getResource(resourceName);
		if (resource == null) {
			throw new IOException("Cannot load resource " + resourceName);
		}
		TransformerFactory factory = createTransformerFactory();
		try (InputStream inputStream = resource.openStream()) {
			templates = factory.newTemplates(new StreamSource(inputStream, resource.toExternalForm()));
		}
	}

	private static TransformerFactory createTransformerFactory() throws TransformerConfigurationException {
		TransformerFactory factory = TransformerFactory.newInstance();
		factory.setFeature(XMLConstants.FEATURE_SECURE_PROCESSING, true);
		setAttributeIfSupported(factory, XMLConstants.ACCESS_EXTERNAL_DTD, "");
		setAttributeIfSupported(factory, XMLConstants.ACCESS_EXTERNAL_STYLESHEET, "");
		return factory;
	}

	private static void setAttributeIfSupported(TransformerFactory factory, String name, Object value) {
		try {
			factory.setAttribute(name, value);
		} catch (IllegalArgumentException e) {
			LOGGER.debug("Attribute {} not supported by {}", name, factory.getClass().getName());
		}
	}

	public Conversion getConversion() {
		return conversion;
	}

	/**
	 * @return the compiled stylesheet, for callers that need to configure the {@link Transformer} themselves.
	 */
	public Templates getTemplates() {
		return templates;
	}

	/**
	 * Transform a document in the source version of this downgrader's {@link Conversion} into the target version.
	 *
	 * @param source the input document, for instance a {@link StreamSource} over a file.
	 * @param result where the converted document is written, for instance a {@link javax.xml.transform.stream.StreamResult}.
	 */
	public void downgrade(Source source, Result result) throws TransformerException {
		Transformer transformer = templates.newTransformer();
		transformer.transform(source, result);
	}

}
