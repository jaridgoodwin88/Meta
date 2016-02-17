package com.github.t1.meta2;

/**
 * A collection of objects, generally but not necessarily ordered and of the same type. A.k.a. Collection, List, Set, etc.
 */
public interface Sequence<B> {
    Scalar<B> getScalar(int i);

    Sequence<B> getSequence(int i);

    Mapping<B> getMapping(int i);
}
