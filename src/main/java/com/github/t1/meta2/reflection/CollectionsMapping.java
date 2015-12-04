package com.github.t1.meta2.reflection;

import java.lang.reflect.Array;
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
            properties.put(name, new MapProperty<>(map.get(name).getClass(), name));
    }

    @ToString
    private static class MapProperty<B extends Map<String, ?>> extends ObjectProperty<B> {
        @Getter
        private final String name;

        public MapProperty(Class<?> type, String name) {
            super(type);
            this.name = name;
        }

        @Override
        public Scalar<B> createScalar() {
            return new MapScalar<>(name);
        }

        @Override
        public Sequence<B> createSequence() {
            return new MapSequence<>(name);
        }
    }

    @RequiredArgsConstructor
    private static class MapScalar<B extends Map<String, ?>> extends ObjectScalar<B> {
        @Getter
        private final String name;

        @Override
        protected Object get(B map) {
            return map.get(name);
        }
    }

    @RequiredArgsConstructor
    private static class MapSequence<B extends Map<String, ?>> implements Sequence<B> {
        @Getter
        private final String name;

        @Override
        public int size(B map) {
            return Array.getLength(map.get(name));
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
