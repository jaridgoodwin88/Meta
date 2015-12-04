package com.github.t1.meta2;

import java.util.*;

import com.github.t1.meta2.reflection.CollectionsMapping;

public class CollectionsTest extends AbstractMappingTest {
    @Override
    protected Object createObject() {
        Map<String, String> map = new LinkedHashMap<>();
        map.put("stringProperty", "stringValue");
        return map;
    }

    @Override
    protected Mapping createMapping(Object object) {
        @SuppressWarnings("unchecked")
        Map<String, ?> map = (Map<String, ?>) object;
        return CollectionsMapping.of(map);
    }

}
