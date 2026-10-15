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

import javax.xml.bind.annotation.adapters.XmlAdapter;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;

/**
 * Maps xs:date to {@link LocalDate}. An optional trailing zone offset ({@code 2026-05-03+02:00},
 * {@code 2026-05-03Z}), as permitted by xs:date, is accepted and ignored when unmarshalling. Dates
 * are always marshalled as {@code yyyy-MM-dd}.
 */
public class LocalDateXmlAdapter extends XmlAdapter<String, LocalDate> {

	private static final DateTimeFormatter PARSER = new DateTimeFormatterBuilder()
			.append(DateTimeFormatter.ISO_LOCAL_DATE)
			.optionalStart().appendPattern("XXXXX").optionalEnd()
			.toFormatter();

	@Override
	public LocalDate unmarshal(String inputDate) {
		return LocalDate.parse(inputDate, PARSER);
	}

	@Override
	public String marshal(LocalDate inputDate) {
		if (inputDate != null) {
			return DateTimeFormatter.ISO_LOCAL_DATE.format(inputDate);
		} else {
			return null;
		}
	}

}
