package com.github.t1.meta2.reflection;

import java.lang.reflect.Field;
import java.util.Optional;

import com.github.t1.meta2.Scalar;

import lombok.SneakyThrows;

class FieldReflectionScalar implements Scalar {
    private final Field field;

    public FieldReflectionScalar(Field field) {
        this.field = field;
        this.field.setAccessible(true);
    }

    @Override
    @SneakyThrows(ReflectiveOperationException.class)
    public Optional<String> getStringValue(Object object) {
        return Optional.ofNullable(field.get(object)).map(value -> value.toString());
    }
}
