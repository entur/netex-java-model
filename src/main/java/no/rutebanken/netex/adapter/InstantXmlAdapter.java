package no.rutebanken.netex.adapter;

import javax.xml.bind.annotation.adapters.XmlAdapter;
import java.time.Instant;
import java.time.format.DateTimeFormatter;

public class InstantXmlAdapter extends XmlAdapter<String, Instant> {

    @Override
    public Instant unmarshal(String value) throws Exception {
        return value == null || value.isEmpty() ? null : DateTimeFormatter.ISO_INSTANT.parse(value, Instant::from);
    }

    @Override
    public String marshal(Instant instant) throws Exception {
        return instant == null ? null : instant.toString();
    }
}
