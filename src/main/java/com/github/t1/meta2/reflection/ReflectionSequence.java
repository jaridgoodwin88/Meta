package com.github.t1.meta2.reflection;

import static com.github.t1.meta2.util.JavaCast.*;

import java.util.Optional;
import java.util.function.Function;

import com.github.t1.meta2.*;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class ReflectionSequence<B> implements Sequence<B> {
    private final Function<B, B> backtrack;

    @Override
    public Mapping<B> getMapping(int i) {
        return null; // TODO test and implement
    }

    @Override
    public Sequence<B> getSequence(int i) {
        return new ReflectionSequence<>(object -> backtrack(object, i));
    }

    @Override
    public Scalar<B> getScalar(int i) {
        return new Scalar<B>() {
            @Override
            public <T> Optional<T> get(B object, Class<T> type) {
                return Optional.ofNullable(cast(backtrack(object, i), type));
            }
        };
    }

    @SuppressWarnings("unchecked")
    private <T> T backtrack(B object, int i) {
        T sequence = (T) backtrack.apply(object);
        return getSequenceElement(sequence, i);
    }
}
