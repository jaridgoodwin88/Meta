package com.github.t1.meta2.reflection;

import java.util.*;

import com.github.t1.meta2.*;

import lombok.*;

public class CollectionsMapping implements Mapping {
    public static CollectionsMapping of(Map<String, ?> map) {
        return new CollectionsMapping(map);
    }

    private final Map<String, Property> properties;

    private CollectionsMapping(Map<String, ?> map) {
        this.properties = new LinkedHashMap<>();
        for (String name : map.keySet()) {
            properties.put(name, new Mapping.Property() {
                @Override
                public String getName() {
                    return name;
                }

                @Override
                public Scalar getScalarValue() {
                    return new StringScalar(name);
                }
            });
        }
    }

    @Override
    public Scalar getScalar(String name) {
        if (!properties.containsKey(name))
            throw new IllegalArgumentException("property " + name + " not found in map");
        return new StringScalar(name);
    }

    @RequiredArgsConstructor
    public class StringScalar implements Scalar {
        @Getter
        private final String name;

        @Override
        public Optional<String> getStringValue(Object object) {
            @SuppressWarnings("unchecked")
            Map<String, Object> map = (Map<String, Object>) object;
            return Optional.ofNullable(map.get(name)).map(value -> value.toString());
        }

    }

    @Override
    public List<Property> getProperties() {
        return new ArrayList<>(properties.values());
    }

    @Override
    public Property getProperty(String name) {
        return properties.get(name);
    }

}
