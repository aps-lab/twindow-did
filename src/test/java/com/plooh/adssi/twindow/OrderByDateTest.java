package com.plooh.adssi.twindow;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.text.ParseException;
import java.time.Instant;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.junit.jupiter.api.Test;

public class OrderByDateTest {

    @Test
    public void testOrderByDate() throws ParseException {
        DateTimeFormatter dtf = DateTimeFormatter.ISO_INSTANT;
        String date_1_String = "2020-11-12T10:12:00Z";
        Instant date_1 = Instant.parse(date_1_String);

        // Instant date_1 = DateUtils.parseDate("2020-11-12T10:12:00Z",
        // TWDateFormat.DATE_FORMAT);
        Instant date_2 = date_1.plus(1, ChronoUnit.SECONDS);
        String date_2_String = dtf.format(date_2);
        Instant date_3 = date_2.plus(1, ChronoUnit.HOURS);
        String date_3_String = dtf.format(date_3);
        Instant date_4 = date_3.plus(1, ChronoUnit.DAYS);
        String date_4_String = dtf.format(date_4);
        List<String> dateList = Arrays.asList(date_2_String, date_3_String, date_1_String, date_4_String);
        Collections.sort(dateList);
        assertEquals(date_1_String, dateList.get(0));
        assertEquals(date_2_String, dateList.get(1));
        assertEquals(date_3_String, dateList.get(2));
        assertEquals(date_4_String, dateList.get(3));
    }
}