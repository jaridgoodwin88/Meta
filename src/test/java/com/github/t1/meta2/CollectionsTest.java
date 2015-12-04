package com.github.t1.meta2;

import java.util.*;

import com.github.t1.meta2.reflection.CollectionsMapping;

public class CollectionsTest extends AbstractMappingTest<Map<String, Object>> {
    @Override
    protected Map<String, Object> createObject() {
        Map<String, Object> map = new LinkedHashMap<>();
        map.put("stringProperty", "stringValue");
        return map;
    }

    @Override
    protected Mapping<Map<String, Object>> createMapping() {
        return CollectionsMapping.of(object);
    }

}
