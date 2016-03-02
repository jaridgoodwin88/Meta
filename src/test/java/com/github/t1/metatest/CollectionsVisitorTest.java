package com.github.t1.metatest;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;

public class CollectionsVisitorTest extends AbstractVisitorTest {
    @Override protected Object createFlatMapping() {
        return ImmutableMap.of("one", "a", "two", "b");
    }

    @Override protected Object createNestedMapping() {
        return ImmutableMap.of("mappingOne", createFlatMapping());
    }

    @Override protected Object createFlatSequence() {
        return ImmutableList.of("a", "b", "c");
    }

    @Override protected Object createNestedSequence() {
        return ImmutableList.of(ImmutableList.of("a1", "a2", "a3"), ImmutableList.of(), ImmutableList.of("c1"));
    }

    @Override protected Object createMappingWithSequence() {
        return ImmutableMap.of(
                "a", ImmutableList.of("x", "y", "z"),
                "b", ImmutableList.of(),
                "c", ImmutableList.of("x"));
    }

    @Override protected Object createSequenceWithMapping() {
        return ImmutableList.of(
                ImmutableMap.of("one", "a", "two", "b"),
                ImmutableMap.of(),
                ImmutableMap.of("two", "b"));
    }
}
