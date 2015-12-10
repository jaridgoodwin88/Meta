package com.github.t1.meta2;

import static com.github.t1.meta2.StructureKind.*;

import com.github.t1.meta2.Mapping.Property;

/** Either a {@link Mapping.Property} or a {@link Sequence.Element} */
interface Item<B> {
    StructureKind getKind();

    default void checkKind(StructureKind expected) {
        if (getKind() != expected)
            throw new IllegalStateException(this + " is a " + getKind() + ", not a " + expected);
    }

    default Scalar<B> getScalar() {
        checkKind(scalar);
        throw new UnsupportedOperationException("not overloaded");
    }

    default Sequence<B> getSequence() {
        checkKind(sequence);
        throw new UnsupportedOperationException("not overloaded");
    }

    default Mapping<B> getMapping() {
        checkKind(mapping);
        throw new UnsupportedOperationException("not overloaded");
    }

    default Property<B> getProperty(String name) {
        return getMapping().getProperty(name);
    }
}
