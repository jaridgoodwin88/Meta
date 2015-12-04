package com.github.t1.meta2;

import com.github.t1.meta2.reflection.Reflect;

import lombok.Data;

public class ReflectionTest extends AbstractMappingTest<com.github.t1.meta2.ReflectionTest.Pojo> {
    @Data
    public static class Pojo {
        String stringProperty = "stringValue";
    }

    @Override
    protected Pojo createObject() {
        return new Pojo();
    }

    @Override
    protected Reflect<Pojo> createMapping() {
        return Reflect.on(Pojo.class);
    }
}
