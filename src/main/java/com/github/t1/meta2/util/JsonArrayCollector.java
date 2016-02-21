package com.github.t1.meta2.util;

import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonArrayBuilder;
import java.util.Set;
import java.util.function.*;
import java.util.stream.Collector;

import static java.util.Collections.emptySet;

/**
 * A {@link Collector} into a {@link JsonArray}. Usage: <code>myStream.collect(asJsonArray())</code>
 */
public class JsonArrayCollector<T> implements Collector<T, JsonArrayBuilder, JsonArray> {
    /**
     * Combiner to be used for {@link java.util.stream.Stream#reduce(Object, BiFunction, BinaryOperator)},
     * when you would like to call {@link java.util.stream.Stream#reduce(Object, BinaryOperator)},
     * but with a different target/identity type.
     * IMPORTANT: Can only be used for non-parallel streams, so it will never be called!
     */
    public static <T> T neverCombine(T left, T right) {
        throw new UnsupportedOperationException("just a dummy");
    }

    public static <T> Collector<T, JsonArrayBuilder, JsonArray> toJsonArray() {
        return new JsonArrayCollector<>();
    }

    @Override
    public Supplier<JsonArrayBuilder> supplier() {
        return Json::createArrayBuilder;
    }

    @Override
    public BiConsumer<JsonArrayBuilder, T> accumulator() {
        return (builder, value) -> {
            if (value instanceof Integer)
                builder.add((Integer) value);
            else
                builder.add((String) value);
        };
    }

    @Override
    public BinaryOperator<JsonArrayBuilder> combiner() {
        return (left, right) -> {
            right.build().forEach(left::add);
            return left;
        };
    }

    @Override
    public Function<JsonArrayBuilder, JsonArray> finisher() {
        return JsonArrayBuilder::build;
    }

    @Override
    public Set<java.util.stream.Collector.Characteristics> characteristics() {
        return emptySet();
    }
}
