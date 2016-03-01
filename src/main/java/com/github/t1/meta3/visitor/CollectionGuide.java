package com.github.t1.meta3.visitor;

import java.util.Collection;
import java.util.stream.Stream;

class CollectionGuide extends SequenceGuide {
    private final Collection<?> collection;

    public CollectionGuide(GuideFactory guideFactory, Collection<?> collection) {
        super(guideFactory);
        this.collection = collection;
    }

    @Override protected Stream<?> getItems() {
        return collection.stream();
    }
}
