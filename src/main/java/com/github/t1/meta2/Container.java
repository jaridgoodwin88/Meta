package com.github.t1.meta2;

import static java.util.Arrays.*;
import static java.util.Collections.*;

import java.nio.file.InvalidPathException;
import java.util.*;

import com.google.common.reflect.TypeToken;

/**
 * A non-{@link Scalar scalar} object, i.e. either a {@link Sequence} or a {@link Mapping}.
 *
 * @param <B> the backtrack type
 * @param <K> the type required to access items, i.e. Integer for {@link Sequence}s or String {@link Mapping}s.
 */
@SuppressWarnings("ClassReferencesSubclass")
public interface Container<B, K> {
    /** @return the {@link Scalar} with that key in this container. */
    Scalar<B> getScalar(K key);

    /** @return the {@link Sequence} with that key in this container. */
    Sequence<B> getSequence(K key);

    /** @return the {@link Mapping} with that key in this container. */
    Mapping<B> getMapping(K key);

    /** @return the optional meta data of that type. */
    default <T> Optional<T> getMeta(Class<T> type) {
        return getMeta(TypeToken.of(type));
    }

    /** @return the optional meta data of that type, where the type token can have parameters. */
    default <T> Optional<T> getMeta(@SuppressWarnings("unused") TypeToken<T> type) {
        return Optional.empty();
    }

    /** @return all items in this container */
    default List<? extends Structure.Item<B, K>> getItems() {
        return emptyList();
    }

    /** @return the item with that key in this container */
    default Optional<? extends Structure.Item<B, K>> getItem(K key) {
        return getItems().stream()
                .filter(item -> item.getKey().equals(key))
                .findAny();
    }

    /**
     * @param path A <code>/</code>-separated list of container expression,
     *             i.e. Integers for sequence element indexes an/or Strings for mapping property names.
     * @return a path of access objects, which may be invalid when there's no {@link Structure} available, but never <code>null</code>.
     * @throws InvalidPathException when there <em>is</em> a {@link Structure} available, but not valid for this path expression.
     */
    default Structure.Path<B> getPath(String path) {
        return new Structure.Path<>(this, asList(path.split("/")));
    }
}
