package com.github.t1.meta2.json;

import static com.github.t1.meta2.StructureKind.*;
import static java.util.Collections.*;
import static java.util.function.Function.*;
import static javax.json.JsonValue.ValueType.*;

import java.util.*;
import java.util.function.Function;

import javax.json.*;

import com.github.t1.meta2.*;

import lombok.*;

@RequiredArgsConstructor
public class JsonMapping implements Mapping<JsonObject> {
    public static JsonMapping of(JsonObject object) {
        return new JsonMapping(object, identity());
    }

    private final JsonObject object;
    private final Function<JsonObject, JsonObject> backtrack;
    private Map<String, Property<JsonObject>> properties;

    @Override
    public Property<JsonObject> getProperty(String name) {
        return properties().get(name);
    }

    @Override
    public List<Property<JsonObject>> getProperties() {
        return unmodifiableList(new ArrayList<>(properties().values()));
    }

    @SuppressWarnings("unchecked")
    private Map<String, Property<JsonObject>> properties() {
        if (properties == null) {
            properties = new LinkedHashMap<>();
            object.entrySet().stream()
                    .map(entry -> of(entry.getKey(), object))
                    .forEach(p -> properties.put(p.getName(), p));
        }
        return properties;
    }

    public JsonProperty of(String name, JsonObject object) {
        switch (object.get(name).getValueType()) {
        case STRING:
        case NUMBER:
        case FALSE:
        case TRUE:
        case NULL:
            return new JsonScalarProperty(name);
        case ARRAY:
            return new JsonSequenceProperty(name);
        case OBJECT:
            return new JsonMappingProperty(name);
        }
        throw new UnsupportedOperationException("unreachable code");
    }

    @Getter
    @RequiredArgsConstructor
    private static abstract class JsonProperty implements Property {
        private final StructureKind kind;
        private final String name;

        @Override
        public String toString() {
            return "JSON " + kind + " property: " + name;
        }
    }

    static Object cast(JsonValue value, Class<?> targetType) {
        if (value == null)
            return null;
        if (String.class.isAssignableFrom(targetType))
            if (value instanceof JsonString)
                return ((JsonString) value).getString();
            else
                return value.toString();
        if (Boolean.class.isAssignableFrom(targetType))
            if (value.getValueType() == TRUE)
                return true;
            else if (value.getValueType() == FALSE)
                return false;
        if (Character.class.isAssignableFrom(targetType))
            return (char) ((JsonNumber) value).intValue();
        if (Byte.class.isAssignableFrom(targetType))
            return (byte) ((JsonNumber) value).intValue();
        if (Short.class.isAssignableFrom(targetType))
            return (short) ((JsonNumber) value).intValue();
        if (Integer.class.isAssignableFrom(targetType))
            return ((JsonNumber) value).intValue();
        if (Long.class.isAssignableFrom(targetType))
            return ((JsonNumber) value).longValue();
        if (Float.class.isAssignableFrom(targetType))
            return (float) ((JsonNumber) value).doubleValue();
        if (Double.class.isAssignableFrom(targetType))
            return ((JsonNumber) value).doubleValue();
        throw new ClassCastException("Cannot cast " + value + " to " + targetType.getName());
    }

    private static class JsonScalarProperty extends JsonProperty {
        public JsonScalarProperty(String name) {
            super(scalar, name);
        }

        @Override
        public Scalar<JsonObject> getScalar() {
            checkKind(scalar);
            return new JsonScalar<>(getName(), object -> object.get(getName()));
        }
    }

    static class JsonSequenceProperty extends JsonProperty {
        public JsonSequenceProperty(String name) {
            super(sequence, name);
        }

        @Override
        @SuppressWarnings("unchecked")
        public Sequence<JsonObject> getSequence() {
            checkKind(sequence);
            return (Sequence) new JsonSequence(object -> ((JsonObject) object).getJsonArray(getName()));
        }
    }

    private class JsonMappingProperty extends JsonProperty {
        public JsonMappingProperty(String name) {
            super(mapping, name);
        }

        @Override
        public JsonMapping getMapping() {
            checkKind(mapping);
            return new JsonMapping((JsonObject) object.get(getName()), o -> (JsonObject) get(o));
        }

        private Object get(JsonObject o) {
            return backtrack.apply(o).getJsonObject(getName());
        }
    }
}
