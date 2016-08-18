package no.rutebanken.netex.adapter;

import javax.xml.bind.annotation.adapters.XmlAdapter;
import java.time.Instant;
import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;

public class OffsetDateTimeXmlAdapter extends XmlAdapter<String, OffsetDateTime> {

    @Override
    public OffsetDateTime unmarshal(String value) throws Exception {
        return value == null || value.isEmpty() ? null : DateTimeFormatter.ISO_OFFSET_DATE_TIME.parse(value, OffsetDateTime::from);
    }

    @Override
    public String marshal(OffsetDateTime offsetDateTime) throws Exception {
        return offsetDateTime == null ? null : offsetDateTime.toString();
    }
}
