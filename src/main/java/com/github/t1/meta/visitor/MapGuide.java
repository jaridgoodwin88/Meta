package com.github.t1.meta.visitor;

import com.github.t1.meta.Property;
import lombok.RequiredArgsConstructor;
import lombok.Value;

import java.util.Map;
import java.util.stream.Stream;

@RequiredArgsConstructor
class MapGuide extends MappingGuide {
    private final Map<?, ?> map;

    @Override protected Stream<Property> getProperties() {
        return map.entrySet().stream().map(MapProperty::new);
    }

    @Value
    private class MapProperty implements Property {
        private final Map.Entry<?, ?> entry;

        @Override public Object getName() {
            return entry.getKey();
        }

        @Override public Object getValue() {
            return entry.getValue();
        }
    }
}
