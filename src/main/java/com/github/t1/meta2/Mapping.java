package com.github.t1.meta2;

import java.util.List;

import com.github.t1.meta2.Structure.Property;

/**
 * Projection of a number of distinct keys to corresponding values. A.k.a. Map, Dictionary, etc.
 */
public interface Mapping<B> {
    Scalar<B> getScalar(String name);

    Sequence<B> getSequence(String name);

    Mapping<B> getMapping(String name);

    /** convenience method */
    default List<Property<B>> getProperties() {
        return null; // TODO test and implement
    }

    /** convenience method */
    default Property<B> getPropertyPath(String path) {
        return null; // TODO test and implement
    }

    /** convenience method */
    default Property<B> getProperty(String name) {
        return null; // TODO test and implement
    }
}
