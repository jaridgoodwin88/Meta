package com.github.t1.meta2;

import java.util.Optional;
import java.util.function.*;

import lombok.RequiredArgsConstructor;

public class MetaFactory {
    @RequiredArgsConstructor
    public abstract static class AbstractSequence<B> implements Sequence<B> {
        protected final Function<B, B> backtrack;
        protected final BiFunction<Object, Class<?>, ?> cast;
        protected final Function<Function<B, B>, Sequence<B>> sequenceFactory;
        protected final Function<Function<B, B>, Mapping<B>> mappingFactory;
        protected BiFunction<B, Integer, B> resolve;

        @Override
        public Scalar<B> getScalar(int i) {
            return new Scalar<B>() {
                @Override
                public <T> Optional<T> get(B object, Class<T> type) {
                    return Optional.ofNullable(cast(resolve.apply(object, i), type));
                }

                private <T> T cast(B backtracked, Class<T> type) {
                    return type.cast(cast.apply(backtracked, type));
                }
            };
        }

        @Override
        public Sequence<B> getSequence(int i) {
            return sequenceFactory.apply(object -> resolve.apply(object, i));
        }

        @Override
        public Mapping<B> getMapping(int i) {
            return mappingFactory.apply(object -> resolve.apply(object, i));
        }
    }

    @RequiredArgsConstructor
    public abstract static class AbstractMapping<B> implements Mapping<B> {
        protected final Function<B, B> backtrack;
        protected final BiFunction<Object, Class<?>, ?> cast;
        protected final Function<Function<B, B>, Sequence<B>> sequenceFactory;
        protected final Function<Function<B, B>, Mapping<B>> mappingFactory;
        protected BiFunction<B, String, B> resolve;

        @Override
        public Scalar<B> getScalar(String name) {
            return new Scalar<B>() {
                @Override
                public <T> Optional<T> get(B object, Class<T> type) {
                    return Optional.ofNullable(cast(resolve.apply(object, name), type));
                }

                private <T> T cast(B backtracked, Class<T> type) {
                    return type.cast(cast.apply(backtracked, type));
                }
            };
        }

        @Override
        public Sequence<B> getSequence(String name) {
            return sequenceFactory.apply(object -> resolve.apply(object, name));
        }

        @Override
        public Mapping<B> getMapping(String name) {
            return mappingFactory.apply(object -> resolve.apply(object, name));
        }
    }
}
