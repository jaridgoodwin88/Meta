package com.github.t1.meta2;

import java.util.List;

public interface Mapping<B> {
    public interface Property<B> {
        StructureKind getKind();

        String getName();

        Scalar<B> getScalarValue();
    }

    public default Scalar<B> getScalar(String name) {
        return getProperty(name).getScalarValue();
    }

    public Property<B> getProperty(String name);

    public List<Property<B>> getProperties();
}
