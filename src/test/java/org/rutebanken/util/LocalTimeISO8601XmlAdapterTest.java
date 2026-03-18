package org.rutebanken.util;

import org.junit.jupiter.api.Test;

import java.time.LocalTime;

import static org.junit.jupiter.api.Assertions.*;

public class LocalTimeISO8601XmlAdapterTest {

    private final LocalTimeISO8601XmlAdapter adapter = new LocalTimeISO8601XmlAdapter();

    @Test
    public void testUnmarshalBasicTime() {
        LocalTime result = adapter.unmarshal("14:30:00");
        assertEquals(LocalTime.of(14, 30, 0), result);
    }

    @Test
    public void testUnmarshalTimeWithMilliseconds() {
        LocalTime result = adapter.unmarshal("14:30:00.123");
        assertEquals(LocalTime.of(14, 30, 0, 123_000_000), result);
    }

    @Test
    public void testUnmarshalTimeWithOffset() {
        LocalTime result = adapter.unmarshal("14:30:00+02:00");
        assertEquals(LocalTime.of(14, 30, 0), result);
    }

    @Test
    public void testUnmarshalTimeWithMillisecondsAndOffset() {
        LocalTime result = adapter.unmarshal("14:30:00.456+01:00");
        assertEquals(LocalTime.of(14, 30, 0, 456_000_000), result);
    }

    @Test
    public void testUnmarshalMidnight() {
        LocalTime result = adapter.unmarshal("00:00:00");
        assertEquals(LocalTime.MIDNIGHT, result);
    }

    @Test
    public void testUnmarshalNoon() {
        LocalTime result = adapter.unmarshal("12:00:00");
        assertEquals(LocalTime.NOON, result);
    }

    @Test
    public void testUnmarshalEndOfDay() {
        LocalTime result = adapter.unmarshal("23:59:59");
        assertEquals(LocalTime.of(23, 59, 59), result);
    }

    @Test
    public void testMarshalBasicTime() {
        String result = adapter.marshal(LocalTime.of(14, 30, 0));
        assertEquals("14:30:00", result);
    }

    @Test
    public void testMarshalTimeWithNanoseconds() {
        String result = adapter.marshal(LocalTime.of(14, 30, 0, 123_000_000));
        assertEquals("14:30:00.123", result);
    }

    @Test
    public void testMarshalMidnight() {
        String result = adapter.marshal(LocalTime.MIDNIGHT);
        assertEquals("00:00:00", result);
    }

    @Test
    public void testMarshalNoon() {
        String result = adapter.marshal(LocalTime.NOON);
        assertEquals("12:00:00", result);
    }

    @Test
    public void testMarshalNull() {
        String result = adapter.marshal(null);
        assertNull(result);
    }

    @Test
    public void testRoundTrip() {
        LocalTime original = LocalTime.of(15, 45, 30, 500_000_000);
        String marshalled = adapter.marshal(original);
        LocalTime unmarshalled = adapter.unmarshal(marshalled);
        assertEquals(original, unmarshalled);
    }

    @Test
    public void testCaching() {
        String timeString = "10:20:30";
        LocalTime first = adapter.unmarshal(timeString);
        LocalTime second = adapter.unmarshal(timeString);
        assertSame(first, second, "Same string should return cached instance");
    }
}
