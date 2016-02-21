package com.github.t1.meta2;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Optional;
import java.util.function.BiFunction;
import java.util.function.Function;

@RequiredArgsConstructor
public abstract class AbstractContainer<B, K> implements Container<B, K> {
    @Getter
    protected final Structure.Path path;
    protected final Function<Optional<B>, Optional<B>> backtrack;
    protected final BiFunction<Object, Class<?>, ?> cast;
    protected final BiFunction<Structure.Path, Function<Optional<B>, Optional<B>>, Sequence<B>> sequenceFactory;
    protected final BiFunction<Structure.Path, Function<Optional<B>, Optional<B>>, Mapping<B>> mappingFactory;
    protected final BiFunction<Optional<B>, K, Optional<B>> resolve;

    @Override
    public Scalar<B> getScalar(K key) {
        return new Scalar<B>() {
            @Override
            public <T> Optional<T> get(Optional<B> object, Class<T> type) {
                return cast(resolve(object, key), type);
            }

            private <T> Optional<T> cast(Optional<B> optional, Class<T> type) {
                return optional.map(value -> type.cast(cast.apply(value, type)));
            }

            @Override
            public String toString() {
                return "Scalar[" + path.with(key) + "]";
            }
        };
    }

    @Override
    public Sequence<B> getSequence(K key) {
        return sequenceFactory.apply(path.with(key), object -> resolve(object, key));
    }

    @Override
    public Mapping<B> getMapping(K key) {
        return mappingFactory.apply(path.with(key), object -> resolve(object, key));
    }

    protected Optional<B> resolve(Optional<B> optional, K key) {
        Optional<B> backtracked = backtrack.apply(optional);
        try {
            return backtracked.flatMap(value -> resolve.apply(Optional.of(value), key));
        } catch (IndexOutOfBoundsException e) {
            return Optional.empty();
        }
    }

    @Override
    public String toString() {
        return "Container[" + path + "]";
    }
}
