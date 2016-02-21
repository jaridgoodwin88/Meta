package com.github.t1.meta2.util;

import java.util.List;

import static java.util.Arrays.asList;

public class JavaCast {
    public static final List<Class<?>> PRIMITIVE_WRAPPER_SCALARS =
            asList(Boolean.class, Character.class, Byte.class, Short.class,
                    Integer.class, Long.class, Float.class, Double.class);
    public static final List<Class<?>> PRIMITIVE_SCALARS = asList(boolean.class, char.class, byte.class, short.class,
            int.class, long.class, float.class, double.class);

    /**
     * Cast a value to a targetType, even if that means converting, e.g., a Long into a String (and much more).
     */
    @SuppressWarnings("ChainOfInstanceofChecks")
    public static <T> T cast(Object value, Class<T> targetType) {
        if (value == null)
            return null;
        if (Number.class.isAssignableFrom(targetType) && value instanceof Number)
            return numericCast((Number) value, targetType);
        if (Number.class.isAssignableFrom(targetType) && value instanceof Character)
            return numericCast((short) (char) value, targetType);
        if (Character.class.isAssignableFrom(targetType) && value instanceof Number)
            return charCast(value);
        if (CharSequence.class.isAssignableFrom(targetType) && value instanceof Character)
            return targetType.cast(Integer.toString((char) value));
        if (CharSequence.class.isAssignableFrom(targetType))
            value = value.toString();
        return targetType.cast(value);
    }

    @SuppressWarnings("unchecked")
    public static <T> T numericCast(Number number, Class<T> type) {
        if (Byte.class.equals(type))
            return (T) (Byte) number.byteValue();
        if (Short.class.equals(type))
            return (T) (Short) number.shortValue();
        if (Integer.class.equals(type))
            return (T) (Integer) number.intValue();
        if (Long.class.equals(type))
            return (T) (Long) number.longValue();
        if (Float.class.equals(type))
            return (T) (Float) number.floatValue();
        if (Double.class.equals(type))
            return (T) (Double) number.doubleValue();
        return type.cast(number);
    }

    @SuppressWarnings("unchecked")
    public static <T> T charCast(Object value) {
        return (T) (Character) (char) ((Number) value).shortValue();
    }
}
