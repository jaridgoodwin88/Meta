package com.github.t1.meta2.json;

import static com.github.t1.meta2.json.JsonCast.*;
import static java.util.function.Function.*;

import java.util.Optional;
import java.util.function.Function;

import javax.json.*;

import com.github.t1.meta2.*;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class JsonMapping<B extends JsonValue> implements Mapping<B> {
    private final Function<B, B> backtrack;

    public JsonMapping() {
        this(identity());
    }

    @Override
    public Scalar<B> getScalar(String name) {
        return new Scalar<B>() {
            @Override
            public <T> Optional<T> get(B object, Class<T> type) {
                return Optional.ofNullable(cast(backtrack(object, name), type));
            }
        };
    }

    @Override
    public Sequence<B> getSequence(String name) {
        return new JsonSequence<>(object -> backtrack(object, name));
    }

    @Override
    public Mapping<B> getMapping(String name) {
        return new JsonMapping<>(object -> backtrack((B) object, name));
    }

    private B backtrack(B object, String name) {
        JsonObject backtracked = (JsonObject) backtrack.apply(object);
        if (backtracked == null)
            return null;
        @SuppressWarnings("unchecked")
        B eventuallyNullValue = (B) backtracked.get(name);
        return eventuallyNullValue;
    }
}
