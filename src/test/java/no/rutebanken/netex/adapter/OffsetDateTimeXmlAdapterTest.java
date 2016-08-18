package no.rutebanken.netex.adapter;

import org.junit.Test;

import java.time.*;

import static org.assertj.core.api.Assertions.assertThat;


public class OffsetDateTimeXmlAdapterTest {
    OffsetDateTimeXmlAdapter offsetDateTimeXmlAdapter = new OffsetDateTimeXmlAdapter();

    @Test
    public void unmarshalUTC() throws Exception {
        OffsetDateTime actual = offsetDateTimeXmlAdapter.unmarshal("2016-08-17T04:26:23Z");
        assertThat(actual.getDayOfMonth()).isEqualTo(17);
        assertThat(actual.getMonthValue()).isEqualTo(8);
        assertThat(actual.getHour()).isEqualTo(4);
        assertThat(actual.getMinute()).isEqualTo(26);
        assertThat(actual.getSecond()).isEqualTo(23);
        assertThat(actual.getOffset().toString()).isEqualTo("Z");
    }

    @Test
    public void unmarshalUTCMinus6Hours() throws Exception {
        OffsetDateTime actual = offsetDateTimeXmlAdapter.unmarshal("2016-08-17T04:26:23-06:00");
        assertThat(actual.getOffset().toString()).isEqualTo("-06:00");
    }

    @Test
    public void marshal() throws Exception {
        OffsetDateTime offsetDateTime = OffsetDateTime.ofInstant(Instant.ofEpochMilli(1000000000000L), ZoneId.of("Z"));
        String actual = offsetDateTimeXmlAdapter.marshal(offsetDateTime);
        assertThat(actual).isEqualTo("2001-09-09T01:46:40Z");
    }

}