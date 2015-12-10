package com.github.t1.meta2.test;

import static com.github.t1.meta2.json.JsonArrayCollector.*;
import static org.junit.Assume.*;

import java.util.stream.IntStream;

import javax.json.*;

import com.github.t1.meta2.Mapping;
import com.github.t1.meta2.json.JsonMapping;

public class JsonMappingTest extends AbstractMappingTest<JsonObject> {
    @Override
    protected JsonObject createObject() {
        JsonObjectBuilder json = Json.createObjectBuilder();
        json.add("stringProperty", STRING_VALUE);
        json.add("booleanProperty", BOOLEAN_VALUE);
        json.add("charProperty", CHARACTER_VALUE);
        json.add("byteProperty", BYTE_VALUE);
        json.add("shortProperty", SHORT_VALUE);
        json.add("intProperty", INT_VALUE);
        json.add("integerProperty", INTEGER_VALUE);
        json.add("longProperty", LONG_VALUE);
        json.add("floatProperty", FLOAT_VALUE);
        json.add("doubleProperty", DOUBLE_VALUE);

        json.add("intArrayProperty", IntStream.of(INT_ARRAY_VALUE).boxed().collect(asJsonArray()));
        json.add("intListProperty", INT_LIST_VALUE.stream().collect(asJsonArray()));
        json.add("stringListProperty", STRING_LIST_VALUE.stream().collect(asJsonArray()));

        json.add("nestedProperty", Json.createObjectBuilder().add("nestedStringProperty", "nestedString"));
        json.add("nestingProperty", Json.createObjectBuilder().add("nestedProperty",
                Json.createObjectBuilder().add("nestedStringProperty", "nestedString")));

        return json.build();
    }

    @Override
    protected Mapping<JsonObject> createMapping() {
        return JsonMapping.of(object);
    }

    @Override
    public void shouldGetNestedProperties() {
        assumeTrue(false);
    }

    @Override
    public void shouldGetNestingProperties() {
        assumeTrue(false);
    }

    @Override
    public void shouldGetNestedProperty() {
        assumeTrue(false);
    }

    @Override
    public void shouldGetDoublyNestedProperty() {
        assumeTrue(false);
    }

    @Override
    public void shouldGetPropertyPath() {
        assumeTrue(false);
    }
}
