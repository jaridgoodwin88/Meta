package com.github.t1.meta2;

public interface Sequence<B> {
    public interface Element<B> extends Item<B> {
        int getIndex();
    }

    int size(B object);

    Element<B> get(int i);
}
