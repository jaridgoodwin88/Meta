package com.github.t1.meta2;

import com.github.t1.meta2.util.JavaCast;
import com.github.t1.meta2.util.OptionalExtension;

import java.lang.reflect.Field;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;

import static com.github.t1.meta2.Structure.Kind.*;
import static com.github.t1.meta2.util.JavaCast.PRIMITIVE_SCALARS;
import static com.github.t1.meta2.util.JavaCast.PRIMITIVE_WRAPPER_SCALARS;
import static com.github.t1.meta2.util.OptionalExtension.stream;
import static java.util.Arrays.asList;
import static java.util.function.Function.identity;
import static java.util.stream.Collectors.toList;

public class MetaReflection {
    public static <T> Mapping<T> mapping(Class<T> type) {
        return new ReflectionMapping<>(type, Structure.Path.ROOT, identity());
    }

    private static class ReflectionSequence<B> extends AbstractContainer<B, Integer>
            implements Sequence<B> {
        public ReflectionSequence(Class<B> type, Structure.Path path, Function<Optional<B>, Optional<B>> backtrack) {
            super(path,
                    backtrack,
                    JavaCast::cast,
                    (p, b) -> new ReflectionSequence<>(type, p, b),
                    (p, b) -> new ReflectionMapping<>(type, p, b),
                    OptionalExtension::getSequenceElement);
        }

        @Override
        @SuppressWarnings("unchecked")
        public <T> List<T> get(B object, Class<T> elementType) {
            return stream(backtrack.apply(Optional.of(object)))
                    .map(OptionalExtension::toList)
                    .flatMap(Collection::stream)
                    .map(element -> JavaCast.cast(element, elementType))
                    .collect(toList());
        }

        @Override
        public int size(B object) {
            return get(object, Object.class).size();
        }
    }

    private static class ReflectionMapping<B> extends AbstractContainer<B, String> implements Mapping<B> {
        private final Class<B> type;
        private List<Structure.Property<B>> properties;

        public ReflectionMapping(Class<B> type, Structure.Path path, Function<Optional<B>, Optional<B>> backtrack) {
            super(path, backtrack,
                    JavaCast::cast,
                    (p, b) -> new ReflectionSequence<>(type, p, b),
                    null,
                    (optional, name) -> optional.flatMap(object -> get(object, type, name)));
            this.type = type;
        }

        @SuppressWarnings("unchecked")
        private static <B> Optional<B> get(B object, Class<B> type, String name) {
            return getField(type, name).map((Field field) -> {
                try {
                    return (B) field.get(object);
                } catch (IllegalAccessException e) {
                    throw new RuntimeException(e);
                }
            });
        }

        @Override
        public Mapping<B> getMapping(String name) {
            @SuppressWarnings("unchecked")
            Class<B> fieldType = (Class<B>) getField(name).get().getType();
            return new ReflectionMapping<>(fieldType, path.with(name), object -> resolve(object, name));
        }

        private Optional<Field> getField(String name) {
            return getField(type, name);
        }

        private static Optional<Field> getField(Class<?> declaringType, String name) {
            try {
                Field field = declaringType.getDeclaredField(name);
                field.setAccessible(true);
                return Optional.of(field);
            } catch (NoSuchFieldException e) {
                return Optional.empty();
            }
        }

        @Override
        public List<Structure.Property<B>> getProperties() {
            if (properties == null) {
                properties = asList(type.getDeclaredFields()).stream()
                        .map(ReflectionProperty::new)
                        .collect(toList());
            }
            return properties;
        }

        private final class ReflectionProperty extends Structure.Property<B> {
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
        }
    }
}
