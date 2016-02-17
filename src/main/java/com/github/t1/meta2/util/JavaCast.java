package com.github.t1.meta2.util;

import static java.util.Arrays.*;

import java.lang.reflect.Array;
import java.util.List;

public class JavaCast {
    public static final List<Class<?>> PRIMITIVE_WRAPPER_SCALARS =
            asList(Boolean.class, Character.class, Byte.class, Short.class,
                    Integer.class, Long.class, Float.class, Double.class);
    public static final List<Class<?>> PRIMITIVE_SCALARS = asList(boolean.class, char.class, byte.class, short.class,
            int.class, long.class, float.class, double.class);

    /**
     * Cast a value to a type, even if that means converting, e.g., a Long into a String (and much more).
     */
    @SuppressWarnings("ChainOfInstanceofChecks")
    public static <T> T cast(Object value, Class<T> type) {
        if (value == null)
            return null;
        if (Number.class.isAssignableFrom(type) && value instanceof Number)
            return numericCast((Number) value, type);
        if (Number.class.isAssignableFrom(type) && value instanceof Character)
            return numericCast((short) (char) value, type);
        if (Character.class.isAssignableFrom(type) && value instanceof Number)
            return charCast(value);
        if (CharSequence.class.isAssignableFrom(type) && value instanceof Character)
            return type.cast(Integer.toString((char) value));
        if (CharSequence.class.isAssignableFrom(type))
            value = value.toString();
        return type.cast(value);
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

    /** @return the <code>i</code>th element of an array or List object. */
    public static <T> T getSequenceElement(Object sequence, int i) {
        if (sequence == null)
            return null;
        if (sequence instanceof List) {
            List<?> list = (List<?>) sequence;
            if (i >= list.size())
                return null;
            @SuppressWarnings("unchecked")
            T backtracked = (T) list.get(i);
            return backtracked;
        }
        if (i >= Array.getLength(sequence))
            return null;
        @SuppressWarnings("unchecked")
        T backtracked = (T) Array.get(sequence, i);
        return backtracked;
    }
}
