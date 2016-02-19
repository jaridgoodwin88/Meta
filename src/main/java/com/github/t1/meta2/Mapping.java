package com.github.t1.meta2;

import java.util.*;

import com.github.t1.meta2.Structure.Property;
import com.google.common.reflect.TypeToken;

/**
 * Projection of a number of distinct keys to corresponding values. A.k.a. Map, Dictionary, etc.
 */
public interface Mapping<B> extends Container<B, String> {
    default <T> Optional<T> getMeta(Class<T> type) {
        return getMeta(TypeToken.of(type));
    }

    default <T> Optional<T> getMeta(TypeToken<T> type) {
        return Optional.empty();
    }

    /** convenience method */
    default List<Property<B>> getProperties() {
        return getMeta(new TypeToken<List<Property<B>>>() {}).get();
    }

    /** convenience method */
    default Property<B> getProperty(String name) {
        return null; // TODO test and implement
    }

    /** convenience method */
    default Property<B> getPropertyPath(String path) {
        return null; // TODO test and implement
    }
}
