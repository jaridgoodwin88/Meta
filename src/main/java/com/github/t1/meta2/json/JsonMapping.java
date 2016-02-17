package com.github.t1.meta2.json;

import static java.util.function.Function.*;

import java.util.function.Function;

import javax.json.*;

import com.github.t1.meta2.AbstractMapping;

public class JsonMapping<B extends JsonValue> extends AbstractMapping<B> {
    public JsonMapping() {
        this(identity());
    }

    public JsonMapping(Function<B, B> backtrack) {
        super(backtrack, JsonSequence::new, JsonMapping::new);
    }

    @Override
    protected <T> T backtrack(B object, String name) {
        JsonObject backtracked = (JsonObject) backtrack.apply(object);
        if (backtracked == null)
            return null;
        @SuppressWarnings("unchecked")
        T eventuallyNullValue = (T) backtracked.get(name);
        return eventuallyNullValue;
    }

    @Override
    protected <T> T cast(Object object, Class<T> type) {
        return JsonCast.cast((JsonValue) object, type);
    }
}
