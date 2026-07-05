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
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.time.temporal.ChronoField;

import java.time.format.DateTimeParseException;
import java.time.format.DecimalStyle;
import java.util.List;
import java.util.regex.Pattern;

/**
 * XmlAdapter that (un)marshals {@link LocalDateTime} to/from a relaxed set of
 * ISO-8601-like string representations.
 *
 * Supported input variations:
 *  - Date-only:                 2023-01-01
 *  - 'T' or space as separator: 2023-01-01T10:15:30 / 2023-01-01 10:15:30
 *  - Seconds optional:          2023-01-01T10:15
 *  - Fractional seconds with '.' or ',':  10:15:30.123 / 10:15:30,123
 *  - Offset with colon:         +02:00
 *  - Offset without colon:      +0200
 *  - Zulu marker:                Z
 *  - Trailing zone-id in brackets (stripped, not interpreted): +01:00[Europe/Berlin]
 *
 * Notes / deliberate limitations:
 *  - The target type is LocalDateTime, so any offset/zone information found in the
 *    input is parsed only to avoid failures - it is NOT applied to shift the time.
 *    If you need offset-aware conversion, parse to OffsetDateTime/ZonedDateTime instead.
 *  - The bracketed zone-id (e.g. "[Europe/Berlin]") is stripped via regex before
 *    parsing because DateTimeFormatterBuilder has no simple "consume and ignore"
 *    construct for that syntax.
 *  - Full ISO-8601 "basic" format (no separators at all, e.g. 20230101T101530Z)
 *    is intentionally NOT supported here to keep the formatter list manageable.
 *    Add another formatter to the list below if you need it.
 */
public class LocalDateTimeISO8601XmlAdapter extends XmlAdapter<String, LocalDateTime> {

    // Strips a trailing IANA zone id in brackets, e.g. "+01:00[Europe/Berlin]" -> "+01:00"
    private static final Pattern ZONE_ID_SUFFIX = Pattern.compile("\\[.*\\]$");

    // DecimalStyle that accepts ',' as fraction separator (used by the comma-variant formatter)
    private static final DecimalStyle COMMA_DECIMAL_STYLE = DecimalStyle.STANDARD.withDecimalSeparator(',');

     /* Builds one formatter variant.
     *
     * @param dateTimeSeparatorLiteral the literal between date and time, e.g. "T" or " "
     * @param decimalStyle             decimal style to use for fractional seconds (dot or comma)
     */
    private static DateTimeFormatter buildFormatter(String dateTimeSeparatorLiteral, DecimalStyle decimalStyle) {
        DateTimeFormatterBuilder builder = new DateTimeFormatterBuilder()
                // Date part: yyyy-MM-dd
                .appendPattern("yyyy-MM-dd")

                // Time part is entirely optional -> supports date-only input
                .optionalStart()
                    .appendLiteral(dateTimeSeparatorLiteral)
                    .appendPattern("HH:mm")
                    // Seconds are optional
                    .optionalStart()
                        .appendPattern(":ss")
                    .optionalEnd()
                    // Fractional seconds are optional (uses the decimalStyle set below)
                    .optionalStart()
                        .appendFraction(ChronoField.NANO_OF_SECOND, 0, 9, true)
                    .optionalEnd()
                    // Offset with colon, e.g. +02:00, or 'Z'
                    .optionalStart()
                        .appendPattern("XXXXX")
                    .optionalEnd()
                    // Offset without colon, e.g. +0200 (only tried if the above didn't match)
                    .optionalStart()
                        .appendPattern("XX")
                    .optionalEnd()
                .optionalEnd()

                // Default missing fields so that LocalTime.MIDNIGHT / 0 seconds are assumed
                .parseDefaulting(ChronoField.HOUR_OF_DAY, 0)
                .parseDefaulting(ChronoField.MINUTE_OF_HOUR, 0)
                .parseDefaulting(ChronoField.SECOND_OF_MINUTE, 0)
                // Fixed default offset (UTC) just so parsing of an "X" pattern never fails
                // due to a missing offset. This value is irrelevant for the LocalDateTime
                // result since offset info is discarded for this target type.
                .parseDefaulting(ChronoField.OFFSET_SECONDS, 0);

        return builder.toFormatter().withDecimalStyle(decimalStyle);
    }

    // Ordered list of formatter variants tried during unmarshalling.
    // Order matters only for performance (most common case first); all variants
    // are tried until one succeeds.
    private static final List<DateTimeFormatter> PARSERS = List.of(
            buildFormatter("T", DecimalStyle.STANDARD),         // 2023-01-01T10:15:30.123+02:00
            buildFormatter(" ", DecimalStyle.STANDARD),         // 2023-01-01 10:15:30.123+02:00
            buildFormatter("T", COMMA_DECIMAL_STYLE),           // 2023-01-01T10:15:30,123+02:00
            buildFormatter(" ", COMMA_DECIMAL_STYLE)            // 2023-01-01 10:15:30,123+02:00
    );

    // Formatter used for marshalling (output). Always produces a canonical,
    // unambiguous representation: 'T' separator, dot as decimal separator.
    private static final DateTimeFormatter OUTPUT_FORMATTER =
            DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss");

    @Override
    public LocalDateTime unmarshal(String inputDate) {
        if (inputDate == null) {
            return null;
        }

        // Strip a trailing bracketed zone id, e.g. "...+01:00[Europe/Berlin]" -> "...+01:00"
        String cleaned = ZONE_ID_SUFFIX.matcher(inputDate.trim()).replaceAll("");

        DateTimeParseException lastException = null;
        for (DateTimeFormatter formatter : PARSERS) {
            try {
                return LocalDateTime.parse(cleaned, formatter);
            } catch (DateTimeParseException e) {
                lastException = e;
                // try next formatter variant
            }
        }

        // None of the variants matched - rethrow the last exception for diagnostics
        throw lastException;
    }

 @Override
	public String marshal(LocalDateTime inputDate) {
		if(inputDate != null) {
			return OUTPUT_FORMATTER.format(inputDate);
		} else {
			return null;
        }
    }
}