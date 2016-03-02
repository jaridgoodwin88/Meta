package com.github.t1.meta.visitor;

import lombok.RequiredArgsConstructor;

import java.lang.reflect.Array;
import java.util.stream.Stream;

@RequiredArgsConstructor
class ArrayGuide extends SequenceGuide {
    private final Object array;

    @Override protected Stream<?> getItems() {
        Stream.Builder<Object> stream = Stream.builder();
        for (int i = 0; i < Array.getLength(array); i++)
            stream.add(Array.get(array, i));
        return stream.build();
    }
}
