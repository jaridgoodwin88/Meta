package com.github.t1.meta.visitor;

import com.github.t1.meta.Property;
import lombok.SneakyThrows;
import lombok.Value;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

class ReflectionGuide extends MappingGuide {
    private final Object object;

    public ReflectionGuide(GuideFactory guideFactory, Object object) {
        super(guideFactory);
        this.object = object;
    }

    @Override protected Stream<Property> getProperties() {
        return getFields(object).stream().map(ReflectionProperty::new);
    }

    private List<Field> getFields(Object object) {
        return Arrays.asList(object.getClass().getDeclaredFields());
    }

    @Value
    private class ReflectionProperty implements Property {
        private final Field field;

        @Override public Object getName() {
            return field.getName();
        }

        @SneakyThrows(IllegalAccessException.class)
        @Override public Object getValue() {
            field.setAccessible(true);
            return field.get(object);
        }
    }
}
