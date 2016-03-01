package com.github.t1.meta.visitor;

import com.github.t1.meta.Property;
import lombok.SneakyThrows;
import lombok.Value;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.Arrays;
import java.util.stream.Stream;

class ReflectionGuide extends MappingGuide {
    private final Object object;

    public ReflectionGuide(GuideFactory guideFactory, Object object) {
        super(guideFactory);
        this.object = object;
    }

    @Override protected Stream<Property> getProperties() {
        return Arrays.asList(object.getClass().getDeclaredFields())
                .stream()
                .filter(this::isProperty)
                .map(ReflectionProperty::new);
    }

    private boolean isProperty(Field field) {
        return !Modifier.isStatic(field.getModifiers())
                && !Modifier.isVolatile(field.getModifiers());
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
