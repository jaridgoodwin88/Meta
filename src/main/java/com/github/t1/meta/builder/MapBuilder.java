package com.github.t1.meta.builder;

import lombok.Value;

import java.util.*;

@Value
class MapBuilder implements Builder<Map<String, Object>> {
    private final Map<String, Object> map = new LinkedHashMap<>();

    @Override public void set(Object key, Object value) {
        map.put(key.toString(), value);
    }

    @Override public Map<String, Object> build() {
        return map;
    }
}
