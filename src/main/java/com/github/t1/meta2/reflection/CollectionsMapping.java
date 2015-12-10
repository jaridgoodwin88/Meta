package com.github.t1.meta2.reflection;

import static com.github.t1.meta2.reflection.ReflectionMapping.*;
import static java.util.Collections.*;

import java.util.*;
import java.util.function.Function;

import com.github.t1.meta2.Mapping;

import lombok.*;

@RequiredArgsConstructor
public class CollectionsMapping<B extends Map<String, ?>> implements Mapping<B> {
    public static <B extends Map<String, ?>> CollectionsMapping<B> of(B map) {
        return new CollectionsMapping<>(map, identity());
    }

    private final B map;
    private final Function<B, B> backtrack;
    private Map<String, Property<B>> properties;

    @ToString
    private class MapProperty extends ObjectProperty<B> {
        @Getter
        private final String name;

        public MapProperty(String name) {
            super(map.get(name).getClass(), "map element " + name);
            this.name = name;
        }

        @Override
        protected Object get(B map) {
            return backtrack.apply(map).get(name);
        }

        @Override
        @SuppressWarnings("unchecked")
        protected Mapping<B> createMapping() {
            return new CollectionsMapping<>((B) map.get(name), object -> (B) get(object));
        }
    }

    @Override
    public List<Property<B>> getProperties() {
        return unmodifiableList(new ArrayList<>(properties().values()));
    }

    @Override
    public Property<B> getProperty(String name) {
        return properties().get(name);
    }

    private Map<String, Property<B>> properties() {
        if (properties == null) {
            this.properties = new LinkedHashMap<>();
            for (String name : map.keySet())
                properties.put(name, new MapProperty(name));
        }
        return properties;
    }

}
