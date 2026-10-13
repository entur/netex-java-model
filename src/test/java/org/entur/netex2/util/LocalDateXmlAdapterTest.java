package org.entur.netex2.util;

import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;

import static org.junit.jupiter.api.Assertions.*;

class LocalDateXmlAdapterTest {

    private final LocalDateXmlAdapter adapter = new LocalDateXmlAdapter();

    @Test
    void testUnmarshalBasicDate() {
        LocalDate result = adapter.unmarshal("2026-08-31");
        assertEquals(LocalDate.of(2026, 8, 31), result);
    }

    @Test
    void testUnmarshalDateWithOffset() {
        LocalDate result = adapter.unmarshal("2026-05-03+02:00");
        assertEquals(LocalDate.of(2026, 5, 3), result);
    }

    @Test
    void testUnmarshalDateWithUtcDesignator() {
        LocalDate result = adapter.unmarshal("2026-05-03Z");
        assertEquals(LocalDate.of(2026, 5, 3), result);
    }

    @Test
    void testUnmarshalDateTimeIsRejected() {
        assertThrows(DateTimeParseException.class, () -> adapter.unmarshal("2026-05-03T00:00:00"));
    }

    @Test
    void testMarshalBasicDate() {
        String result = adapter.marshal(LocalDate.of(2026, 8, 31));
        assertEquals("2026-08-31", result);
    }

    @Test
    void testMarshalNull() {
        assertNull(adapter.marshal(null));
    }

    @Test
    void testRoundTrip() {
        LocalDate original = LocalDate.of(2026, 5, 3);
        assertEquals(original, adapter.unmarshal(adapter.marshal(original)));
    }
}
