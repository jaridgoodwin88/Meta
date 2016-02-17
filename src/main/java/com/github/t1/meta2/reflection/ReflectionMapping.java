package com.github.t1.meta2.reflection;

import static com.github.t1.meta2.util.JavaCast.*;
import static java.util.function.Function.*;

import java.lang.reflect.Field;
import java.util.Optional;
import java.util.function.Function;

import com.github.t1.meta2.*;

import lombok.SneakyThrows;

public class ReflectionMapping<B> implements Mapping<B> {
    @SuppressWarnings("unchecked")
    public static <B> ReflectionMapping<B> on(Class<B> type) {
        return new ReflectionMapping<>(type, (Function<B, Object>) identity());
    }

    private Class<?> type;
    private Function<B, Object> backtrack;

    private ReflectionMapping(Class<?> type, Function<B, Object> backtrack) {
        this.type = type;
        this.backtrack = backtrack;
    }

    @Override
    public Scalar<B> getScalar(String name) {
        return new Scalar<B>() {
            @Override
            public <T> Optional<T> get(B object, Class<T> type) {
                return Optional.of(cast(backtrack(object, name), type));
            }
        };
    }

    @Override
    public Sequence<B> getSequence(String name) {
        return new ReflectionSequence<>(object -> backtrack(object, name));
    }

    @Override
    public Mapping<B> getMapping(String name) {
        return new ReflectionMapping<>(type, object -> backtrack(object, name));
    }

    @SuppressWarnings("unchecked")
    @SneakyThrows(ReflectiveOperationException.class)
    private <T> T backtrack(Object object, String name) {
        return (T) getField(name).get(backtrack.apply((B) object));
    }

    @SneakyThrows(ReflectiveOperationException.class)
    private Field getField(String name) {
        Field field = type.getDeclaredField(name);
        field.setAccessible(true);
        return field;
    }
}
