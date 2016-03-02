package com.github.t1.meta.visitor;

import com.github.t1.meta.Property;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.Value;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Stream;

@RequiredArgsConstructor
class ReflectionGuide extends MappingGuide {
    private final Object object;

    @Override protected Stream<Property> getProperties() {
        return classHierarchy(object.getClass())
                .flatMap(this::fields)
                .filter(this::isProperty)
                .map(ReflectionProperty::new);
    }

    private Stream<Class<?>> classHierarchy(Class<?> start) {
        List<Class<?>> list = new ArrayList<>();
        for (Class<?> c = start; c.getSuperclass() != null; c = c.getSuperclass())
            list.add(c);
        Collections.reverse(list);
        return list.stream();
    }

    private Stream<Field> fields(Class<?> type) {
        return Arrays.asList(type.getDeclaredFields()).stream();
    }

    private boolean isProperty(Field field) {
        return !Modifier.isStatic(field.getModifiers())
                && !Modifier.isVolatile(field.getModifiers())
                && !field.isSynthetic();
    }

    @Value
    private class ReflectionProperty implements Property {
        private final Field field;

        @Override public Object getName() {
            return field.getName();
        }

        @SneakyThrows(IllegalAccessException.class)
        @Override public Object getValue() {
            field.setAccessible(true);
            return field.get(object);
        }
    }
}
