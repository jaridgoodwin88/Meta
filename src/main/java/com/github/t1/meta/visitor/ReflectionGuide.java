package com.github.t1.meta.visitor;

import com.github.t1.stereotypes.Annotations;
import lombok.*;

import java.lang.reflect.*;
import java.util.*;
import java.util.stream.Stream;

@RequiredArgsConstructor
class ReflectionGuide extends MappingGuide {
    private final Object object;

    @Override protected Stream<PropertyWithValue> getProperties() {
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
    private class ReflectionProperty implements PropertyWithValue {
        private final Field field;

        @Override public Object name() {
            return field.getName();
        }

        @Override public AnnotatedElement annotations() {
            return Annotations.on(field);
        }

        @SneakyThrows(IllegalAccessException.class)
        @Override public Object getValue() {
            field.setAccessible(true);
            return field.get(object);
        }
    }
}
