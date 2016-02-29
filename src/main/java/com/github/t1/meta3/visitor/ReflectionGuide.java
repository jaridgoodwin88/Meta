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
        List<Field> fields = getFields();
        if (fields.isEmpty())
            return;
        super.guide(visitor);
        visitor.enterMapping();
        boolean first = true;
        for (Field field : fields) {
            if (first)
                first = false;
            else
                visitor.continueMapping();
            guideToProperty(visitor, field);
        }
        visitor.leaveMapping();
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

    private List<Field> getFields() {
        return Arrays.asList(object.getClass().getDeclaredFields());
    }
}
