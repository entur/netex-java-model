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

package org.rutebanken.util;

import jakarta.xml.bind.annotation.adapters.XmlAdapter;

public class VersionXmlAdapter extends XmlAdapter<String, String> {

	/**
	 * EPIP allows the string 'any' as a valid version, so we deduplicate it to conserve memory. In one
	 * especially bad example this saved 1.7 GB of memory!
	 */
	private static final String ANY = "any";

	@Override
	public String unmarshal(String input) {
		return ANY.equals(input) ? ANY : input;
	}

	@Override
	public String marshal(String input) {
		return input;
	}

}
