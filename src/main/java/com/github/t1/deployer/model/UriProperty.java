package com.github.t1.deployer.model;

import java.net.URI;
import java.util.Optional;
import java.util.function.Function;

/** A {@link Property} for URIs; useful for method overloading, which doesn't work with generics. */
public class UriProperty<B> extends Property<URI, B> {
    public UriProperty(String id, String title, String description, Function<B, Optional<URI>> backtrack) {
        super(id, title, description, backtrack);
    }
}
