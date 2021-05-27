package com.plooh.adssi.twindow.utils;

import com.plooh.adssi.twindow.data.TwindowEntry;
import java.time.Instant;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;

public class TwindowUtils {
    public static final DateTimeFormatter DTF = DateTimeFormatter.ISO_INSTANT;

    public static final TwindowEntry twindow(Instant now, String antecedentHash, String antecedentClosing) {
        return twindow(now).a_hash(antecedentHash).a_closing(antecedentClosing);
    }

    public static final TwindowEntry twindow(Instant now) {
        Instant start = now.truncatedTo(ChronoUnit.HOURS);
        Instant end = start.plus(1, ChronoUnit.HOURS);
        String startFormated = TwindowUtils.DTF.format(start);
        String endFormated = TwindowUtils.DTF.format(end);
        return new TwindowEntry().start(startFormated).end(endFormated);
    }

    public static final Instant openingTwindowRecordDate(Instant now) {
        return now.truncatedTo(ChronoUnit.HOURS);
    }

    public static final Instant closingTwindowRecordDate(Instant now) {
        return now.truncatedTo(ChronoUnit.HOURS).plus(1, ChronoUnit.HOURS).minus(1, ChronoUnit.SECONDS);
    }

    public static final TwindowEntry antecedantTwindow(String antecedantClosing) {
        Instant start = Instant.parse(antecedantClosing).minus(1, ChronoUnit.HOURS);
        String startFormated = TwindowUtils.DTF.format(start);
        return new TwindowEntry().start(startFormated).end(antecedantClosing);
    }
}