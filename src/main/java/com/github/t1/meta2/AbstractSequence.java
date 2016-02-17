package com.github.t1.meta2;

import java.util.Optional;
import java.util.function.Function;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public abstract class AbstractSequence<B> implements Sequence<B> {
    protected final Function<B, B> backtrack;
    protected final Function<Function<B, B>, Sequence<B>> sequenceFactory;
    protected final Function<Function<B, B>, Mapping<B>> mappingFactory;

    @Override
    public Scalar<B> getScalar(int i) {
        return new Scalar<B>() {
            @Override
            public <T> Optional<T> get(B object, Class<T> type) {
                return Optional.ofNullable(cast(backtrack(object, i), type));
            }
        };
    }

    @Override
    public Sequence<B> getSequence(int i) {
        return sequenceFactory.apply(object -> backtrack(object, i));
    }

    @Override
    public Mapping<B> getMapping(int i) {
        return mappingFactory.apply(object -> backtrack(object, i));
    }

    protected abstract <T> T backtrack(B object, int i);

    protected abstract <T> T cast(Object object, Class<T> type);
}
