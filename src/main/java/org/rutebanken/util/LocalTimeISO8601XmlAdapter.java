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
import java.time.LocalTime;
import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.time.temporal.ChronoField;
import java.util.HashMap;
import java.util.Map;

public class LocalTimeISO8601XmlAdapter extends XmlAdapter<String, LocalTime> {

	private static final DateTimeFormatter formatter = new DateTimeFormatterBuilder().appendPattern("HH:mm:ss")
			.optionalStart().appendFraction(ChronoField.NANO_OF_SECOND, 0, 9, true).optionalEnd()
			.optionalStart().appendPattern("XXXXX")
            .optionalEnd()

//
	.parseDefaulting(ChronoField.OFFSET_SECONDS,OffsetDateTime.now().getLong(ChronoField.OFFSET_SECONDS) ).toFormatter();

	private static final int SECONDS_PER_DAY = 24 * 60 * 60;
	/**
	 * Parsing a LocalTime with a DateTimeFormatter is expensive and shows up in real-world
	 * profiles, since NeTEx documents contain a huge number of time values. Since there are only
	 * 86400 distinct whole-second times in a day, we precompute all of them once: as canonical
	 * LocalTime instances indexed by second-of-day (for reuse/dedup), and by their formatted
	 * string representation (for a direct lookup that bypasses the parser entirely for the common
	 * "HH:mm:ss" case).
	 */
	private static final LocalTime[] TIMES_BY_SECOND_OF_DAY = new LocalTime[SECONDS_PER_DAY];
	private static final Map<String, LocalTime> TIME_BY_STRING = new HashMap<>(SECONDS_PER_DAY * 2);

	static {
		for (int secondOfDay = 0; secondOfDay < SECONDS_PER_DAY; secondOfDay++) {
			LocalTime time = LocalTime.ofSecondOfDay(secondOfDay);
			TIMES_BY_SECOND_OF_DAY[secondOfDay] = time;
			TIME_BY_STRING.put(formatter.format(time), time);
		}
	}

	@Override
	public LocalTime unmarshal(String input) {
		// fast path: avoid the DateTimeFormatter parser entirely for plain whole-second times
		LocalTime cached = TIME_BY_STRING.get(input);
		if (cached != null) {
			return cached;
		}

		var key = LocalTime.parse(input, formatter);
		// only reuse the cached instance if nano is zero, so as not to increase the size of the
		// cache unduly, since there is a limited number of seconds in a single day
		if (key.getNano() == 0) {
			return TIMES_BY_SECOND_OF_DAY[key.toSecondOfDay()];
		}
		// sub-second times are not cached to not increase the size of the cache unduly
		else {
			return key;
		}
	}

	@Override
	public String marshal(LocalTime inputDate) {
		if(inputDate != null) {
			return formatter.format(inputDate);
		} else {
			return null;
		}
	}

}
