package com.github.t1.meta2.reflection;

import static java.util.Arrays.*;

import java.lang.reflect.Array;
import java.util.*;
import java.util.function.Function;

import com.github.t1.meta2.*;
import com.github.t1.meta2.Mapping.Property;

import lombok.*;

public abstract class ObjectProperty<B> implements Property<B> {
    public static final List<Class<?>> PRIMITIVE_WRAPPER_SCALARS =
            asList(Boolean.class, Character.class, Byte.class, Short.class,
                    Integer.class, Long.class, Float.class, Double.class);
    public static final List<Class<?>> PRIMITIVE_SCALARS = asList(boolean.class, char.class, byte.class, short.class,
            int.class, long.class, float.class, double.class);

    @RequiredArgsConstructor
    private static class ObjectGlue<B> implements Scalar<B>, Sequence<B> {
        private final Function<B, Object> get;

        @Override
        public final <T> Optional<T> get(B object, Class<T> type) {
            return Optional.ofNullable(get.apply(object)).map(value -> cast(value, type));
        }

        private <T> T cast(Object value, Class<T> type) {
            if (CharSequence.class.isAssignableFrom(type))
                value = value.toString();
            return type.cast(value);
        }

        @Override
        public int size(B object) {
            Object sequence = get.apply(object);
            if (sequence instanceof Collection)
                return ((Collection<?>) sequence).size();
            return Array.getLength(sequence);
        }
    }

    @Getter
    private final StructureKind kind;

    private Scalar<B> scalar;
    private Sequence<B> sequence;

    public ObjectProperty(Class<?> type) {
        this.kind = structure(type);
    }

    private StructureKind structure(Class<?> type) {
        if (CharSequence.class.isAssignableFrom(type) || PRIMITIVE_WRAPPER_SCALARS.contains(type)
                || PRIMITIVE_SCALARS.contains(type))
            return StructureKind.scalar;
        if (type.isArray() || Collection.class.isAssignableFrom(type))
            return StructureKind.sequence;
        return StructureKind.mapping;
    }

    @Override
    public final Scalar<B> getScalar() {
        if (getKind() != StructureKind.scalar)
            throw new IllegalStateException(this + " is a " + getKind() + ", not a scalar");
        if (this.scalar == null)
            this.scalar = createScalar();
        return scalar;
    }

    public Scalar<B> createScalar() {
        return new ObjectGlue<>(object -> get(object));
    }


    @Override
    public final Sequence<B> getSequence() {
        if (getKind() != StructureKind.sequence)
            throw new IllegalStateException(this + " is a " + getKind() + ", not a sequence");
        if (this.sequence == null)
            this.sequence = createSequence();
        return sequence;
    }

    public Sequence<B> createSequence() {
        return new ObjectGlue<>(object -> get(object));
    }

    protected abstract Object get(B object);
}
