package com.github.t1.meta3;

import com.google.common.collect.ImmutableMap;

public class CollectionVisitorTest extends AbstractVisitorTest {
    @Override protected Object createPropertyContainer() {
        return ImmutableMap.of("one", "a", "two", "b");
    }
}
