package com.github.t1.meta2.reflection;

import java.lang.reflect.Field;
import java.util.Optional;

import com.github.t1.meta2.Scalar;

import lombok.SneakyThrows;

class FieldReflectionScalar implements Scalar {
    private final Field field;
    private Object object;

    public FieldReflectionScalar(Field field) {
        this.field = field;
        this.field.setAccessible(true);
    }

    @Override
    public String getName() {
        return field.getName();
    }

    @Override
    public FieldReflectionScalar attach(Object object) {
        this.object = object;
        return this;
    }

    @Override
    @SneakyThrows(ReflectiveOperationException.class)
    public Optional<String> getStringValue() {
        return Optional.ofNullable(field.get(object)).map(value -> value.toString());
    }
}