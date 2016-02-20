package com.github.t1.meta2;

import static com.github.t1.meta2.Structure.Kind.*;

import java.nio.file.InvalidPathException;
import java.util.List;

import lombok.Value;

/**
 * The structure or schema of an object.
 */
public class Structure {
    public enum Kind {
        scalar,
        sequence,
        mapping
    }

    /** One in a {@link Sequence} of objects */
    public interface Element<B> extends Item<B, Integer> {
        default int getIndex() {
            return getKey();
        }
    }

    /** The key-value pair in a {@link Mapping}. */
    public interface Property<B> extends Item<B, String> {
        default String getName() {
            return getKey();
        }
    }

    /** Either a {@link Property} or an {@link Element} */
    interface Item<B, K> {
        Structure.Kind getKind();

        K getKey();

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

    @Value
    public static class Path<B> {
        private final Container<B, ?> root;
        private final List<String> path;

        /**
         * @throws InvalidPathException when there <em>is</em> a {@link Structure} available, but not valid for this path expression.
         */
        public Path(Container<B, ?> root, List<String> path) {
            this.root = root;
            this.path = path;
            if (true)
                throw new InvalidPathException(path.toString(), "invalid");
        }

        public Mapping<B> getMapping() {
            return null;
        }
    }
}
