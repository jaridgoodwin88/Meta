package com.github.t1.metatest;

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
        return Map.of("mappingOne", createFlatMapping());
    }

    @Override protected Object createFlatSequence() {
        return asList("a", "b", null, "c");
    }

    @Override protected Object createNestedSequence() {
        return List.of(List.of("a1", "a2", "a3"), List.of(), List.of("c1"));
    }

    @Override protected Object createMappingWithSequence() {
        return Map.of(
                "a", asList("x", "y", null, "z"),
                "b", List.of(),
                "c", List.of("x"));
    }

    @Override protected Object createSequenceWithMapping() {
        Map<Object, Object> map3 = new LinkedHashMap<>();
        map3.put("one", null);
        map3.put("two", "b");
        map3.put("nil", null);
        return List.of(
                createFlatMapping(),
                Map.of(),
                map3);
    }
}
