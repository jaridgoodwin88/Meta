package com.github.t1.meta2.test;

import com.github.t1.meta2.Mapping;
import com.github.t1.meta2.MetaCollection;
import com.google.common.collect.ImmutableMap;

import java.util.LinkedHashMap;
import java.util.Map;

import static java.util.Arrays.asList;

public class CollectionMappingTest extends AbstractMappingTest<Map<String, ?>> {
    @Override
    protected Map<String, ?> createObject() {
        Map<String, Object> map = new LinkedHashMap<>();
        map.put("stringProperty", STRING_VALUE);
        map.put("booleanProperty", BOOLEAN_VALUE);
        map.put("charProperty", CHARACTER_VALUE);
        map.put("byteProperty", BYTE_VALUE);
        map.put("shortProperty", SHORT_VALUE);
        map.put("intProperty", INT_VALUE);
        map.put("integerProperty", INTEGER_VALUE);
        map.put("longProperty", LONG_VALUE);
        map.put("floatProperty", FLOAT_VALUE);
        map.put("doubleProperty", DOUBLE_VALUE);

        map.put("intArrayProperty", INT_ARRAY_VALUE);
        map.put("intListProperty", INT_LIST_VALUE);
        map.put("stringListProperty", STRING_LIST_VALUE);
        map.put("nestedSequenceSequenceProperty", asList(asList("A1", "A2"), asList("B1", "B2", "B3")));
        map.put("nestedMappingSequenceProperty", asList(
                ImmutableMap.of("nestedStringProperty", "A"),
                ImmutableMap.of("nestedStringProperty", "B")));

        map.put("nestedProperty", ImmutableMap.of(
                "nestedStringProperty", "nestedString",
                "nestedIntegerProperty", INTEGER_VALUE,
                "nestedSequenceProperty", STRING_LIST_VALUE));
        map.put("nestingProperty", ImmutableMap.of(
                "nestedProperty", ImmutableMap.of(
                        "nestedStringProperty", "nestedString")));
        map.put("outerNestingProperty", ImmutableMap.of(
                "nestingPojo", ImmutableMap.of(
                        "nestedProperty", ImmutableMap.of(
                                "nestedStringProperty", "nestedString")),
                "nestingPojoList", "asList(new NestingPojo(), new NestingPojo()",
                "nestedPojoArray",
                "{ new NestedPojo(\"A\", 0), new ReflectionMappingTest.NestedPojo(\"B\", 1)"));

        return map;
    }

    @Override
    protected Mapping<Map<String, ?>> createMapping() {
        return MetaCollection.mapping();
    }
}
