package com.github.t1.meta2;

import java.util.List;
import java.util.Optional;

/**
 * Projection of a number of distinct keys to corresponding values. A.k.a. Map, Dictionary, etc.
 */
public interface Mapping<B> extends Container<B, String> {
    /*****************************************************************
     * The most interesting methods are inherited from the container *
     *****************************************************************/

    /**
     * @return all {@link Structure.Property properties} in this mapping,
     * or an empty list, if no {@link Structure} meta data is available.
     */
    default List<Structure.Property<B>> getProperties() {
        return null; // TODO getMeta(new TypeToken<List<Structure.Property<B>>>() {}).get();
    }

    /**
     * @return the {@link Structure.Property} with that name in this mapping,
     * or an empty optional, if no {@link Structure} meta data is available.
     */
    default Optional<Structure.Property<B>> getProperty(String key) {
        return getProperties().stream().filter(item -> item.getName().equals(key)).findAny();
    }
}
