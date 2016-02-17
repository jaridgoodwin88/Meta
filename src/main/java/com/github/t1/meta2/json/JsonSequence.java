package com.github.t1.meta2.json;

import java.util.function.Function;

import javax.json.*;

import com.github.t1.meta2.AbstractSequence;

class JsonSequence<B extends JsonValue> extends AbstractSequence<B> {
    public JsonSequence(Function<B, B> backtrack) {
        super(backtrack, JsonSequence::new, JsonMapping::new);
    }

    @Override
    protected <T> T backtrack(B object, int i) {
        JsonArray array = (JsonArray) backtrack.apply(object);
        if (array == null || i >= array.size())
            return null;
        @SuppressWarnings("unchecked")
        T backtracked = (T) array.get(i);
        return backtracked;
    }

    @Override
    protected <T> T cast(Object object, Class<T> type) {
        return JsonCast.cast((JsonValue) object, type);
    }
}
