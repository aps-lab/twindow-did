package org.adorsys.adssi.twindow.utils;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class MapBuilder {
    private final Map<String, Object> map;

    private MapBuilder() {
        this.map = new HashMap<>();
    }

    public static MapBuilder m() {
        return new MapBuilder();
    }

    public MapBuilder w(final String key, final Object value) {
        map.put(key, value);
        return this;
    }

    public MapBuilder w(final Map<String, Object> src) {
        if (src != null)
            map.putAll(src);
        return this;
    }

    public Map<String, Object> build() {
        return Collections.unmodifiableMap(map);
    }
}