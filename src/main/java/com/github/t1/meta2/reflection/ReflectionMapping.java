package com.github.t1.meta2.reflection;

import static com.github.t1.meta2.StructureKind.*;

import java.lang.reflect.Field;
import java.util.*;

import com.github.t1.meta2.*;

import lombok.*;

public class ReflectionMapping<B> implements Mapping<B> {
    public static <B> ReflectionMapping<B> on(Class<B> type) {
        return new ReflectionMapping<>(type);
    }

    private final Map<String, FieldReflectionProperty<B>> properties;

    private ReflectionMapping(Class<?> type) {
        this.properties = new LinkedHashMap<>();
        for (Field field : type.getDeclaredFields())
            properties.put(field.getName(), new FieldReflectionProperty<>(field));
    }

    @RequiredArgsConstructor
    private static class FieldReflectionProperty<B> implements Property<B> {
        private final Field field;

        @Override
        public String getName() {
            return field.getName();
        }

        @Override
        public Scalar<B> getScalarValue() {
            return new FieldReflectionScalar<>(field);
        }

        @Override
        public StructureKind getKind() {
            return scalar;
        }
    }

    private static class FieldReflectionScalar<B> extends ObjectScalar<B> {
        private final Field field;

        public FieldReflectionScalar(Field field) {
            this.field = field;
            this.field.setAccessible(true);
        }

        @Override
        @SneakyThrows(ReflectiveOperationException.class)
        protected Object get(Object object) {
            return field.get(object);
        }
    }

    @Override
    public Property<B> getProperty(String name) {
        return properties.get(name);
    }

    @Override
    public List<Property<B>> getProperties() {
        return new ArrayList<>(properties.values());
    }

}
