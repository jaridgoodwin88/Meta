package com.github.t1.meta2;

/**
 * A non-{@link Scalar scalar} object, i.e. either a {@link Sequence} or a {@link Mapping}.
 *
 * @param <B> the backtrack type
 * @param <K> the type required to access items, i.e. Integer for {@link Sequence}s or String {@link Mapping}s.
 */
@SuppressWarnings("ClassReferencesSubclass")
public interface Container<B, K> {
    Scalar<B> getScalar(K key);

    Sequence<B> getSequence(K key);

    Mapping<B> getMapping(K key);
}
