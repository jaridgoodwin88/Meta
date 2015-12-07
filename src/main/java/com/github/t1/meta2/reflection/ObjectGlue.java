package com.github.t1.meta2.reflection;

import java.lang.reflect.Array;
import java.util.*;
import java.util.function.Function;

import com.github.t1.meta2.*;

import lombok.*;

@RequiredArgsConstructor
class ObjectGlue<B> implements Scalar<B>, Sequence<B> {
    private final Function<B, Object> get;

    @Override
    public final <T> Optional<T> get(B object, Class<T> type) {
        return Optional.ofNullable(get.apply(object)).map(value -> cast(value, type));
    }

    private <T> T cast(Object value, Class<T> type) {
        if (CharSequence.class.isAssignableFrom(type))
            value = value.toString();
        return type.cast(value);
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
            return new ObjectGlue<>(object -> getElement(object));
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
}
