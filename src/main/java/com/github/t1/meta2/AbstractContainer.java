package com.github.t1.meta2;

import java.util.Optional;
import java.util.function.*;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public abstract class AbstractContainer<B, N> implements Container<B, N> {
    protected final Function<B, B> backtrack;
    protected final BiFunction<Object, Class<?>, ?> cast;
    protected final Function<Function<B, B>, Sequence<B>> sequenceFactory;
    protected final Function<Function<B, B>, Mapping<B>> mappingFactory;
    protected final BiFunction<B, N, B> resolve;

    @Override
    public Scalar<B> getScalar(N i) {
        return new Scalar<B>() {
            @Override
            public <T> Optional<T> get(B object, Class<T> type) {
                return Optional.ofNullable(cast(resolve(object, i), type));
            }

            private <T> T cast(B backtracked, Class<T> type) {
                return type.cast(cast.apply(backtracked, type));
            }
        };
    }

    @Override
    public Sequence<B> getSequence(N i) {
        return sequenceFactory.apply(object -> resolve(object, i));
    }

    @Override
    public Mapping<B> getMapping(N i) {
        return mappingFactory.apply(object -> resolve(object, i));
    }

    protected B resolve(B object, N i) {
        object = backtrack.apply(object);
        return resolve.apply(object, i);
    }
}
