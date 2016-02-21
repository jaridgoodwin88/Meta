package com.github.t1.meta2;

import java.util.List;

/**
 * A collection of objects, generally but not necessarily ordered and of the same type. A.k.a. Collection, List, Set, etc.
 */
public interface Sequence<B> extends Container<B, Integer> {
    /*****************************************************************
     * The most interesting methods are inherited from the container *
     *****************************************************************/

    <T> List<T> get(B object, Class<T> elementType);

    int size(B object);

    default boolean isEmpty(B object) {
        return size(object) == 0;
    }
}
