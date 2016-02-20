package com.github.t1.meta2;

import java.util.*;

import com.google.common.reflect.TypeToken;

/**
 * A collection of objects, generally but not necessarily ordered and of the same type. A.k.a. Collection, List, Set, etc.
 */
public interface Sequence<B> extends Container<B, Integer> {
    /*****************************************************************
     * The most interesting methods are inherited from the container *
     *****************************************************************/

    /** @return all {@link Structure.Element}s in this sequence. */
    @Override
    default List<Structure.Element<B>> getItems() {
        return getMeta(new TypeToken<List<Structure.Element<B>>>() {}).get();
    }

    /** @return the {@link Structure.Element} with that index in this sequence. */
    @Override
    default Optional<Structure.Element<B>> getItem(Integer index) {
        return getItems().stream().filter(item -> index.equals(item.getIndex())).findAny();
    }
}
