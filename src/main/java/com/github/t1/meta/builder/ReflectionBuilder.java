package com.github.t1.meta.builder;

import lombok.SneakyThrows;

import java.lang.reflect.*;

class ReflectionBuilder<T> implements Builder<T> {
    private final Class<T> type;
    private T instance;

    ReflectionBuilder(Class<T> type) {
        this.type = type;
        this.instance = newInstance();
    }

    @SneakyThrows(ReflectiveOperationException.class) private T newInstance() {
        Constructor<T> constructor = type.getDeclaredConstructor();
        constructor.setAccessible(true);
        return constructor.newInstance();
    }

    @SneakyThrows(ReflectiveOperationException.class)
    @Override public void set(Object key, Object value) {
        Field field = type.getDeclaredField(key.toString());
        field.setAccessible(true);
        field.set(instance, value);
    }

    @Override public T build() {
        return instance;
    }
}
