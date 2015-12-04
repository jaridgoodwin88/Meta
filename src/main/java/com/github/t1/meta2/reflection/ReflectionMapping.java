package com.github.t1.meta2.reflection;

import java.lang.reflect.*;
import java.util.*;

import com.github.t1.meta2.*;

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
        }

        @Override
        public String getName() {
            return field.getName();
        }

        @Override
        public Scalar<B> createScalar() {
            return new FieldReflectionScalar<>(field);
        }

        @Override
        public Sequence<B> createSequence() {
            return new FieldReflectionSequence<>(field);
        }
    }

    private static class FieldReflectionScalar<B> extends ObjectScalar<B> {
        private final Field field;

        public FieldReflectionScalar(Field field) {
            this.field = field;
            this.field.setAccessible(true);
        }

        @Override
        @SneakyThrows(ReflectiveOperationException.class)
        protected Object get(Object object) {
            return field.get(object);
        }
    }

    private static class FieldReflectionSequence<B> implements Sequence<B> {
        private final Field field;

        public FieldReflectionSequence(Field field) {
            this.field = field;
            this.field.setAccessible(true);
        }

        @Override
        @SneakyThrows(ReflectiveOperationException.class)
        public int size(B object) {
            return Array.getLength(field.get(object));
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
