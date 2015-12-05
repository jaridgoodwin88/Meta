package com.github.t1.meta2.reflection;

import java.util.*;

import com.github.t1.meta2.Mapping;

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
        protected Object get(B map) {
            return map.get(name);
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
