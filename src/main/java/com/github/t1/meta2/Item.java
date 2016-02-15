package com.github.t1.meta2;

import static com.github.t1.meta2.StructureKind.*;

import java.util.Optional;

import com.github.t1.meta2.Mapping.Property;
import com.github.t1.meta2.Sequence.Element;

/** Either a {@link Mapping.Property} or a {@link Sequence.Element} */
interface Item<B> {
    StructureKind getKind();

    default void checkKind(StructureKind expected) {
        if (getKind() != expected)
            throw new IllegalStateException(this + " is a " + getKind() + ", not a " + expected);
    }

    default Scalar<B> getScalar() {
        checkKind(scalar);
        throw notOverloaded();
    }

    default Sequence<B> getSequence() {
        checkKind(sequence);
        throw notOverloaded();
    }

    default Mapping<B> getMapping() {
        checkKind(mapping);
        throw notOverloaded();
    }

    default UnsupportedOperationException notOverloaded() {
        return new UnsupportedOperationException("not overloaded");
    }

    /** convenience method */
    default <T> Optional<T> get(B object, Class<T> type) {
        return getScalar().get(object, type);
    }

    /** convenience method */
    @SuppressWarnings("ClassReferencesSubclass")
    default Element<B> get(int index) {
        return getSequence().get(index);
    }

    /** convenience method */
    @SuppressWarnings("ClassReferencesSubclass")
    default Property<B> get(String name) {
        return getMapping().getProperty(name);
    }
}
