package com.github.t1.meta2;

/**
 * A collection of objects, generally but not necessarily ordered and of the same type. A.k.a. Collection, List, Set, etc.
 */
public interface Sequence<B> {
    /** One in a {@link Sequence} of objects */
    interface Element<B> extends Item<B> {
        int getIndex();
    }

    int size(B object);

    Element<B> get(int i);
}
