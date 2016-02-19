package com.github.t1.meta2.reflection;

import static com.github.t1.meta2.util.JavaCast.*;
import static java.util.function.Function.*;

import java.lang.reflect.Field;
import java.util.function.Function;

import com.github.t1.meta2.*;
import com.github.t1.meta2.util.JavaCast;

import lombok.SneakyThrows;

public class ReflectionMeta {
    public static <T> Mapping<T> mapping(Class<T> type) {
        return new ReflectionMapping<>(type, identity());
    }

    private static class ReflectionSequence<B> extends MetaFactory.AbstractSequence<B> {
        public ReflectionSequence(Class<B> type, Function<B, B> backtrack) {
            super(backtrack,
                    JavaCast::cast,
                    b -> new ReflectionSequence<>(type, b),
                    b -> new ReflectionMapping<>(type, b));
            this.resolve = (object, i) -> {
                Object sequence = backtrack.apply(object);
                return getSequenceElement(sequence, i);
            };
        }
    }

    private static class ReflectionMapping<B> extends MetaFactory.AbstractMapping<B> {
        public ReflectionMapping(Class<B> type, Function<B, B> backtrack) {
            super(backtrack,
                    JavaCast::cast,
                    b -> new ReflectionSequence<>(type, b),
                    b -> new ReflectionMapping<>(type, b));
            this.resolve = this::backtrack;
            this.type = type;
        }

        private final Class<?> type;

        @SuppressWarnings("unchecked")
        @SneakyThrows(ReflectiveOperationException.class)
        protected <T> T backtrack(B object, String name) {
            return (T) getField(name).get(backtrack.apply(object));
        }

        private Field getField(String name) throws NoSuchFieldException {
            Field field = type.getDeclaredField(name);
            field.setAccessible(true);
            return field;
        }
    }
}
