package com.github.t1.meta2.reflection;

import java.util.*;

import com.github.t1.meta2.*;

import lombok.*;

@AllArgsConstructor
public class CollectionsMapping implements Mapping {
    public static CollectionsMapping of(Map<?, ?> map) {
        return new CollectionsMapping(map);
    }

    private Map<?, ?> map;

    @Override
    public Scalar getScalar(String name) {
        if (!map.containsKey(name))
            throw new IllegalArgumentException("property " + name + " not found in map");
        return new StringScalar(name);
    }

    @RequiredArgsConstructor
    public class StringScalar implements Scalar {
        @Getter
        private final String name;
        @Getter
        private Optional<String> stringValue;

        @Override
        public StringScalar attach(Object object) {
            @SuppressWarnings("unchecked")
            Map<String, String> map = (Map<String, String>) object;
            this.stringValue = Optional.ofNullable(map.get(name));
            return this;
        }

    }

}
