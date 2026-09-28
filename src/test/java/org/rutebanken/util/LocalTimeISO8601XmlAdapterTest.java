package org.rutebanken.util;

import java.util.List;
import java.util.stream.Collectors;
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
    public void testUnmarshalTimeWithMicroseconds() {
        LocalTime result = adapter.unmarshal("07:55:00.398629");
        assertEquals(LocalTime.of(7, 55, 0, 398_629_000), result);
    }

    @Test
    public void testUnmarshalTimeWithMicrosecondsAndOffset() {
        LocalTime result = adapter.unmarshal("07:55:00.398629+02:00");
        assertEquals(LocalTime.of(7, 55, 0, 398_629_000), result);
    }

    @Test
    public void testUnmarshalTimeWithNanoseconds() {
        LocalTime result = adapter.unmarshal("07:55:00.123456789+02:00");
        assertEquals(LocalTime.of(7, 55, 0, 123_456_789), result);
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
    public void testMarshalTimeWithMicroseconds() {
        String result = adapter.marshal(LocalTime.of(7, 55, 0, 398_629_000));
        assertEquals("07:55:00.398629", result);
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
    public void testRoundTripWithMicroseconds() {
        LocalTime original = LocalTime.of(7, 55, 0, 398_629_000);
        assertEquals(original, adapter.unmarshal(adapter.marshal(original)));
    }

    @Test
    public void testCaching() {
        String timeString = "10:20:30";
        LocalTime first = adapter.unmarshal(timeString);
        LocalTime second = adapter.unmarshal(timeString);
        assertSame(first, second, "Same string should return cached instance");
    }

    @Test
    public void testCachingIdenticalValue() {
        var equalInputs = List.of(
                "12:20:00",
                "12:20:00+02:00",
                "12:20:00.000",
                "12:20:00.000+02:00"
        );

        var parsed = equalInputs.stream()
                .map(adapter::unmarshal)
                .collect(Collectors.toList());
        var first = parsed.get(0);

        parsed.forEach(time -> assertSame(first, time, "Same time value should return same instance"));
    }

    @Test
    public void testFastPathReturnsSameInstanceAsParsedEquivalent() {
        // "10:20:30" is a plain "HH:mm:ss" string that hits the precomputed second-of-day cache
        // directly, without ever going through the DateTimeFormatter parser. Values that must go
        // through the parser (offset, fractional zero) should still resolve to that same cached
        // instance rather than a freshly parsed, distinct object.
        LocalTime fastPath = adapter.unmarshal("10:20:30");
        LocalTime viaOffset = adapter.unmarshal("10:20:30+02:00");
        LocalTime viaFraction = adapter.unmarshal("10:20:30.000");
        LocalTime viaFractionAndOffset = adapter.unmarshal("10:20:30.000-05:00");

        assertSame(fastPath, viaOffset);
        assertSame(fastPath, viaFraction);
        assertSame(fastPath, viaFractionAndOffset);
    }

    @Test
    public void testEverySecondOfTheDayIsCachedAndConsistent() {
        for (int secondOfDay = 0; secondOfDay < 24 * 60 * 60; secondOfDay += 37) {
            LocalTime expected = LocalTime.ofSecondOfDay(secondOfDay);
            String input = String.format("%02d:%02d:%02d", expected.getHour(), expected.getMinute(), expected.getSecond());

            LocalTime first = adapter.unmarshal(input);
            LocalTime second = adapter.unmarshal(input);

            assertEquals(expected, first);
            assertSame(first, second, "Repeated unmarshal of '" + input + "' should return the cached instance");
        }
    }

    @Test
    public void testSubSecondTimesAreNotCached() {
        LocalTime first = adapter.unmarshal("18:00:00.001");
        LocalTime second = adapter.unmarshal("18:00:00.001");

        assertEquals(first, second);
        assertNotSame(first, second, "Sub-second times should not be served from the whole-second cache");
    }
}
