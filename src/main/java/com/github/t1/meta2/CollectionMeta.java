package com.github.t1.meta2;

import com.github.t1.meta2.util.JavaCast;
import com.github.t1.meta2.util.OptionalExtension;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;

import static java.util.Collections.emptyList;
import static java.util.function.Function.identity;
import static java.util.stream.Collectors.toList;

public class CollectionMeta {
    public static Mapping<Map<String, ?>> mapping() {
        return new CollectionMapping<>(Structure.Path.ROOT, identity());
    }

    private static class CollectionSequence<B> extends AbstractContainer<B, Integer>
            implements Sequence<B> {
        public CollectionSequence(Structure.Path path, Function<Optional<B>, Optional<B>> backtrack) {
            super(path,
                    backtrack,
                    JavaCast::cast,
                    CollectionSequence::new,
                    CollectionMapping::new,
                    OptionalExtension::getSequenceElement
            );
        }

        @Override
        @SuppressWarnings("unchecked")
        public <T> List<T> get(B object, Class<T> elementType) {
            Optional<?> backtracked = backtrack.apply(Optional.of(object));
            if (!backtracked.isPresent())
                return emptyList();
            return OptionalExtension.toList(backtracked.get()).stream()
                    .map(element -> JavaCast.cast(element, elementType))
                    .collect(toList());
        }

        @Override
        public int size(B object) {
            return get(object, Object.class).size();
        }
    }

    private static class CollectionMapping<B> extends AbstractContainer<B, String> implements Mapping<B> {
        public CollectionMapping(Structure.Path path, Function<Optional<B>, Optional<B>> backtrack) {
            super(path, backtrack, JavaCast::cast, CollectionSequence::new, CollectionMapping::new,
                    (optional, name) -> optional.map(object -> resolve(path, name, object)));
        }

        private static <B> B resolve(Structure.Path path, String name, B object) {
            if (!(object instanceof Map))
                throw new RuntimeException("expected " + path.with(name) + " to be a map, "
                        + "but found a " + object.getClass());
            @SuppressWarnings("unchecked")
            Map<String, B> map = (Map<String, B>) object;
            return map.get(name);
        }
    }
}
