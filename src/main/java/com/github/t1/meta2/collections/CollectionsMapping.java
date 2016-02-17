package com.github.t1.meta2.collections;

import static java.util.function.Function.*;

import java.util.Map;
import java.util.function.Function;

import com.github.t1.meta2.AbstractMapping;
import com.github.t1.meta2.util.JavaCast;

public class CollectionsMapping<B> extends AbstractMapping<B> {
    public CollectionsMapping() {
        this(identity());
    }

    public CollectionsMapping(Function<B, B> backtrack) {
        super(backtrack, CollectionsSequence::new, CollectionsMapping::new);
    }

    @SuppressWarnings("unchecked")
    protected <T> T backtrack(B object, String name) {
        Map<String, ?> backtracked = (Map<String, ?>) backtrack.apply(object);
        return (backtracked == null) ? null : (T) backtracked.get(name);
    }

    @Override
    protected <T> T cast(Object object, Class<T> type) {
        return JavaCast.cast(object, type);
    }
}
