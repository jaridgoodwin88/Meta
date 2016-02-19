package com.github.t1.meta2.reflection;

import static java.util.function.Function.*;

import java.lang.reflect.Field;
import java.util.function.*;

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
                    b -> new ReflectionMapping<>(type, b),
                    JavaCast::getSequenceElement);
        }
    }

    private static class ReflectionMapping<B> extends MetaFactory.AbstractMapping<B> {
        public ReflectionMapping(Class<B> type, Function<B, B> backtrack) {
            super(backtrack,
                    JavaCast::cast,
                    b -> new ReflectionSequence<>(type, b),
                    b -> new ReflectionMapping<>(type, b),
                    new BiFunction<B, String, B>() {
                        @Override
                        @SuppressWarnings("unchecked")
                        @SneakyThrows(ReflectiveOperationException.class)
                        public B apply(B object, String name) {
                            return (B) getField(name).get(object);
                        }

                        private Field getField(String name) throws NoSuchFieldException {
                            Field field = type.getDeclaredField(name);
                            field.setAccessible(true);
                            return field;
                        }
                    });
        }
    }
}
