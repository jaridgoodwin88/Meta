package com.github.t1.meta2.collections;

import static com.github.t1.meta2.util.JavaCast.*;

import java.util.function.Function;

import com.github.t1.meta2.AbstractSequence;
import com.github.t1.meta2.util.JavaCast;

class CollectionsSequence<B> extends AbstractSequence<B> {
    public CollectionsSequence(Function<B, B> backtrack) {
        super(backtrack, CollectionsSequence::new, CollectionsMapping::new);
    }

    @Override
    protected <T> T backtrack(B object, int i) {
        B sequence = backtrack.apply(object);
        return getSequenceElement(sequence, i);
    }

    @Override
    protected <T> T cast(Object object, Class<T> type) {
        return JavaCast.cast(object, type);
    }
}
