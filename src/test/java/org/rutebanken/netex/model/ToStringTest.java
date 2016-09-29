package org.rutebanken.netex.model;

import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class ToStringTest {
    @Test
    public void toStringMethod() {
        StopPlace stopPlace = new StopPlace()
                .withName(new MultilingualString().withValue("berger"));
        assertThat(stopPlace.toString()).contains("berger");
    }
}
