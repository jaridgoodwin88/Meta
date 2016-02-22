package com.github.t1.meta2;

import com.github.t1.meta2.util.JsonArrayCollector;
import lombok.NonNull;
import lombok.Value;

import java.nio.file.InvalidPathException;

import static java.util.Arrays.asList;

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
    public abstract static class Element<B> extends Item<B, Integer> {
        public int getIndex() {
            return getKey();
        }
    }

    /** The key-value pair in a {@link Mapping}. */
    public abstract static class Property<B> extends Item<B, String> {
        public String getName() {
            return getKey();
        }
    }

    /** Either a {@link Property} or an {@link Element} */
    abstract static class Item<B, K> {
        public abstract Structure.Kind getKind();

        public abstract K getKey();

        public void checkKind(Structure.Kind expected) {
            if (getKind() != expected)
                throw new IllegalStateException(this + " is a " + getKind() + ", not a " + expected);
        }

        @Override
        public String toString() {
            return getKind() + " " + getKey();
        }
    }

    @Value
    public static class Path {
        public static final Path ROOT = new Path(null, null);

        /**
         * @param path A <code>/</code>-separated list of container expression,
         *             i.e. Integers for sequence element indexes an/or Strings for mapping property names.
         * @return a path of access objects, which may be invalid when there's no {@link Structure} available, but never <code>null</code>.
         * @throws InvalidPathException when there <em>is</em> a {@link Structure} available, but not valid for this path expression.
         */
        private static Structure.Path of(String path) {
            return asList(path.split("/")).stream()
                    .reduce(ROOT,
                            Path::with,
                            JsonArrayCollector::neverCombine);
        }

        private final Path parent;
        private final String key;

        public Path with(@NonNull Object key) {
            if (this.equals(ROOT))
                return new Path(null, key.toString());
            else
                return new Path(this, key.toString());
        }

        public String toString() {
            StringBuilder out = new StringBuilder();
            if (parent != null)
                out.append(parent);
            out.append("/");
            if (key != null)
                out.append(key);
            return out.toString();
        }
    }
}
