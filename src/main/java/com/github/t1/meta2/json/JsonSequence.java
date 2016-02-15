package com.github.t1.meta2.json;

import java.util.function.Function;

import javax.json.*;

import com.github.t1.meta2.*;

import lombok.*;

@RequiredArgsConstructor
class JsonSequence<B> implements Sequence<B> {
    private final Function<B, B> backtrack;

    @Getter
    @RequiredArgsConstructor
    private class JsonElement implements Element<B> {
        private final int index;

        @Override
        public String toString() {
            return "JSON " + getKind() + " element " + index;
        }

        @Override
        public Scalar<B> getScalar() {
            // checkKind(scalar);
            return new JsonScalar<>(Integer.toString(index), this::get);
        }

        @Override
        @SuppressWarnings("unchecked")
        public Sequence<B> getSequence() {
            // checkKind(sequence);
            return new JsonSequence(this::get);
        }

        @Override
        public StructureKind getKind() {
            return null;
        }

        @SuppressWarnings("unchecked")
        private JsonValue get(Object object) {
            JsonArray array = array((B) object);
            return (array == null || index >= array.size()) ? null : array.get(index);
        }
    }

    @Override
    public int size(B object) {
        return array(object).size();
    }

    private JsonArray array(B object) {
        return (JsonArray) backtrack.apply(object);
    }

    @Override
    public Element<B> get(int index) {
        return new JsonElement(index);
    }
}
