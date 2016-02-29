package com.github.t1.meta3.visitor;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.stream.Stream;

@RequiredArgsConstructor
class ReflectionGuide extends Guide {
    private final GuideFactory guideFactory;
    private final Object object;

    @Override
    public void guide(Visitor visitor) {
        super.guide(visitor);
        getFields().forEach(field -> guideToProperty(visitor, field));
    }

    private void guideToProperty(Visitor visitor, Field field) {
        visitor.enterProperty((Object) field.getName());
        guideFactory.guideTo(value(field)).guide(visitor);
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
