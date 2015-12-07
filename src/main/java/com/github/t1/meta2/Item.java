package com.github.t1.meta2;

import com.github.t1.meta2.Mapping.Property;

interface Item<B> {
    StructureKind getKind();

    Scalar<B> getScalar();

    Sequence<B> getSequence();

    Mapping<B> getMapping();

    default Property<B> getProperty(String name) {
        return getMapping().getProperty(name);
    }
}
