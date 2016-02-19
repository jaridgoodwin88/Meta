package com.github.t1.meta2.collections;

import static java.util.function.Function.*;

import java.util.Map;
import java.util.function.Function;

import com.github.t1.meta2.*;
import com.github.t1.meta2.util.JavaCast;

public class CollectionMeta {
    public static Mapping<Map<String, ?>> mapping() {
        return new CollectionMeta().newCollectionsMapping();
    }

    private Mapping<Map<String, ?>> newCollectionsMapping() {
        return new CollectionMapping<>(identity());
    }

    private static class CollectionSequence<B> extends MetaFactory.AbstractSequence<B> {
        public CollectionSequence(Function<B, B> backtrack) {
            super(
                    backtrack,
                    JavaCast::cast,
                    CollectionSequence::new,
                    CollectionMapping::new,
                    JavaCast::getSequenceElement);
        }
    }

    private static class CollectionMapping<B> extends MetaFactory.AbstractMapping<B> {
        public CollectionMapping(Function<B, B> backtrack) {
            super(backtrack, JavaCast::cast, CollectionSequence::new, CollectionMapping::new,
                    (object, name) -> {
                        @SuppressWarnings("unchecked")
                        Map<String, B> map = (Map<String, B>) object;
                        return (map == null) ? null : map.get(name);
                    });
        }
    }
}
