package org.rutebanken.util;

import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class LocalDateTimeISO8601XmlAdapterTest {

    private final LocalDateTimeISO8601XmlAdapter adapter = new LocalDateTimeISO8601XmlAdapter();

    @Test
    void testUnmarshalBasicDateTime() {
        LocalDateTime result = adapter.unmarshal("2026-08-31T00:00:00");
        assertEquals(LocalDateTime.of(2026, 8, 31, 0, 0, 0), result);
    }

    @Test
    void testUnmarshalDateTimeWithMilliseconds() {
        LocalDateTime result = adapter.unmarshal("2026-05-03T23:30:09.398");
        assertEquals(LocalDateTime.of(2026, 5, 3, 23, 30, 9, 398_000_000), result);
    }

    @Test
    void testUnmarshalDateTimeWithOffset() {
        LocalDateTime result = adapter.unmarshal("2026-05-03T23:30:09+02:00");
        assertEquals(LocalDateTime.of(2026, 5, 3, 23, 30, 9), result);
    }

    @Test
    void testUnmarshalDateTimeWithMicroseconds() {
        LocalDateTime result = adapter.unmarshal("2026-05-03T23:30:09.398629");
        assertEquals(LocalDateTime.of(2026, 5, 3, 23, 30, 9, 398_629_000), result);
    }

    @Test
    void testUnmarshalDateTimeWithMicrosecondsAndOffset() {
        LocalDateTime result = adapter.unmarshal("2026-05-03T23:30:09.398629+02:00");
        assertEquals(LocalDateTime.of(2026, 5, 3, 23, 30, 9, 398_629_000), result);
    }

    @Test
    void testUnmarshalDateTimeWithNanoseconds() {
        LocalDateTime result = adapter.unmarshal("2026-05-03T23:30:09.123456789+02:00");
        assertEquals(LocalDateTime.of(2026, 5, 3, 23, 30, 9, 123_456_789), result);
    }

    @Test
    void testMarshalBasicDateTime() {
        String result = adapter.marshal(LocalDateTime.of(2026, 8, 31, 0, 0, 0));
        assertEquals("2026-08-31T00:00:00", result);
    }

    @Test
    void testMarshalDateTimeWithMilliseconds() {
        String result = adapter.marshal(LocalDateTime.of(2026, 5, 3, 23, 30, 9, 398_000_000));
        assertEquals("2026-05-03T23:30:09.398", result);
    }

    @Test
    void testMarshalDateTimeWithMicroseconds() {
        String result = adapter.marshal(LocalDateTime.of(2026, 5, 3, 23, 30, 9, 398_629_000));
        assertEquals("2026-05-03T23:30:09.398629", result);
    }

    @Test
    void testMarshalNull() {
        assertNull(adapter.marshal(null));
    }

    @Test
    void testRoundTripWithMicroseconds() {
        LocalDateTime original = LocalDateTime.of(2026, 5, 3, 23, 30, 9, 398_629_000);
        assertEquals(original, adapter.unmarshal(adapter.marshal(original)));
    }
}
