package com.github.t1.meta2.json;

import java.util.Optional;
import java.util.function.Function;

import javax.json.JsonValue;

import com.github.t1.meta2.Scalar;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
class JsonScalar<B> implements Scalar<B> {
    private final String toStringInfo;
    private final Function<B, JsonValue> backtrack;

    @Override
    @SuppressWarnings("unchecked")
    public <T> Optional<T> get(B object, Class<T> type) {
        JsonValue value = backtrack.apply(object);
        return Optional.ofNullable((T) JsonMapping.cast(value, type));
    }

    @Override
    public String toString() {
        return "JSON scalar: " + toStringInfo;
    }
}
