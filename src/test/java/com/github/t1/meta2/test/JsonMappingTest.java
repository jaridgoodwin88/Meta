package com.github.t1.meta2.test;

import static com.github.t1.meta2.util.JsonArrayCollector.*;
import static org.junit.Assume.*;

import java.util.stream.IntStream;

import javax.json.*;

import com.github.t1.meta2.*;

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

        json.add("intArrayProperty", IntStream.of(INT_ARRAY_VALUE).boxed().collect(toJsonArray()));
        json.add("intListProperty", INT_LIST_VALUE.stream().collect(toJsonArray()));
        json.add("stringListProperty", STRING_LIST_VALUE.stream().collect(toJsonArray()));
        json.add("nestedSequenceSequenceProperty", Json.createArrayBuilder()
                .add(Json.createArrayBuilder().add("A1").add("A2"))
                .add(Json.createArrayBuilder().add("B1").add("B2").add("B3")));
        json.add("nestedMappingSequenceProperty", Json.createArrayBuilder().add(nested("A")).add(nested("B")));

        json.add("nestedProperty", nested("nestedString"));
        json.add("nestingProperty", Json.createObjectBuilder().add("nestedProperty",
                nested("nestedString")));

        return json.build();
    }

    private JsonObjectBuilder nested(String value) {
        return Json.createObjectBuilder().add("nestedStringProperty", value);
    }

    @Override
    protected Mapping<JsonObject> createMapping() {
        return JsonMeta.mapping();
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
