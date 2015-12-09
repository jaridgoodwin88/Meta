package com.github.t1.meta2.json;

import static java.util.Collections.*;

import java.util.Set;
import java.util.function.*;
import java.util.stream.Collector;

import javax.json.*;

public class JsonArrayCollector<T> implements Collector<T, JsonArrayBuilder, JsonArray> {
    public static <T> Collector<T, JsonArrayBuilder, JsonArray> asJsonArray() {
        return new JsonArrayCollector<>();
    }

    @Override
    public Supplier<JsonArrayBuilder> supplier() {
        return () -> Json.createArrayBuilder();
    }

    @Override
    public BiConsumer<JsonArrayBuilder, T> accumulator() {
        return (builder, value) -> {
            if (value instanceof Integer)
                builder.add((int) value);
            else
                builder.add((String) value);
        };
    }

    @Override
    public BinaryOperator<JsonArrayBuilder> combiner() {
        return (left, right) -> {
            right.build().forEach(value -> left.add(value));
            return left;
        };
    }

    @Override
    public Function<JsonArrayBuilder, JsonArray> finisher() {
        return builder -> builder.build();
    }

    @Override
    public Set<java.util.stream.Collector.Characteristics> characteristics() {
        return emptySet();
    }
}
