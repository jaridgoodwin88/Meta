package com.github.t1.meta2.test;

import com.github.t1.meta2.Mapping;
import com.github.t1.meta2.MetaJson;

import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;
import java.util.stream.IntStream;

import static com.github.t1.meta2.util.JsonArrayCollector.toJsonArray;
import static org.junit.Assume.assumeTrue;

public class JsonMappingTest extends AbstractMappingTest<JsonObject> {

    private static final JsonArray STRING_ARRAY = STRING_LIST_VALUE.stream().collect(toJsonArray());

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
        json.add("stringListProperty", STRING_ARRAY);
        json.add("nestedSequenceSequenceProperty", Json.createArrayBuilder()
                .add(Json.createArrayBuilder().add("A1").add("A2"))
                .add(Json.createArrayBuilder().add("B1").add("B2").add("B3")));
        json.add("nestedMappingSequenceProperty", Json.createArrayBuilder().add(
                Json.createObjectBuilder().add("nestedStringProperty", "A")).add(
                Json.createObjectBuilder().add("nestedStringProperty", "B")));

        json.add("nestedProperty", Json.createObjectBuilder()
                .add("nestedStringProperty", "nestedString")
                .add("nestedIntegerProperty", INTEGER_VALUE)
                .add("nestedSequenceProperty", STRING_ARRAY));
        json.add("nestingProperty", Json.createObjectBuilder().add("nestedProperty",
                Json.createObjectBuilder().add("nestedStringProperty", "nestedString")));

        return json.build();
    }

    @Override
    protected Mapping<JsonObject> createMapping() {
        return MetaJson.mapping();
    }

    @Override
    public void shouldGetNestedProperties() {
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
