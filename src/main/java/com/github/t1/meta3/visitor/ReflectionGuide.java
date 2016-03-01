package com.github.t1.meta3.visitor;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.List;

@RequiredArgsConstructor
class ReflectionGuide extends Guide {
    private final GuideFactory guideFactory;
    private final Object object;

    @Override
    public void guide(Visitor visitor) {
        List<Field> fields = getFields(object);
        super.guide(visitor);
        visitor.enterMapping();
        boolean first = true;
        for (Field field : fields) {
            Object value = value(object, field);
            if (value == null)
                continue;
            if (first)
                first = false;
            else
                visitor.continueMapping();
            visitor.enterProperty((Object) field.getName());
            guideFactory.guideTo(value).guide(visitor);
            visitor.leaveProperty();
        }
        visitor.leaveMapping();
    }

    private List<Field> getFields(Object object) {
        return Arrays.asList(object.getClass().getDeclaredFields());
    }

    @SneakyThrows(IllegalAccessException.class)
    private Object value(Object object, Field field) {
        field.setAccessible(true);
        return field.get(object);
    }
}
