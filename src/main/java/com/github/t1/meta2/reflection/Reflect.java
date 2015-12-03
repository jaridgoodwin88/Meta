package com.github.t1.meta2.reflection;

import com.github.t1.meta2.*;

import lombok.*;

@RequiredArgsConstructor
public class Reflect implements Mapping {
    public static Reflect on(Object object) {
        return on(object.getClass());
    }

    public static Reflect on(Class<?> type) {
        return new Reflect(type);
    }

    private final Class<?> type;

    @Override
    @SneakyThrows(ReflectiveOperationException.class)
    public Scalar getScalar(String name) {
        return new FieldReflectionScalar(type.getDeclaredField(name));
    }
}