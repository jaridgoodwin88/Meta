package com.github.t1.meta2;

import java.util.*;

import com.google.common.reflect.TypeToken;

/**
 * Projection of a number of distinct keys to corresponding values. A.k.a. Map, Dictionary, etc.
 */
public interface Mapping<B> extends Container<B, String> {
    /*****************************************************************
     * The most interesting methods are inherited from the container *
     *****************************************************************/

    /** @return all {@link Structure.Property}s in this mapping. */
    @Override
    default List<Structure.Property<B>> getItems() {
        return getMeta(new TypeToken<List<Structure.Property<B>>>() {}).get();
    }

    /** @return the {@link Structure.Property} with that name in this mapping. */
    @Override
    default Optional<Structure.Property<B>> getItem(String key) {
        return getItems().stream().filter(item -> item.getName().equals(key)).findAny();
    }

    /** alias for {@link #getItems()} */
    default List<Structure.Property<B>> getProperties() {
        return getItems();
    }

    /** alias for {@link #getItem(String)} */
    default Optional<Structure.Property<B>> getProperty(String key) {
        return getItem(key);
    }
}
