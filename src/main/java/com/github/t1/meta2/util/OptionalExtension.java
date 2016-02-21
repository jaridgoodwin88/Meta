package com.github.t1.meta2.util;

import java.lang.reflect.Array;
import java.util.AbstractList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

public class OptionalExtension {
    public static <T> Stream<T> stream(Optional<T> optional) {
        return optional.map(Stream::of).orElse(Stream.empty());
    }

    /**
     * @return the <code>i</code>th element of an array or List object.
     */
    public static <T> Optional<T> getSequenceElement(Optional<?> optional, int i) {
        return optional.map(o -> OptionalExtension.<T>toList(o).get(i));
    }

    @SuppressWarnings("unchecked")
    public static <T> List<T> toList(Object sequence) {
        if (sequence instanceof List)
            return (List<T>) sequence;
        if (sequence.getClass().isArray())
            return new AbstractList<T>() {
                @Override
                public T get(int index) {
                    return (T) Array.get(sequence, index);
                }

                @Override
                public int size() {
                    return Array.getLength(sequence);
                }
            };
        throw new RuntimeException("sequence is neither a list nor an array: " + sequence);
    }
}
