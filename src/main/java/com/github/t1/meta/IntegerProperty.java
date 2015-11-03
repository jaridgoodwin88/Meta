package com.github.t1.meta;

import java.util.Optional;
import java.util.function.Function;

/** A {@link Property} for Integers; useful for method overloading, which doesn't work with generics. */
public class IntegerProperty<B> extends Property<Integer, B> {
    public IntegerProperty(String id, String title, String description, Function<B, Optional<Integer>> backtrack) {
        super(id, title, description, backtrack);
    }
}
