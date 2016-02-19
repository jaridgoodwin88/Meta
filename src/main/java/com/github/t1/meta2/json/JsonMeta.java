package com.github.t1.meta2.json;

import static java.util.function.Function.*;

import java.util.function.Function;

import javax.json.*;

import com.github.t1.meta2.*;

public class JsonMeta {
    public static Mapping<JsonObject> mapping() {
        return new JsonMapping<>(identity());
    }

    private static class JsonSequence<B extends JsonValue> extends MetaFactory.AbstractSequence<B> {
        public JsonSequence(Function<B, B> backtrack) {
            super(backtrack, JsonCast::cast, JsonSequence::new, JsonMapping::new);
            this.resolve = (object, i) -> {
                JsonArray array = (JsonArray) backtrack.apply(object);
                if (array == null || i >= array.size())
                    return null;
                @SuppressWarnings("unchecked")
                B backtracked = (B) array.get(i);
                return backtracked;
            };
        }
    }

    private static class JsonMapping<B extends JsonValue> extends MetaFactory.AbstractMapping<B> {
        public JsonMapping(Function<B, B> backtrack) {
            super(backtrack, JsonCast::cast, JsonSequence::new, JsonMapping::new);
            this.resolve = (object, name) -> {
                JsonObject backtracked = (JsonObject) backtrack.apply(object);
                if (backtracked == null)
                    return null;
                @SuppressWarnings("unchecked")
                B eventuallyNullValue = (B) backtracked.get(name);
                return eventuallyNullValue;
            };
        }
    }
}
