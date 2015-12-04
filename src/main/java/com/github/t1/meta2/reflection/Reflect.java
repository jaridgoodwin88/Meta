package com.github.t1.meta2.reflection;

import java.lang.reflect.Field;
import java.util.*;

import com.github.t1.meta2.*;

import lombok.RequiredArgsConstructor;

public class Reflect implements Mapping {
    public static Reflect on(Object object) {
        return on(object.getClass());
    }

    public static Reflect on(Class<?> type) {
        return new Reflect(type);
    }

    private final Map<String, FieldReflectionProperty> properties;

    private Reflect(Class<?> type) {
        this.properties = new LinkedHashMap<>();
        for (Field field : type.getDeclaredFields())
            properties.put(field.getName(), new FieldReflectionProperty(field));
    }

    @RequiredArgsConstructor
    private static class FieldReflectionProperty implements Property {
        final Field field;

        @Override
        public String getName() {
            return field.getName();
        }

        @Override
        public Scalar getScalarValue() {
            return new FieldReflectionScalar(field);
        }
    }

    @Override
    public Property getProperty(String name) {
        return properties.get(name);
    }

    @Override
    public List<Property> getProperties() {
        return new ArrayList<>(properties.values());
    }

}
