package com.github.t1.deployer.model;

import java.util.Optional;
import java.util.function.Function;

/** A {@link Property} for Booleans; useful for method overloading, which doesn't work with generics. */
public class BooleanProperty<B> extends Property<Boolean, B> {
    public BooleanProperty(String id, String title, String description, Function<B, Optional<Boolean>> backtrack) {
        super(id, title, description, backtrack);
    }
}
