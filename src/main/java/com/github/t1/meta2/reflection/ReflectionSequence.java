package com.github.t1.meta2.reflection;

import static com.github.t1.meta2.util.JavaCast.*;

import java.util.function.Function;

import com.github.t1.meta2.AbstractSequence;
import com.github.t1.meta2.util.JavaCast;

public class ReflectionSequence<B> extends AbstractSequence<B> {
    public ReflectionSequence(Class<B> type, Function<B, B> backtrack) {
        super(backtrack,
                b -> new ReflectionSequence<>(type, b),
                b -> new ReflectionMapping<>(type, b));
        this.type = type;
    }

    private final Class<?> type;

    @SuppressWarnings("unchecked")
    protected <T> T backtrack(B object, int i) {
        T sequence = (T) backtrack.apply(object);
        return getSequenceElement(sequence, i);
    }

    @Override
    protected <T> T cast(Object object, Class<T> type) {
        return JavaCast.cast(object, type);
    }
}
