package com.github.t1.meta2.test;

import static java.util.Arrays.*;

import java.util.*;

import com.github.t1.meta2.Mapping;
import com.github.t1.meta2.collections.CollectionsMapping;

public class CollectionsMappingTest extends AbstractMappingTest<Map<String, Object>> {
    @Override
    protected boolean hasSchema() {
        return false;
    }

    @Override
    protected Map<String, Object> createObject() {
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
        map.put("nestedMappingSequenceProperty", asList(nested("A"), nested("B")));

        map.put("nestedProperty", nested("nestedString"));
        map.put("nestingProperty", nested("nestedProperty", nested("nestedString")));

        return map;
    }

    private Map<String, Object> nested(String value) {
        return nested("nestedStringProperty", value);
    }

    private Map<String, Object> nested(String name, Object value) {
        Map<String, Object> nestedMap = new LinkedHashMap<>();
        nestedMap.put(name, value);
        return nestedMap;
    }

    @Override
    protected Mapping<Map<String, Object>> createMapping() {
        return new CollectionsMapping<>();
    }

}
