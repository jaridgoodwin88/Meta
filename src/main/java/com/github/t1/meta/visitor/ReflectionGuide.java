package com.github.t1.meta.visitor;

import com.github.t1.stereotypes.Annotations;
import lombok.*;

import java.lang.annotation.Annotation;
import java.lang.reflect.*;
import java.util.ArrayList;
import java.util.*;
import java.util.stream.Stream;

import static java.util.Arrays.*;

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
        return asList(type.getDeclaredFields()).stream();
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

        private AnnotatedElement annotations() {
            return Annotations.on(field);
        }

        @SneakyThrows(IllegalAccessException.class)
        @Override public Object getValue() {
            field.setAccessible(true);
            return field.get(object);
        }

        @Override public <T extends Annotation> T getAnnotation(Class<T> annotationClass) {
            return annotations().getAnnotation(annotationClass);
        }

        @Override public Annotation[] getAnnotations() {
            return annotations().getAnnotations();
        }

        @Override public Annotation[] getDeclaredAnnotations() {
            return annotations().getDeclaredAnnotations();
        }

        @Override public boolean isAnnotationPresent(Class<? extends Annotation> annotationClass) {
            return annotations().isAnnotationPresent(annotationClass);
        }

        @Override public <T extends Annotation> T[] getAnnotationsByType(Class<T> annotationClass) {
            return annotations().getAnnotationsByType(annotationClass);
        }

        @Override public <T extends Annotation> T getDeclaredAnnotation(Class<T> annotationClass) {
            return annotations().getDeclaredAnnotation(annotationClass);
        }

        @Override public <T extends Annotation> T[] getDeclaredAnnotationsByType(Class<T> annotationClass) {
            return annotations().getDeclaredAnnotationsByType(annotationClass);
        }

        @Override public String toString() {
            return "Property[" + field.getName() + "]" + asList(getAnnotations());
        }
    }
}
