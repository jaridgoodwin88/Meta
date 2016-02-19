package com.github.t1.meta2.collections;

import static com.github.t1.meta2.util.JavaCast.*;
import static java.util.function.Function.*;

import java.util.Map;
import java.util.function.Function;

import com.github.t1.meta2.*;
import com.github.t1.meta2.util.JavaCast;

public class CollectionMeta extends MetaFactory {
    public static Mapping<Map<String, ?>> mapping() {
        return new CollectionMeta().newCollectionsMapping();
    }

    private Mapping<Map<String, ?>> newCollectionsMapping() {
        return new CollectionsMapping<>(identity());
    }

    private static class CollectionsSequence<B> extends MetaFactory.AbstractSequence<B> {
        public CollectionsSequence(Function<B, B> backtrack) {
            super(backtrack, JavaCast::cast, CollectionsSequence::new, CollectionsMapping::new);
            this.resolve = (object, i) -> {
                B sequence = super.backtrack.apply(object);
                return getSequenceElement(sequence, i);
            };
        }
    }

    private static class CollectionsMapping<B> extends MetaFactory.AbstractMapping<B> {
        public CollectionsMapping(Function<B, B> backtrack) {
            super(backtrack, JavaCast::cast, CollectionsSequence::new, CollectionsMapping::new);
            this.resolve = (object, name) -> {
                @SuppressWarnings("unchecked")
                Map<String, B> backtracked = (Map<String, B>) backtrack.apply(object);
                return (backtracked == null) ? null : backtracked.get(name);
            };
        }
    }
}
