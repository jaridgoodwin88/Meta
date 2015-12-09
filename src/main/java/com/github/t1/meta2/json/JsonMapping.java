package com.github.t1.meta2.json;

import static com.github.t1.meta2.StructureKind.*;
import static java.util.Collections.*;
import static javax.json.JsonValue.ValueType.*;

import java.util.*;

import javax.json.*;

import com.github.t1.meta2.*;

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
            object.forEach((name, value) -> properties.put(name, jsonProperty(name, value)));
        }
        return properties;
    }

    private JsonProperty jsonProperty(String name, JsonValue value) {
        switch (value.getValueType()) {
        case STRING:
        case NUMBER:
        case FALSE:
        case TRUE:
        case NULL:
            return new JsonScalarProperty(name, value);
        case ARRAY:
            return new JsonSequenceProperty(name, value);
        case OBJECT:
            return new JsonMappingProperty(name, value);
        }
        throw new UnsupportedOperationException("unreachable code");
    }

    @Getter
    @RequiredArgsConstructor
    private static abstract class JsonProperty implements Property<JsonObject> {
        private final String name;
        private final StructureKind kind;

        @Override
        public Scalar<JsonObject> getScalar() {
            throw new IllegalStateException(this + " is a " + getKind() + ", not a scalar");
        }

        @Override
        public Sequence<JsonObject> getSequence() {
            throw new IllegalStateException(this + " is a " + getKind() + ", not a sequence");
        }

        @Override
        public Mapping<JsonObject> getMapping() {
            throw new IllegalStateException(this + " is a " + getKind() + ", not a mapping");
        }

        @Override
        public String toString() {
            return "JSON " + kind + " property: " + name;
        }
    }

    private static class JsonScalarProperty extends JsonProperty {
        private final JsonValue value;

        public JsonScalarProperty(String name, JsonValue value) {
            super(name, scalar);
            this.value = value;
        }

        @Override
        public Scalar<JsonObject> getScalar() {
            return new Scalar<JsonObject>() {
                @Override
                @SuppressWarnings("unchecked")
                public <T> Optional<T> get(JsonObject object, Class<T> type) {
                    assert object.equals(JsonScalarProperty.this.value);
                    return Optional.of((T) value(type));
                }

                private Object value(Class<?> targetType) {
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

                @Override
                public String toString() {
                    return "JSON scalar: " + JsonScalarProperty.this.getName();
                }
            };

        }
    }

    private static class JsonSequenceProperty extends JsonProperty {
        private final JsonValue value;

        public JsonSequenceProperty(String name, JsonValue value) {
            super(name, sequence);
            this.value = value;
        }
    }

    private static class JsonMappingProperty extends JsonProperty {
        private final JsonValue value;

        public JsonMappingProperty(String name, JsonValue value) {
            super(name, mapping);
            this.value = value;
        }
    }
}
