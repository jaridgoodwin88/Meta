package com.github.t1.meta2;

import java.util.Optional;
import java.util.function.Function;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public abstract class AbstractMapping<B> implements Mapping<B> {
    protected final Function<B, B> backtrack;
    protected final Function<Function<B, B>, Sequence<B>> sequenceFactory;
    protected final Function<Function<B, B>, Mapping<B>> mappingFactory;

    @Override
    public Scalar<B> getScalar(String name) {
        return new Scalar<B>() {
            @Override
            public <T> Optional<T> get(B object, Class<T> type) {
                return Optional.ofNullable(cast(backtrack(object, name), type));
            }
        };
    }

    @Override
    public Sequence<B> getSequence(String name) {
        return sequenceFactory.apply(object -> backtrack(object, name));
    }

    @Override
    public Mapping<B> getMapping(String name) {
        return mappingFactory.apply(object -> backtrack(object, name));
    }

    protected abstract <T> T backtrack(B object, String name);

    protected abstract <T> T cast(Object object, Class<T> type);
}
