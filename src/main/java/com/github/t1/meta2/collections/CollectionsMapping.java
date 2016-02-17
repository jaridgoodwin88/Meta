package com.github.t1.meta2.collections;

import static com.github.t1.meta2.util.JavaCast.*;
import static java.util.function.Function.*;

import java.util.*;
import java.util.function.Function;

import com.github.t1.meta2.*;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class CollectionsMapping<B> implements Mapping<B> {
    public CollectionsMapping() {
        this(identity());
    }

    private final Function<B, B> backtrack;

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
        return new CollectionsSequence<>(object -> backtrack(object, name));
    }

    @Override
    public Mapping<B> getMapping(String name) {
        return new CollectionsMapping<>(object -> backtrack(object, name));
    }

    @SuppressWarnings("unchecked")
    private <T> T backtrack(Object object, String name) {
        Map<String, ?> backtracked = (Map<String, ?>) backtrack.apply((B) object);
        return (backtracked == null) ? null : (T) backtracked.get(name);
    }

}
