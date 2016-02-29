package com.github.t1.meta3;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.stream.Stream;

@RequiredArgsConstructor
class ReflectionGuide extends Guide {
    private final Object object;

    @Override
    public void guide(Visitor visitor) {
        super.guide(visitor);
        getFields().forEach(field -> guideToProperty(visitor, field));
    }

    private void guideToProperty(Visitor visitor, Field field) {
        visitor.enterProperty((Object) field.getName());
        visitor.visitScalar(value(field));
        visitor.leaveProperty();
    }

    @SneakyThrows(IllegalAccessException.class)
    private Object value(Field field) {
        field.setAccessible(true);
        return field.get(object);
    }

    private Stream<Field> getFields() {
        return Arrays.asList(object.getClass().getDeclaredFields()).stream();
    }
}
