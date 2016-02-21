package com.github.t1.meta2.test;

import com.github.t1.meta2.Mapping;
import com.github.t1.meta2.MetaReflection;
import lombok.RequiredArgsConstructor;
import org.junit.Ignore;
import org.junit.Test;

import java.util.List;

import static java.util.Arrays.asList;

@SuppressWarnings("unused")
public class ReflectionMappingTest extends AbstractMappingTest<com.github.t1.meta2.test.ReflectionMappingTest.Pojo> {
    @Override
    protected boolean hasSchema() {
        return true;
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

        List<NestedPojo> nestedMappingSequenceProperty = asList(new NestedPojo("A", 0), new NestedPojo("B", 1));
        NestedPojo nestedProperty = new NestedPojo("nestedString", INTEGER_VALUE);
        NestingPojo nestingProperty = new NestingPojo();
        OuterNestingPojo outerNestingProperty = new OuterNestingPojo();
    }

    @RequiredArgsConstructor
    public static class NestedPojo {
        final String nestedStringProperty;
        final Integer nestedIntegerProperty;
        final List<String> nestedSequenceProperty = STRING_LIST_VALUE;
    }

    public static class NestingPojo {
        NestedPojo nested0 = new NestedPojo("nestedString0", 0);
        NestedPojo nested1 = new NestedPojo("nestedString1", 1);
    }

    public static class OuterNestingPojo {
        NestingPojo nestingPojo = new NestingPojo();
        List<NestingPojo> nestingPojoList = asList(new NestingPojo(), new NestingPojo());
        NestedPojo[] nestedPojoArray = { new NestedPojo("A", 0), new NestedPojo("B", 1) };
    }

    @Override
    protected Pojo createObject() {
        return new Pojo();
    }

    @Override
    protected Mapping<Pojo> createMapping() {
        return MetaReflection.mapping(Pojo.class);
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
