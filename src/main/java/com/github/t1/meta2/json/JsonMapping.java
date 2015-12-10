package com.github.t1.meta2.json;

import static com.github.t1.meta2.StructureKind.*;
import static java.util.Collections.*;
import static javax.json.JsonValue.ValueType.*;

import java.util.*;

import javax.json.*;
import javax.json.JsonValue.ValueType;

import com.github.t1.meta2.*;
import com.github.t1.meta2.Sequence.Element;

import lombok.*;

@RequiredArgsConstructor
public class JsonMapping implements Mapping<JsonObject> {
    public static JsonMapping of(JsonObject object) {
        return new JsonMapping(object);
    }

    private final JsonObject object;
    private Map<String, JsonProperty> properties;

    @Override
    public Property<JsonObject> getProperty(String name) {
        return properties().get(name);
    }

    @Override
    public List<Property<JsonObject>> getProperties() {
        return unmodifiableList(new ArrayList<>(properties().values()));
    }

    private Map<String, JsonProperty> properties() {
        if (properties == null) {
            properties = new LinkedHashMap<>();
            object.forEach((name, value) -> properties.put(name, JsonProperty.of(value.getValueType(), name)));
        }
        return properties;
    }

    @Getter
    @RequiredArgsConstructor
    private static abstract class JsonProperty implements Property<JsonObject> {
        public static JsonProperty of(ValueType valueType, String name) {
            switch (kind(valueType)) {
            case scalar:
                return new JsonScalarProperty(name);
            case sequence:
                return new JsonSequenceProperty(name);
            case mapping:
                return new JsonMappingProperty(name);
            }
            throw new UnsupportedOperationException("unreachable code");
        }

        private final StructureKind kind;
        private final String name;

        @Override
        public String toString() {
            return "JSON " + kind + " property: " + name;
        }
    }

    private static StructureKind kind(ValueType valueType) {
        switch (valueType) {
        case STRING:
        case NUMBER:
        case FALSE:
        case TRUE:
        case NULL:
            return scalar;
        case ARRAY:
            return sequence;
        case OBJECT:
            return mapping;
        }
        throw new UnsupportedOperationException("unreachable code");
    }

    private static Object cast(JsonValue value, Class<?> targetType) {
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
            return new Scalar<JsonObject>() {
                @Override
                @SuppressWarnings("unchecked")
                public <T> Optional<T> get(JsonObject object, Class<T> type) {
                    JsonValue value = object.get(getName());
                    return Optional.of((T) cast(value, type));
                }

                @Override
                public String toString() {
                    return "JSON scalar: " + getName();
                }
            };
        }
    }

    private static class JsonSequenceProperty extends JsonProperty {
        @Getter
        @RequiredArgsConstructor
        private static class JsonElement implements Element<JsonObject> {
            private final int index;

            @Override
            public String toString() {
                return "JSON " + getKind() + " element " + index;
            }

            @Override
            public Scalar<JsonObject> getScalar() {
                return null;
            }

            @Override
            public StructureKind getKind() {
                return null;
            }
        }

        public JsonSequenceProperty(String name) {
            super(sequence, name);
        }

        @Override
        public Sequence<JsonObject> getSequence() {
            return new Sequence<JsonObject>() {
                @Override
                public int size(JsonObject object) {
                    return object.getJsonArray(getName()).size();
                }

                @Override
                public Element<JsonObject> get(int index) {
                    return new JsonElement(index);
                }
            };
        }
    }

    private static class JsonMappingProperty extends JsonProperty {
        public JsonMappingProperty(String name) {
            super(mapping, name);
        }
    }
}
