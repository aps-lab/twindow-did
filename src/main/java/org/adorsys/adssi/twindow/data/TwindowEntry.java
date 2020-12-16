package org.adorsys.adssi.twindow.data;

import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
public class TwindowEntry {
    private String start;
    private String end;
    private String a_hash;
    private String a_closing;

    public TwindowEntry start(String start) {
        this.start = start;
        return this;
    }

    public TwindowEntry end(String end) {
        this.end = end;
        return this;
    }

    public TwindowEntry a_hash(String a_hash) {
        this.a_hash = a_hash;
        return this;
    }

    public TwindowEntry a_closing(String a_closing) {
        this.a_closing = a_closing;
        return this;
    }
}