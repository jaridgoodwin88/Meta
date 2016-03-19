package com.github.t1.meta.visitor;

import lombok.*;

import java.lang.annotation.Annotation;
import java.lang.reflect.AnnotatedElement;
import java.util.Map;
import java.util.stream.Stream;

@RequiredArgsConstructor
class MapGuide extends MappingGuide {
    private final Map<?, ?> map;

    @Override protected Stream<PropertyWithValue> getProperties() {
        return map.entrySet().stream().map(MapProperty::new);
    }

    @Value
    private class MapProperty implements PropertyWithValue {
        private final Map.Entry<?, ?> entry;

        @Override public Object name() {
            return entry.getKey();
        }

        @Override public AnnotatedElement annotations() {
            return new EmptyAnnotations();
        }

        @Override public Object getValue() {
            return entry.getValue();
        }
    }

    private class EmptyAnnotations implements AnnotatedElement {
        @Override public <T extends Annotation> T getAnnotation(Class<T> annotationClass) {
            return null;
        }

        @Override public Annotation[] getAnnotations() {
            return new Annotation[0];
        }

        @Override public Annotation[] getDeclaredAnnotations() {
            return new Annotation[0];
        }

        @Override public String toString() {
            return "[]";
        }
    }
}
