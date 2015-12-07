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

    public default Property<B> getPropertyPath(String path) {
        String[] elements = path.split("/");
        Property<B> property = this.getProperty(elements[0]);
        for (int i = 1; i < elements.length; i++)
            property = property.getProperty(elements[i]);
        return property;
    }
}
