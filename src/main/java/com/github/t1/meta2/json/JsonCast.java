package com.github.t1.meta2.json;

import static javax.json.JsonValue.ValueType.*;

import javax.json.*;

public class JsonCast {
    public static <T> T cast(Object value, Class<T> targetType) {
        return targetType.cast(innerCast((JsonValue) value, targetType));
    }

    private static Object innerCast(JsonValue value, Class<?> targetType) {
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
}
