package com.github.t1.meta3;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;

public class CollectionVisitorTest extends AbstractVisitorTest {
    @Override protected Object createFlatMapping() {
        return ImmutableMap.of("one", "a", "two", "b");
    }

    @Override protected Object createFlatSequence() {
        return ImmutableList.of("a", "b", "c");
    }
}
