package com.github.t1.meta.visitor;

import java.lang.reflect.Array;
import java.util.stream.Stream;

class ArrayGuide extends SequenceGuide {
    private final Object array;

    ArrayGuide(GuideFactory guideFactory, Object array) {
        super(guideFactory);
        this.array = array;
    }

    @Override protected Stream<?> getItems() {
        Stream.Builder<Object> stream = Stream.builder();
        for (int i = 0; i < Array.getLength(array); i++)
            stream.add(Array.get(array, i));
        return stream.build();
    }
}
