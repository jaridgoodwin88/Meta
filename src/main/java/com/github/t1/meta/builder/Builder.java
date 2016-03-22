package com.github.t1.meta.builder;

public interface Builder<T> {
    void set(Object key, Object value);

    T build();
}
