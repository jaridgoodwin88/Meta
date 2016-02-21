package com.github.t1.meta2;

import java.util.Optional;

/**
 * A non-{@link Scalar scalar} object, i.e. either a {@link Sequence} or a {@link Mapping}.
 *
 * @param <B> the backtrack type, i.e. the type of object that a chain of containers can be applied to.
 * @param <K> the type required to access items, i.e. Integer for {@link Sequence}s or String {@link Mapping}s.
 */
@SuppressWarnings("ClassReferencesSubclass")
public interface Container<B, K> {
    /** @return the {@link Scalar} with that key in this container. */
    Scalar<B> getScalar(K key);

    /** @return the {@link Sequence} with that key in this container. */
    Sequence<B> getSequence(K key);

    /** @return the {@link Mapping} with that key in this container. */
    Mapping<B> getMapping(K key);

    /**
     * @return the path of containers chained together from a root container to backtrack from.
     */
    Structure.Path getPath();

    /** @return the optional meta data of that type. */
    default <T> Optional<T> getMeta(Class<T> type) {
        return null; // TODO getMeta(TypeToken.of(type));
    }

    /** @return the optional meta data of that type, where the type token can have parameters. */
    //    default <T> Optional<T> getMeta(@SuppressWarnings("unused") TypeToken<T> type) {
    //        return Optional.empty();
    //    }
}
