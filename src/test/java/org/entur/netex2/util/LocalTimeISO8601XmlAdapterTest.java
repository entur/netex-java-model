package org.entur.netex2.util;

import java.util.List;
import org.junit.jupiter.api.Test;

import java.time.LocalTime;

import static org.junit.jupiter.api.Assertions.*;

class LocalTimeISO8601XmlAdapterTest {

    private final LocalTimeISO8601XmlAdapter adapter = new LocalTimeISO8601XmlAdapter();

    @Test
    void testUnmarshalBasicTime() {
        LocalTime result = adapter.unmarshal("14:30:00");
        assertEquals(LocalTime.of(14, 30, 0), result);
    }

    @Test
    void testUnmarshalTimeWithMilliseconds() {
        LocalTime result = adapter.unmarshal("14:30:00.123");
        assertEquals(LocalTime.of(14, 30, 0, 123_000_000), result);
    }

    @Test
    void testUnmarshalTimeWithOffset() {
        LocalTime result = adapter.unmarshal("14:30:00+02:00");
        assertEquals(LocalTime.of(14, 30, 0), result);
    }

    @Test
    void testUnmarshalTimeWithMillisecondsAndOffset() {
        LocalTime result = adapter.unmarshal("14:30:00.456+01:00");
        assertEquals(LocalTime.of(14, 30, 0, 456_000_000), result);
    }

    @Test
    void testUnmarshalTimeWithMicroseconds() {
        LocalTime result = adapter.unmarshal("07:55:00.398629");
        assertEquals(LocalTime.of(7, 55, 0, 398_629_000), result);
    }

    @Test
    void testUnmarshalTimeWithMicrosecondsAndOffset() {
        LocalTime result = adapter.unmarshal("07:55:00.398629+02:00");
        assertEquals(LocalTime.of(7, 55, 0, 398_629_000), result);
    }

    @Test
    void testUnmarshalTimeWithNanoseconds() {
        LocalTime result = adapter.unmarshal("07:55:00.123456789+02:00");
        assertEquals(LocalTime.of(7, 55, 0, 123_456_789), result);
    }

    @Test
    void testUnmarshalMidnight() {
        LocalTime result = adapter.unmarshal("00:00:00");
        assertEquals(LocalTime.MIDNIGHT, result);
    }

    @Test
    void testUnmarshalNoon() {
        LocalTime result = adapter.unmarshal("12:00:00");
        assertEquals(LocalTime.NOON, result);
    }

    @Test
    void testUnmarshalEndOfDay() {
        LocalTime result = adapter.unmarshal("23:59:59");
        assertEquals(LocalTime.of(23, 59, 59), result);
    }

    @Test
    void testMarshalBasicTime() {
        String result = adapter.marshal(LocalTime.of(14, 30, 0));
        assertEquals("14:30:00", result);
    }

    @Test
    void testMarshalTimeWithNanoseconds() {
        String result = adapter.marshal(LocalTime.of(14, 30, 0, 123_000_000));
        assertEquals("14:30:00.123", result);
    }

    @Test
    void testMarshalTimeWithMicroseconds() {
        String result = adapter.marshal(LocalTime.of(7, 55, 0, 398_629_000));
        assertEquals("07:55:00.398629", result);
    }

    @Test
    void testMarshalMidnight() {
        String result = adapter.marshal(LocalTime.MIDNIGHT);
        assertEquals("00:00:00", result);
    }

    @Test
    void testMarshalNoon() {
        String result = adapter.marshal(LocalTime.NOON);
        assertEquals("12:00:00", result);
    }

    @Test
    void testMarshalNull() {
        String result = adapter.marshal(null);
        assertNull(result);
    }

    @Test
    void testRoundTrip() {
        LocalTime original = LocalTime.of(15, 45, 30, 500_000_000);
        String marshalled = adapter.marshal(original);
        LocalTime unmarshalled = adapter.unmarshal(marshalled);
        assertEquals(original, unmarshalled);
    }

    @Test
    void testRoundTripWithMicroseconds() {
        LocalTime original = LocalTime.of(7, 55, 0, 398_629_000);
        assertEquals(original, adapter.unmarshal(adapter.marshal(original)));
    }

    @Test
    void testCaching() {
        String timeString = "10:20:30";
        LocalTime first = adapter.unmarshal(timeString);
        LocalTime second = adapter.unmarshal(timeString);
        assertSame(first, second, "Same string should return cached instance");
    }

    @Test
    void testCachingIdenticalValue() {
        var equalInputs = List.of(
                "12:20:00",
                "12:20:00+02:00",
                "12:20:00.000",
                "12:20:00.000+02:00"
        );

        var parsed = equalInputs.stream()
                .map(adapter::unmarshal)
                .toList();
        var first = parsed.getFirst();

        parsed.forEach(time -> assertSame(first, time, "Same time value should return same instance"));
    }
}
