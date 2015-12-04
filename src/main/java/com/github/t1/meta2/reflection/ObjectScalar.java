package com.github.t1.meta2.reflection;

import java.util.Optional;

import com.github.t1.meta2.Scalar;

public abstract class ObjectScalar<B> implements Scalar<B> {
    @Override
    public final <T> Optional<T> get(B object, Class<T> type) {
        return Optional.ofNullable(get(object)).map(value -> cast(value, type));
    }

    private <T> T cast(Object value, Class<T> type) {
        if (CharSequence.class.isAssignableFrom(type))
            value = value.toString();
        return type.cast(value);
    }

    protected abstract Object get(B object);
}
