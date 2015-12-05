package com.github.t1.meta2.reflection;

import java.lang.reflect.Field;
import java.util.*;

import com.github.t1.meta2.Mapping;

import lombok.*;

public class ReflectionMapping<B> implements Mapping<B> {
    public static <B> ReflectionMapping<B> on(Class<B> type) {
        return new ReflectionMapping<>(type);
    }

    private final Map<String, FieldReflectionProperty<B>> properties;

    private ReflectionMapping(Class<?> type) {
        this.properties = new LinkedHashMap<>();
        for (Field field : type.getDeclaredFields())
            properties.put(field.getName(), new FieldReflectionProperty<>(field));
    }

    @ToString
    private static class FieldReflectionProperty<B> extends ObjectProperty<B> {
        private final Field field;

        public FieldReflectionProperty(Field field) {
            super(field.getType());
            this.field = field;
            this.field.setAccessible(true);
        }

        @Override
        public String getName() {
            return field.getName();
        }

        @Override
        @SneakyThrows(IllegalAccessException.class)
        protected Object get(B object) {
            return field.get(object);
        }
    }

    @Override
    public Property<B> getProperty(String name) {
        return properties.get(name);
    }

    @Override
    public List<Property<B>> getProperties() {
        return new ArrayList<>(properties.values());
    }

}
