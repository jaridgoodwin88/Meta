package com.github.t1.meta2.reflection;

import java.lang.reflect.Array;
import java.util.*;
import java.util.function.Function;

import com.github.t1.meta2.*;
import com.github.t1.meta2.Sequence.Element;

import lombok.*;

@RequiredArgsConstructor
class ObjectGlue {
    static class ObjectScalar<B> extends ObjectGlue implements Scalar<B> {
        private final Function<B, Object> backtrack;

        public ObjectScalar(Function<B, Object> backtrack, String toString) {
            super(toString);
            this.backtrack = backtrack;
        }

        @Override
        public final <T> Optional<T> get(B object, Class<T> type) {
            return Optional.ofNullable(backtrack.apply(object)).map(value -> cast(value, type));
        }
    }

    static class ObjectSequence<B> extends ObjectGlue implements Sequence<B> {
        private Function<B, Object> backtrack;

        public ObjectSequence(Function<B, Object> backtrack, String toString) {
            super(toString);
            this.backtrack = backtrack;
        }

        @Override
        public int size(B object) {
            Object sequence = backtrack.apply(object);
            if (sequence instanceof Collection)
                return ((Collection<?>) sequence).size();
            return Array.getLength(sequence);
        }

        @Override
        public Element<B> get(int index) {
            return new ObjectElement<>(backtrack, index, null);
        }
    }

    private final String toString;

    @SuppressWarnings("ChainOfInstanceofChecks")
    private static <T> T cast(Object value, Class<T> type) {
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
    private static <T> T numericCast(Number number, Class<T> type) {
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
    private static <T> T charCast(Object value) {
        return (T) (Character) (char) ((Number) value).shortValue();
    }

    @Getter
    @RequiredArgsConstructor
    private static class ObjectElement<B> implements Element<B> {
        @NonNull
        private final Function<B, Object> backtrack;
        @NonNull
        private final int index;
        private final StructureKind kind;

        @Override
        public Scalar<B> getScalar() {
            return new ObjectScalar<>(getElement(), toString());
        }

        @Override
        public Sequence<B> getSequence() {
            return new ObjectSequence<>(getElement(), toString());
        }

        @Override
        public Mapping<B> getMapping() {
            // TODO Auto-generated method stub
            return null;
        }

        private Function<B, Object> getElement() {
            return this::getElement;
        }

        private Object getElement(B object) {
            Object sequence = backtrack.apply(object);
            if (sequence == null || index >= size(sequence))
                return null;
            if (sequence instanceof List)
                return ((List<?>) sequence).get(index);
            else
                return Array.get(sequence, index);
        }

        private int size(Object sequence) {
            if (sequence instanceof List)
                return ((List<?>) sequence).size();
            return Array.getLength(sequence);
        }

        @Override
        public String toString() {
            return "element " + index;
        }
    }

    @Override
    public String toString() {
        return toString;
    }
}
