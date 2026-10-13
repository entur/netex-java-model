/*
 * Licensed under the EUPL, Version 1.2 or - as soon they will be approved by
 * the European Commission - subsequent versions of the EUPL (the "Licence");
 * You may not use this work except in compliance with the Licence.
 * You may obtain a copy of the Licence at:
 *
 *   https://joinup.ec.europa.eu/software/page/eupl
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the Licence is distributed on an "AS IS" basis,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the Licence for the specific language governing permissions and
 * limitations under the Licence.
 */

package org.rutebanken.util;

import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

class LocalDateXmlAdapterTest {

    private final LocalDateXmlAdapter adapter = new LocalDateXmlAdapter();

    @Test
    void testUnmarshalBasicDate() {
        LocalDate result = adapter.unmarshal("2026-08-31");
        assertEquals(LocalDate.of(2026, 8, 31), result);
    }

    @Test
    void testUnmarshalDateWithOffset() {
        LocalDate result = adapter.unmarshal("2026-05-03+02:00");
        assertEquals(LocalDate.of(2026, 5, 3), result);
    }

    @Test
    void testUnmarshalDateWithUtcDesignator() {
        LocalDate result = adapter.unmarshal("2026-05-03Z");
        assertEquals(LocalDate.of(2026, 5, 3), result);
    }

    @Test
    void testUnmarshalDateTimeIsRejected() {
        assertThrows(DateTimeParseException.class, () -> adapter.unmarshal("2026-05-03T00:00:00"));
    }

    @Test
    void testMarshalBasicDate() {
        String result = adapter.marshal(LocalDate.of(2026, 8, 31));
        assertEquals("2026-08-31", result);
    }

    @Test
    void testMarshalNull() {
        assertNull(adapter.marshal(null));
    }

    @Test
    void testRoundTrip() {
        LocalDate original = LocalDate.of(2026, 5, 3);
        assertEquals(original, adapter.unmarshal(adapter.marshal(original)));
    }
}
