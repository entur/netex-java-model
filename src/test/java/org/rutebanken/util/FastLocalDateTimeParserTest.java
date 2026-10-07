package org.rutebanken.util;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import org.junit.jupiter.api.Test;

class FastLocalDateTimeParserTest {

    @Test
    void testParseDate() {
        assertEquals(LocalDate.of(2026, 5, 3), FastLocalDateTimeParser.parseDate("2026-05-03"));
        assertEquals(LocalDate.of(2026, 5, 3), FastLocalDateTimeParser.parseDate("2026-05-03Z"));
        assertEquals(LocalDate.of(2026, 5, 3), FastLocalDateTimeParser.parseDate("2026-05-03+02:00"));
        assertEquals(LocalDate.of(2024, 2, 29), FastLocalDateTimeParser.parseDate("2024-02-29-18:00"));
    }

    @Test
    void testParseTime() {
        assertEquals(LocalTime.of(14, 30, 0), FastLocalDateTimeParser.parseTime("14:30:00"));
        assertEquals(LocalTime.of(0, 0, 0), FastLocalDateTimeParser.parseTime("00:00:00"));
        assertEquals(LocalTime.of(23, 59, 59), FastLocalDateTimeParser.parseTime("23:59:59Z"));
        assertEquals(LocalTime.of(14, 30, 0, 100_000_000), FastLocalDateTimeParser.parseTime("14:30:00.1"));
        assertEquals(LocalTime.of(14, 30, 0, 123_456_789), FastLocalDateTimeParser.parseTime("14:30:00.123456789-05:30"));
    }

    @Test
    void testParseDateTime() {
        assertEquals(LocalDateTime.of(2026, 5, 3, 23, 30, 9), FastLocalDateTimeParser.parseDateTime("2026-05-03T23:30:09"));
        assertEquals(
            LocalDateTime.of(2026, 5, 3, 23, 30, 9, 398_000_000),
            FastLocalDateTimeParser.parseDateTime("2026-05-03T23:30:09.398")
        );
        assertEquals(
            LocalDateTime.of(2026, 5, 3, 23, 30, 9, 398_629_000),
            FastLocalDateTimeParser.parseDateTime("2026-05-03T23:30:09.398629+02:00")
        );
        assertEquals(LocalDateTime.of(2026, 5, 3, 23, 30, 9), FastLocalDateTimeParser.parseDateTime("2026-05-03T23:30:09Z"));
    }

    /** A null input is declined, so the adapters keep throwing the formatter's NullPointerException. */
    @Test
    void testNullIsDeclined() {
        assertNull(FastLocalDateTimeParser.parseDate(null));
        assertNull(FastLocalDateTimeParser.parseTime(null));
        assertNull(FastLocalDateTimeParser.parseDateTime(null));

        assertEquals("text", assertThrows(NullPointerException.class, () -> new LocalDateXmlAdapter().unmarshal(null)).getMessage());
        assertEquals("text", assertThrows(NullPointerException.class, () -> new LocalTimeISO8601XmlAdapter().unmarshal(null)).getMessage());
        assertEquals(
            "text",
            assertThrows(NullPointerException.class, () -> new LocalDateTimeISO8601XmlAdapter().unmarshal(null)).getMessage()
        );
    }

    /** Forms the fast parser declines, leaving them to the DateTimeFormatter fallback. */
    @Test
    void testParseDateDeclines() {
        List<String> inputs = List.of(
            "",
            "2023-02-30",
            "2023-04-31",
            "2023-13-01",
            "0000-01-01",
            "+2023-01-01",
            "12023-01-01",
            "2023-1-01",
            "2023-01-01z",
            "2023-01-01+19:00",
            "2023-01-01+18:30",
            "2023-01-01+02",
            "2023-01-01+0200",
            "2023-01-01+02:00:00",
            "2023-01-01 ",
            "2023-01-01T00:00:00"
        );
        for (String text : inputs) {
            assertNull(FastLocalDateTimeParser.parseDate(text), () -> "Input '" + text + "'");
        }
    }

    @Test
    void testParseTimeDeclines() {
        List<String> inputs = List.of(
            "",
            "24:00:00",
            "23:60:00",
            "23:59:60",
            "10:20",
            "1:20:30",
            "10:20:30.",
            "10:20:30.1234567890",
            "10:20:30.Z",
            "10:20:30+02:60",
            "10:20:30+02:00:00",
            " 10:20:30",
            "10:20:30 ",
            "١٠:20:30"
        );
        for (String text : inputs) {
            assertNull(FastLocalDateTimeParser.parseTime(text), () -> "Input '" + text + "'");
        }
    }

    @Test
    void testParseDateTimeDeclines() {
        List<String> inputs = List.of(
            "",
            "2024-01-01 10:00:00",
            "2024-01-01t10:00:00",
            "2024-01-01T10:00",
            "2024-01-01T24:00:00",
            "2023-02-30T10:00:00",
            "0000-01-01T00:00:00",
            "2024-01-01T10:00:00.",
            "2024-01-01T10:00:00.1234567891",
            "2024-01-01T10:00:00+19:00"
        );
        for (String text : inputs) {
            assertNull(FastLocalDateTimeParser.parseDateTime(text), () -> "Input '" + text + "'");
        }
    }
}
