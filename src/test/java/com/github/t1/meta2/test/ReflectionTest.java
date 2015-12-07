package com.github.t1.meta2.test;

import java.util.List;

import com.github.t1.meta2.reflection.ReflectionMapping;

public class ReflectionTest extends AbstractMappingTest<com.github.t1.meta2.test.ReflectionTest.Pojo> {
    // TODO ignore static fields
    // TODO ignore transient fields
    public static class Pojo {
        String stringProperty = STRING_VALUE;
        boolean booleanProperty = BOOLEAN_VALUE;
        char charProperty = CHARACTER_VALUE;
        byte byteProperty = BYTE_VALUE;
        short shortProperty = SHORT_VALUE;
        int intProperty = INT_VALUE;
        Integer integerProperty = INTEGER_VALUE;
        long longProperty = LONG_VALUE;
        float floatProperty = FLOAT_VALUE;
        double doubleProperty = DOUBLE_VALUE;

        int[] intArrayProperty = INT_ARRAY_VALUE;
        List<Integer> intListProperty = INT_LIST_VALUE;
        List<String> stringListProperty = STRING_LIST_VALUE;

        NestedPojo nestedProperty = new NestedPojo();
        NestingPojo nestingProperty = new NestingPojo();
    }

    public static class NestedPojo {
        String nestedStringProperty = "nestedString";
    }

    public static class NestingPojo {
        NestedPojo nestedProperty = new NestedPojo();
    }

    @Override
    protected Pojo createObject() {
        return new Pojo();
    }

    @Override
    protected ReflectionMapping<Pojo> createMapping() {
        return ReflectionMapping.on(Pojo.class);
    }
}
