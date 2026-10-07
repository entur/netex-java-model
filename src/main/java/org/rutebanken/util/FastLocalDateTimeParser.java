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

import java.time.DateTimeException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

/**
 * Position-based parser for the common forms of xs:date, xs:time and xs:dateTime found in NeTEx
 * documents:
 * <ul>
 *   <li>date: {@code yyyy-MM-dd[Z|±HH:MM]}</li>
 *   <li>time: {@code HH:mm:ss[.fffffffff][Z|±HH:MM]}</li>
 *   <li>dateTime: {@code yyyy-MM-ddTHH:mm:ss[.fffffffff][Z|±HH:MM]}</li>
 * </ul>
 * <p>
 * Why this exists: NeTEx documents contain millions of time values, and parsing them with a
 * {@code DateTimeFormatter} costs around 500 ns and 1 KB of garbage per value, spent in the
 * formatter machinery (sub-parser walks, {@code Parsed} copies, optional-group allocations) to
 * parse strings that in practice have one fixed shape. This parser reads the fields at fixed
 * positions, uses no regular expressions and allocates nothing but the result.
 * <p>
 * It is deliberately strict. The zone designator is validated and then ignored, as the
 * formatter-based adapters ignore it when building a local value. Any other form and any
 * out-of-range field makes it return {@code null}, so that the caller falls back to its
 * {@code DateTimeFormatter}. This keeps the formatter's lenient resolution (day-of-month clamping
 * such as {@code 2023-02-30} to {@code 2023-02-28}, {@code 24:00:00} as midnight, a fraction
 * separator without digits, ...) and its error messages unchanged.
 */
final class FastLocalDateTimeParser {

	/*
	 * Layout of the supported text. The date and time parts have fixed positions; the fraction and
	 * the zone designator are optional and follow directly after the seconds.
	 *
	 *   yyyy-MM-dd          HH:mm:ss.fffffffff+HH:MM
	 *   0    5  8           0  3  6 |         |
	 *                               8         fraction end, zone designator starts here
	 *
	 * In a dateTime the time part starts at index 11, after the 'T' separator.
	 */
	private static final int DATE_LENGTH = 10;
	private static final int TIME_LENGTH = 8;
	private static final int DATE_TIME_SEPARATOR = DATE_LENGTH;
	private static final int DATE_TIME_TIME_START = DATE_LENGTH + 1;

	private static final int MAX_FRACTION_DIGITS = 9;
	/** Length of {@code ±HH:MM}. */
	private static final int OFFSET_LENGTH = 6;
	private static final int MAX_OFFSET_HOURS = 18;

	/** Multiplier turning a fraction of {@code n} digits into nanoseconds, indexed by {@code n}. */
	private static final int[] NANOS_PER_FRACTION_DIGIT = {
		0, 100_000_000, 10_000_000, 1_000_000, 100_000, 10_000, 1_000, 100, 10, 1,
	};

	/** Returned by the {@code read*} helpers when the text does not have the expected shape. */
	private static final int INVALID = -1;

	private FastLocalDateTimeParser() {}

	/**
	 * Parses an xs:date in the form {@code yyyy-MM-dd[Z|±HH:MM]}.
	 *
	 * @return the parsed date, or {@code null} if {@code text} is null or not in the supported form
	 */
	static LocalDate parseDate(String text) {
		if (text == null || text.length() < DATE_LENGTH || !hasValidZoneSuffix(text, DATE_LENGTH)) {
			return null;
		}
		return readDate(text, 0);
	}

	/**
	 * Parses an xs:time in the form {@code HH:mm:ss[.fffffffff][Z|±HH:MM]}.
	 *
	 * @return the parsed time, or {@code null} if {@code text} is null or not in the supported form
	 */
	static LocalTime parseTime(String text) {
		if (text == null) {
			return null;
		}
		return parseTimeAt(text, 0);
	}

	/**
	 * Parses an xs:dateTime in the form {@code yyyy-MM-ddTHH:mm:ss[.fffffffff][Z|±HH:MM]}.
	 *
	 * @return the parsed date-time, or {@code null} if {@code text} is null or not in the supported
	 * form
	 */
	static LocalDateTime parseDateTime(String text) {
		if (text == null || text.length() <= DATE_TIME_SEPARATOR || text.charAt(DATE_TIME_SEPARATOR) != 'T') {
			return null;
		}
		LocalDate date = readDate(text, 0);
		if (date == null) {
			return null;
		}
		LocalTime time = parseTimeAt(text, DATE_TIME_TIME_START);
		if (time == null) {
			return null;
		}
		return LocalDateTime.of(date, time);
	}

	/**
	 * Parses {@code HH:mm:ss[.fffffffff][Z|±HH:MM]} starting at {@code start} and running to the end
	 * of the text; {@code null} if malformed or out of range.
	 */
	private static LocalTime parseTimeAt(String text, int start) {
		int timeEnd = start + TIME_LENGTH;
		if (text.length() < timeEnd) {
			return null;
		}
		int fractionEnd = readFractionEnd(text, timeEnd);
		if (fractionEnd == INVALID || !hasValidZoneSuffix(text, fractionEnd)) {
			return null;
		}
		return readTime(text, start, readNanos(text, timeEnd, fractionEnd));
	}

	/** Reads {@code yyyy-MM-dd} at {@code start}; {@code null} if malformed or out of range. */
	private static LocalDate readDate(String text, int start) {
		if (text.charAt(start + 4) != '-' || text.charAt(start + 7) != '-') {
			return null;
		}
		int year = readDigits(text, start, 4);
		int month = readDigits(text, start + 5, 2);
		int day = readDigits(text, start + 8, 2);
		// year 0000 is not a valid year-of-era for the 'yyyy' pattern: leave it to the formatter
		if (year <= 0 || month == INVALID || day == INVALID) {
			return null;
		}
		try {
			return LocalDate.of(year, month, day);
		} catch (DateTimeException e) {
			return null;
		}
	}

	/** Reads {@code HH:mm:ss} at {@code start}; {@code null} if malformed or out of range. */
	private static LocalTime readTime(String text, int start, int nanos) {
		if (text.charAt(start + 2) != ':' || text.charAt(start + 5) != ':') {
			return null;
		}
		int hour = readDigits(text, start, 2);
		int minute = readDigits(text, start + 3, 2);
		int second = readDigits(text, start + 6, 2);
		if (hour == INVALID || minute == INVALID || second == INVALID) {
			return null;
		}
		try {
			return LocalTime.of(hour, minute, second, nanos);
		} catch (DateTimeException e) {
			return null;
		}
	}

	/**
	 * Locates the end of the optional fraction starting at {@code start}: {@code start} when there
	 * is none, the index after its last digit otherwise, or {@link #INVALID} for a separator without
	 * digits or more than nine digits.
	 */
	private static int readFractionEnd(String text, int start) {
		int length = text.length();
		if (length == start || text.charAt(start) != '.') {
			return start;
		}
		int pos = start + 1;
		while (pos < length && isDigit(text.charAt(pos))) {
			pos++;
		}
		int fractionDigits = pos - (start + 1);
		if (fractionDigits < 1 || fractionDigits > MAX_FRACTION_DIGITS) {
			return INVALID;
		}
		return pos;
	}

	/**
	 * Converts the fraction digits, if any, between the separator at {@code start} and
	 * {@code fractionEnd} to nanoseconds. {@link #readFractionEnd} has already checked that these
	 * characters are digits.
	 */
	private static int readNanos(String text, int start, int fractionEnd) {
		if (fractionEnd == start) {
			return 0;
		}
		int value = 0;
		for (int i = start + 1; i < fractionEnd; i++) {
			value = value * 10 + (text.charAt(i) - '0');
		}
		return value * NANOS_PER_FRACTION_DIGIT[fractionEnd - start - 1];
	}

	/**
	 * Checks that the text either ends at {@code start} or continues with exactly a zone designator:
	 * {@code Z} or {@code ±HH:MM} within the ±18:00 range.
	 */
	private static boolean hasValidZoneSuffix(String text, int start) {
		int length = text.length();
		if (start == length) {
			return true;
		}
		char designator = text.charAt(start);
		if (designator == 'Z') {
			return start + 1 == length;
		}
		if (designator != '+' && designator != '-') {
			return false;
		}
		if (start + OFFSET_LENGTH != length || text.charAt(start + 3) != ':') {
			return false;
		}
		int hours = readDigits(text, start + 1, 2);
		int minutes = readDigits(text, start + 4, 2);
		if (hours == INVALID || minutes == INVALID || minutes >= 60) {
			return false;
		}
		return hours < MAX_OFFSET_HOURS || (hours == MAX_OFFSET_HOURS && minutes == 0);
	}

	/** Reads exactly {@code count} ASCII digits starting at {@code start}, or {@link #INVALID}. */
	private static int readDigits(String text, int start, int count) {
		int value = 0;
		for (int i = start; i < start + count; i++) {
			char c = text.charAt(i);
			if (!isDigit(c)) {
				return INVALID;
			}
			value = value * 10 + (c - '0');
		}
		return value;
	}

	private static boolean isDigit(char c) {
		return c >= '0' && c <= '9';
	}
}
