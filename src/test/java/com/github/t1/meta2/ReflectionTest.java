package com.github.t1.meta2;

import com.github.t1.meta2.reflection.Reflect;

import lombok.Data;

public class ReflectionTest extends AbstractMappingTest {
    @Data
    public static class Pojo {
        String stringProperty = "stringValue";
    }

    @Override
    protected Object createObject() {
        return new Pojo();
    }

    @Override
    protected Reflect createMapping(Object object) {
        return Reflect.on(object);
    }
}
