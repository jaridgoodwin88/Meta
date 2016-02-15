package com.github.t1.meta2;

import java.util.List;

/**
 * Projection of a number of distinct keys to corresponding values. A.k.a. Map, Dictionary, etc.
 */
public interface Mapping<B> {
    /** The key-value pair in a {@link Mapping}. */
    interface Property<B> extends Item<B> {
        String getName();
    }

    Property<B> getProperty(String name);

    List<Property<B>> getProperties();

    /** convenience method */
    default Scalar<B> getScalar(String name) {
        return getProperty(name).getScalar();
    }

    /** convenience method */
    default Sequence<B> getSequence(String name) {
        return getProperty(name).getSequence();
    }

    default Property<B> getPropertyPath(String path) {
        String[] elements = path.split("/");
        Property<B> property = this.getProperty(elements[0]);
        for (int i = 1; i < elements.length; i++)
            property = property.get(elements[i]);
        return property;
    }
}
