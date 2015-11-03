package com.github.t1.meta;

import java.util.Optional;
import java.util.function.Function;

/** A {@link Property} for Strings; useful for method overloading, which doesn't work with generics. */
public class StringProperty<B> extends Property<String, B> {
    public StringProperty(String id, String title, String description, Function<B, Optional<String>> backtrack) {
        super(id, title, description, backtrack);
    }
}
