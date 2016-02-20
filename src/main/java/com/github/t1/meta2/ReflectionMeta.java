package com.github.t1.meta2;

import static com.github.t1.meta2.Structure.Kind.mapping;
import static com.github.t1.meta2.Structure.Kind.*;
import static com.github.t1.meta2.util.JavaCast.*;
import static java.util.Arrays.*;
import static java.util.function.Function.*;
import static java.util.stream.Collectors.*;

import java.lang.reflect.Field;
import java.util.List;
import java.util.function.Function;

import com.github.t1.meta2.util.JavaCast;

import lombok.SneakyThrows;

public class ReflectionMeta {
    public static <T> Mapping<T> mapping(Class<T> type) {
        return new ReflectionMapping<>(type, identity());
    }

    private static class ReflectionSequence<B> extends AbstractContainer<B, Integer>
            implements Sequence<B> {
        public ReflectionSequence(Class<B> type, Function<B, B> backtrack) {
            super(backtrack,
                    JavaCast::cast,
                    b -> new ReflectionSequence<>(type, b),
                    b -> new ReflectionMapping<>(type, b),
                    JavaCast::getSequenceElement);
        }

        @Override
        public List<Structure.Element<B>> getItems() {
            return null;
        }
    }

    private static class ReflectionMapping<B> extends AbstractContainer<B, String> implements Mapping<B> {
        private final Class<B> type;
        private List<Structure.Property<B>> properties;

        public ReflectionMapping(Class<B> type, Function<B, B> backtrack) {
            super(backtrack,
                    JavaCast::cast,
                    b -> new ReflectionSequence<>(type, b),
                    null,
                    (object, name) -> get(object, type, name));
            this.type = type;
        }

        @SuppressWarnings("unchecked")
        @SneakyThrows(ReflectiveOperationException.class)
        private static <B> B get(B object, Class<B> type, String name) {
            return (B) getField(type, name).get(object);
        }

        @Override
        public Mapping<B> getMapping(String name) {
            @SuppressWarnings("unchecked")
            Class<B> fieldType = (Class<B>) getField(name).getType();
            return new ReflectionMapping<>(fieldType, object -> resolve(object, name));
        }

        private Field getField(String name) {
            return getField(type, name);
        }

        @SneakyThrows(NoSuchFieldException.class)
        private static Field getField(Class<?> type, String name) {
            Field field = type.getDeclaredField(name);
            field.setAccessible(true);
            return field;
        }

        @Override
        public List<Structure.Property<B>> getItems() {
            if (properties == null) {
                properties = asList(type.getDeclaredFields()).stream()
                        .map(ReflectionProperty::new)
                        .collect(toList());
            }
            return properties;
        }

        private class ReflectionProperty implements Structure.Property<B> {
            private final Field field;

            private ReflectionProperty(Field field) {
                this.field = field;
                this.field.setAccessible(true);
            }

            @Override
            public String getKey() {
                return field.getName();
            }

            @Override
            public Structure.Kind getKind() {
                if (isSequence())
                    return sequence;
                if (isScalar())
                    return scalar;
                return mapping;
            }

            private boolean isSequence() {
                return List.class.isAssignableFrom(field.getType()) || field.getType().isArray();
            }

            private boolean isScalar() {
                return PRIMITIVE_WRAPPER_SCALARS.contains(field.getType())
                        || PRIMITIVE_SCALARS.contains(field.getType())
                        || CharSequence.class.isAssignableFrom(field.getType());
            }

            @Override
            public String toString() {
                return getKind() + " " + getKey();
            }
        }
    }
}
