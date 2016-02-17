package com.github.t1.meta2.reflection;

import static java.util.function.Function.*;

import java.lang.reflect.Field;
import java.util.function.Function;

import com.github.t1.meta2.AbstractMapping;
import com.github.t1.meta2.util.JavaCast;

import lombok.SneakyThrows;

public class ReflectionMapping<B> extends AbstractMapping<B> {
    public static <B> ReflectionMapping<B> on(Class<B> type) {
        return new ReflectionMapping<>(type);
    }

    public ReflectionMapping(Class<B> type) {
        this(type, identity());
    }

    public ReflectionMapping(Class<B> type, Function<B, B> backtrack) {
        super(backtrack,
                b -> new ReflectionSequence<>(type, b),
                b -> new ReflectionMapping<>(type, b));
        this.type = type;
    }

    private final Class<?> type;

    @Override
    @SuppressWarnings("unchecked")
    @SneakyThrows(ReflectiveOperationException.class)
    protected <T> T backtrack(B object, String name) {
        return (T) getField(name).get(backtrack.apply(object));
    }

    @SneakyThrows(ReflectiveOperationException.class)
    private Field getField(String name) {
        Field field = type.getDeclaredField(name);
        field.setAccessible(true);
        return field;
    }

    @Override
    protected <T> T cast(Object object, Class<T> type) {
        return JavaCast.cast(object, type);
    }
}
