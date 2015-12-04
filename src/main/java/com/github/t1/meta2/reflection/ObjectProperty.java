package com.github.t1.meta2.reflection;

import static java.util.Arrays.*;

import java.util.List;

import com.github.t1.meta2.*;
import com.github.t1.meta2.Mapping.Property;

import lombok.Getter;

public abstract class ObjectProperty<B> implements Property<B> {
    public static final List<Class<?>> PRIMITIVE_WRAPPER_SCALARS =
            asList(Boolean.class, Character.class, Byte.class, Short.class,
                    Integer.class, Long.class, Float.class, Double.class);
    public static final List<Class<?>> PRIMITIVE_SCALARS = asList(boolean.class, char.class, byte.class, short.class,
            int.class, long.class, float.class, double.class);

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
        if (type.isArray())
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

    protected abstract Scalar<B> createScalar();

    @Override
    public final Sequence<B> getSequence() {
        if (getKind() != StructureKind.sequence)
            throw new IllegalStateException(this + " is a " + getKind() + ", not a sequence");
        if (this.sequence == null)
            this.sequence = createSequence();
        return sequence;
    }

    protected abstract Sequence<B> createSequence();
}
