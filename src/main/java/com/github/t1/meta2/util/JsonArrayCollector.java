package com.github.t1.meta2.util;

import static java.util.Collections.*;

import java.util.Set;
import java.util.function.*;
import java.util.stream.Collector;

import javax.json.*;

/**
 * A {@link Collector} into a {@link JsonArray}. Usage: <code>myStream.collect(asJsonArray())</code>
 */
public class JsonArrayCollector<T> implements Collector<T, JsonArrayBuilder, JsonArray> {
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
