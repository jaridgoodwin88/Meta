package com.github.t1.meta2.test;

import static java.util.Arrays.*;

import java.util.List;

import org.junit.*;

import com.github.t1.meta2.*;

import lombok.RequiredArgsConstructor;

public class ReflectionMappingTest extends AbstractMappingTest<com.github.t1.meta2.test.ReflectionMappingTest.Pojo> {
    @Override
    protected boolean hasSchema() {
        return false; // TODO should be true
    }

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
        List<List<String>> nestedSequenceSequenceProperty = asList(asList("A1", "A2"), asList("B1", "B2", "B3"));

        List<NestedPojo> nestedMappingSequenceProperty = asList(new NestedPojo("A"), new NestedPojo("B"));
        NestedPojo nestedProperty = new NestedPojo("nestedString");
        NestingPojo nestingProperty = new NestingPojo();

    }

    @RequiredArgsConstructor
    public static class NestedPojo {
        final String nestedStringProperty;

    }

    public static class NestingPojo {
        NestedPojo nestedProperty = new NestedPojo("nestedString");

    }

    @Override
    protected Pojo createObject() {
        return new Pojo();
    }

    @Override
    protected Mapping<Pojo> createMapping() {
        return ReflectionMeta.mapping(Pojo.class);
    }

    // TODO pull this up
    @Test
    @Ignore
    public void shouldGetNestedSequenceMapping() {
        // TODO        Property<Pojo> list = getProperty("nestedMappingSequenceProperty");
        //
        //        assertThat(list.getSequence().size(object)).isEqualTo(2);
        //
        //        assertThat(list.getSequence().get(0)
        //                .getMapping().getProperty("nestedString")
        //                .getScalar().get(object, String.class))
        //                .contains("A");
        //        assertThat(list.getSequence().get(1)
        //                .getMapping().getProperty("nestedString")
        //                .getScalar().get(object, String.class))
        //                .contains("B");
        //
        //        assertThat(list.get(0).get(object, String.class)).contains("A");
        //        assertThat(list.get(0).get(object, String.class)).contains("B");
    }
}
