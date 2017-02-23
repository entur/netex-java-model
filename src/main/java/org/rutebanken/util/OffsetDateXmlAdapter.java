package org.rutebanken.util;
import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.time.temporal.ChronoField;

import javax.xml.bind.annotation.adapters.XmlAdapter;

public class OffsetDateXmlAdapter extends XmlAdapter<String, OffsetDateTime> {
	private final static DateTimeFormatter formatter = new DateTimeFormatterBuilder().appendPattern("yyyy-MM-dd")
			.optionalStart().appendPattern("XXXXX").optionalEnd()
            .parseDefaulting(ChronoField.HOUR_OF_DAY, 0)
            .parseDefaulting(ChronoField.MINUTE_OF_HOUR, 0)
            .parseDefaulting(ChronoField.SECOND_OF_MINUTE, 0)
            .parseDefaulting(ChronoField.OFFSET_SECONDS,OffsetDateTime.now().getLong(ChronoField.OFFSET_SECONDS) )
            .toFormatter();

	@Override
	public OffsetDateTime unmarshal(String inputDate) throws Exception {
		return OffsetDateTime.parse(inputDate, formatter);

	}

	@Override
	public String marshal(OffsetDateTime inputDate) throws Exception {
		if(inputDate != null) {
			return formatter.format(inputDate);
		} else {
			return null;
		}
	}

}