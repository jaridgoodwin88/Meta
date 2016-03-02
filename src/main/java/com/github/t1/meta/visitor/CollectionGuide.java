package com.github.t1.meta.visitor;

import lombok.RequiredArgsConstructor;

import java.util.Collection;
import java.util.stream.Stream;

@RequiredArgsConstructor
class CollectionGuide extends SequenceGuide {
    private final Collection<?> collection;

    @Override protected Stream<?> getItems() {
        return collection.stream();
    }
}
