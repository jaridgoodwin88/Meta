package com.github.t1.meta2.reflection;

import static com.github.t1.meta2.StructureKind.*;

import java.util.*;

import com.github.t1.meta2.*;

import lombok.*;

public class CollectionsMapping<B extends Map<String, ?>> implements Mapping<B> {
    public static <B extends Map<String, ?>> CollectionsMapping<B> of(B map) {
        return new CollectionsMapping<>(map);
    }

    private final Map<String, Property<B>> properties;

    private CollectionsMapping(Map<String, ?> map) {
        this.properties = new LinkedHashMap<>();
        for (String name : map.keySet())
            properties.put(name, new MapProperty<>(name));
    }

    @Override
    public Scalar<B> getScalar(String name) {
        if (!properties.containsKey(name))
            throw new IllegalArgumentException("property " + name + " not found in map");
        return new StringScalar<>(name);
    }

    @RequiredArgsConstructor
    private static class MapProperty<B extends Map<String, ?>> implements Mapping.Property<B> {
        private final String name;

        @Override
        public String getName() {
            return name;
        }

        @Override
        public Scalar<B> getScalarValue() {
            return new StringScalar<>(name);
        }

        @Override
        public StructureKind getKind() {
            return scalar;
        }
    }

    @RequiredArgsConstructor
    private static class StringScalar<B extends Map<String, ?>> implements Scalar<B> {
        @Getter
        private final String name;

        @Override
        public Optional<String> getStringValue(B map) {
            return Optional.ofNullable(map.get(name)).map(value -> value.toString());
        }

    }

    @Override
    public List<Property<B>> getProperties() {
        return new ArrayList<>(properties.values());
    }

    @Override
    public Property<B> getProperty(String name) {
        return properties.get(name);
    }

}
