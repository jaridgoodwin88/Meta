package com.github.t1.meta2.reflection;

import static java.util.Collections.*;
import static java.util.function.Function.*;

import java.lang.reflect.Field;
import java.util.*;
import java.util.function.Function;

import com.github.t1.meta2.Mapping;

import lombok.*;

public class ReflectionMapping<B> implements Mapping<B> {
    @SuppressWarnings("unchecked")
    public static <B> ReflectionMapping<B> on(Class<B> type) {
        return new ReflectionMapping<>(type, (Function<B, Object>) identity());
    }

    private final Map<String, FieldReflectionProperty<B>> properties;

    private ReflectionMapping(Class<?> type, Function<B, Object> backtrack) {
        this.properties = new LinkedHashMap<>();
        for (Field field : type.getDeclaredFields())
            properties.put(field.getName(), new FieldReflectionProperty<>(field, backtrack));
    }

    @ToString
    private static class FieldReflectionProperty<B> extends ObjectProperty<B> {
        private final Field field;
        private final Function<B, Object> backtrack;

        public FieldReflectionProperty(Field field, Function<B, Object> backtrack) {
            super(field.getType(), field.getName());
            this.field = field;
            this.field.setAccessible(true);
            this.backtrack = backtrack;
        }

        @Override
        public String getName() {
            return field.getName();
        }

        @Override
        @SneakyThrows(IllegalAccessException.class)
        protected Object get(B object) {
            return field.get(backtrack.apply(object));
        }

        @Override
        protected Mapping<B> createMapping() {
            return new ReflectionMapping<>(field.getType(), this::get);
        }
    }

    @Override
    public Property<B> getProperty(String name) {
        return properties.get(name);
    }

    @Override
    public List<Property<B>> getProperties() {
        return unmodifiableList(new ArrayList<>(properties.values()));
    }

}
