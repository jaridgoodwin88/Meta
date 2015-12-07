package com.github.t1.meta2;

import java.util.List;

public interface Mapping<B> {
    public interface Property<B> extends Item<B> {
        String getName();
    }

    public Property<B> getProperty(String name);

    public List<Property<B>> getProperties();

    /** convenience method */
    public default Scalar<B> getScalar(String name) {
        return getProperty(name).getScalar();
    }

    /** convenience method */
    public default Sequence<B> getSequence(String name) {
        return getProperty(name).getSequence();
    }
}
