package org.rutebanken.util;

import java.time.OffsetDateTime;
import java.time.OffsetTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.time.temporal.ChronoField;

import javax.xml.bind.annotation.adapters.XmlAdapter;

public class OffsetTimeISO8601XmlAdapter extends XmlAdapter<String, OffsetTime> {

	private final static DateTimeFormatter formatter = new DateTimeFormatterBuilder().appendPattern("HH:mm:ss")
			.optionalStart().appendFraction(ChronoField.MILLI_OF_SECOND, 0, 3, true).optionalEnd()
			.optionalStart().appendPattern("XXXXX")
            .optionalEnd()
			
//
	.parseDefaulting(ChronoField.OFFSET_SECONDS,OffsetDateTime.now().getLong(ChronoField.OFFSET_SECONDS) ).toFormatter();

	@Override
	public OffsetTime unmarshal(String inputDate) throws Exception {
		return OffsetTime.parse(inputDate, formatter);

	}

	@Override
	public String marshal(OffsetTime inputDate) throws Exception {
		if(inputDate != null) {
			return formatter.format(inputDate);
		} else {
			return null;
		}
	}

}
