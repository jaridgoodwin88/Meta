package com.github.t1.meta2;

import java.util.Optional;
import java.util.function.*;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public abstract class AbstractContainer<B, K> implements Container<B, K> {
    protected final Function<B, B> backtrack;
    protected final BiFunction<Object, Class<?>, ?> cast;
    protected final Function<Function<B, B>, Sequence<B>> sequenceFactory;
    protected final Function<Function<B, B>, Mapping<B>> mappingFactory;
    protected final BiFunction<B, K, B> resolve;

    @Override
    public Scalar<B> getScalar(K key) {
        return new Scalar<B>() {
            @Override
            public <T> Optional<T> get(B object, Class<T> type) {
                return Optional.ofNullable(cast(resolve(object, key), type));
            }

            private <T> T cast(B backtracked, Class<T> type) {
                return type.cast(cast.apply(backtracked, type));
            }
        };
    }

    @Override
    public Sequence<B> getSequence(K key) {
        return sequenceFactory.apply(object -> resolve(object, key));
    }

    @Override
    public Mapping<B> getMapping(K key) {
        return mappingFactory.apply(object -> resolve(object, key));
    }

    protected B resolve(B object, K key) {
        object = backtrack.apply(object);
        return resolve.apply(object, key);
    }
}
