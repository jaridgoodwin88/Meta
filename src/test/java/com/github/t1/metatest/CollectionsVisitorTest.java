package com.github.t1.metatest;

import com.google.common.collect.*;

import java.util.*;

import static java.util.Arrays.*;

public class CollectionsVisitorTest extends AbstractVisitorTest {
    @Override protected Map<Object, Object> createFlatMapping() {
        Map<Object, Object> map = new LinkedHashMap<>();
        map.put("one", "a");
        map.put("two", "b");
        map.put("nil", null);
        return map;
    }

    @Override protected Object createNestedMapping() {
        return ImmutableMap.of("mappingOne", createFlatMapping());
    }

    @Override protected Object createFlatSequence() {
        return asList("a", "b", null, "c");
    }

    @Override protected Object createNestedSequence() {
        return ImmutableList.of(ImmutableList.of("a1", "a2", "a3"), ImmutableList.of(), ImmutableList.of("c1"));
    }

    @Override protected Object createMappingWithSequence() {
        return ImmutableMap.of(
                "a", asList("x", "y", null, "z"),
                "b", ImmutableList.of(),
                "c", ImmutableList.of("x"));
    }

    @Override protected Object createSequenceWithMapping() {
        Map<Object, Object> map3 = new LinkedHashMap<>();
        map3.put("one", null);
        map3.put("two", "b");
        map3.put("nil", null);
        return ImmutableList.of(
                createFlatMapping(),
                ImmutableMap.of(),
                map3);
    }
}
