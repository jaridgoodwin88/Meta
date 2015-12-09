package com.github.t1.meta2.reflection;

import java.lang.reflect.Array;
import java.util.*;
import java.util.function.Function;

import com.github.t1.meta2.*;

import lombok.*;

@RequiredArgsConstructor
class ObjectGlue<B> implements Scalar<B>, Sequence<B> {
    private final Function<B, Object> get;
    private final String toString;

    @Override
    public final <T> Optional<T> get(B object, Class<T> type) {
        return Optional.ofNullable(get.apply(object)).map(value -> cast(value, type));
    }

    private <T> T cast(Object value, Class<T> type) {
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
    private <T> T numericCast(Number number, Class<T> type) {
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
    private <T> T charCast(Object value) {
        return (T) (Character) (char) ((Number) value).shortValue();
    }

    @Override
    public int size(B object) {
        Object sequence = get.apply(object);
        if (sequence instanceof Collection)
            return ((Collection<?>) sequence).size();
        return Array.getLength(sequence);
    }

    @Override
    public Element<B> get(int index) {
        return new ObjectElement(index);
    }

    @RequiredArgsConstructor
    private class ObjectElement implements Element<B> {
        @Getter
        private final int index;

        @Override
        public StructureKind getKind() {
            // TODO Auto-generated method stub
            return null;
        }

        @Override
        public Scalar<B> getScalar() {
            return new ObjectGlue<>(object -> getElement(object), "element " + index);
        }

        private Object getElement(B object) {
            Object sequence = get.apply(object);
            if (sequence instanceof List)
                return ((List<?>) sequence).get(index);
            return Array.get(sequence, index);
        }

        @Override
        public Sequence<B> getSequence() {
            // TODO Auto-generated method stub
            return null;
        }

        @Override
        public Mapping<B> getMapping() {
            // TODO Auto-generated method stub
            return null;
        }
    }

    @Override
    public String toString() {
        return toString;
    }
}
