package com.github.t1.meta2.collections;

import static com.github.t1.meta2.util.JavaCast.*;

import java.util.Optional;
import java.util.function.Function;

import com.github.t1.meta2.*;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
class CollectionsSequence<B> implements Sequence<B> {
    private final Function<B, B> backtrack;

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
        return new CollectionsSequence<>(object -> backtrack(object, i));
    }

    @Override
    public Mapping<B> getMapping(int i) {
        return new CollectionsMapping<>(object -> backtrack(object, i));
    }

    private B backtrack(B object, int i) {
        B sequence = backtrack.apply(object);
        return getSequenceElement(sequence, i);
    }

}
