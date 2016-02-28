package com.github.t1.meta2;

import static com.github.t1.meta2.util.OptionalExtension.*;
import static java.util.function.Function.*;
import static java.util.stream.Collectors.toList;

import java.util.*;
import java.util.function.Function;

import javax.json.*;

import com.github.t1.meta2.util.*;

public class MetaJson {
    public static Mapping<JsonObject> mapping() {
        return new JsonMapping<>(Structure.Path.ROOT, identity());
    }

    private static class JsonSequence<B extends JsonValue> extends AbstractContainer<B, Integer>
            implements Sequence<B> {
        public JsonSequence(Structure.Path path, Function<Optional<B>, Optional<B>> backtrack) {
            super(path, backtrack, JsonCast::cast, JsonSequence::new, JsonMapping::new,
                    (optional, i) -> optional.map(object -> {
                        JsonArray array = (JsonArray) object;
                        if (array == null || i >= array.size())
                            return null;
                        @SuppressWarnings("unchecked")
                        B backtracked = (B) array.get(i);
                        return backtracked;
                    }));
        }

        @Override
        @SuppressWarnings("unchecked")
        public <T> List<T> get(B object, Class<T> elementType) {
            return stream(backtrack.apply(Optional.of(object)))
                    .map(OptionalExtension::toList)
                    .flatMap(Collection::stream)
                    .flatMap(o -> ((Collection<T>) o).stream())
                    .map(element -> JsonCast.cast(element, elementType))
                    .collect(toList());
        }

        @Override
        public int size(B object) {
            return get(object, Object.class).size();
        }
    }

    private static class JsonMapping<B extends JsonValue> extends AbstractContainer<B, String>
            implements Mapping<B> {
        public JsonMapping(Structure.Path path, Function<Optional<B>, Optional<B>> backtrack) {
            super(path, backtrack, JsonCast::cast, JsonSequence::new, JsonMapping::new,
                    (optional, name) -> optional.map(object -> {
                        JsonObject backtracked = (JsonObject) object;
                        if (backtracked == null)
                            return null;
                        @SuppressWarnings("unchecked")
                        B eventuallyNullValue = (B) backtracked.get(name);
                        return eventuallyNullValue;
                    }));
        }
    }
}
