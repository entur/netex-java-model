package no.rutebanken.netex.adapter;

import org.junit.Test;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.ZoneId;

import static org.assertj.core.api.Assertions.assertThat;


public class OffsetDateTimeXmlAdapterTest {
    OffsetDateTimeXmlAdapter offsetDateTimeXmlAdapter = new OffsetDateTimeXmlAdapter();

    @Test
    public void unmarshal() throws Exception {
        // The dateTime is specified in the following form "YYYY-MM-DDThh:mm:ss"
        OffsetDateTime actual = offsetDateTimeXmlAdapter.unmarshal("2016-08-17T04:26:23Z");
        assertThat(actual.getDayOfMonth()).isEqualTo(17);
    }

    @Test
    public void marshal() throws Exception {
        OffsetDateTime offsetDateTime = OffsetDateTime.ofInstant(Instant.ofEpochMilli(1000000000000L), ZoneId.of("Z"));
        String actual = offsetDateTimeXmlAdapter.marshal(offsetDateTime);
        System.out.println(actual);
        assertThat(actual).isEqualTo("2001-09-09T01:46:40Z");
    }

}