package com.github.t1.meta2;

import static com.github.t1.meta2.Structure.Kind.*;

/**
 * The structure or schema of an object.
 */
public class Structure {
    public enum Kind {
        scalar,
        sequence,
        mapping
    }

    /** The key-value pair in a {@link Mapping}. */
    public interface Property<B> extends Item<B> {
        String getName();
    }

    /** One in a {@link Sequence} of objects */
    public interface Element<B> extends Item<B> {
        int getIndex();
    }

    /** Either a {@link Property} or an {@link Element} */
    interface Item<B> {
        Structure.Kind getKind();

        default void checkKind(Structure.Kind expected) {
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
    }
}
