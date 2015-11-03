package com.github.t1.meta;

import java.util.Optional;
import java.util.function.Function;

import lombok.*;
import lombok.experimental.Accessors;

/**
 * Accessor for a property of an object.
 *
 * @param <T> The type of the property
 * @param <B> The 'backtrack' type, i.e. the type of object you'll have to pass into {@link #get(Object)} to fetch the
 *            actual value from. For a simple case, this is the object that has this property; but you can also chain
 *            the access to get the property of a property, and so on: For example: a class <code>Customer</code> has a
 *            property <code>address</code> of type <code>Address</code>; the class <code>Address</code> has a property
 *            <code>street</code> of type <code>String</code>. If you fetch that property from the customer by calling
 *            <code>CustomerProperties.customerProperties().customer().address()<code>, then the backtrack type is <code>Customer</code>
 *            so you'll have to pass a customer instance into {@link #get(Object)}. Note that the customer may be null,
 *            or the address of the customer may be null, or the street of the address may be null: You won't get an
 *            NPE, but simply an empty {@link Optional}.
 */
@RequiredArgsConstructor
@Getter
@Accessors(fluent = true)
public class Property<T, B> {
    private final String id;
    private final String title;
    private final String description;

    private final Function<B, Optional<T>> backtrack;

    /** @see Property */
    public Optional<T> get(B root) {
        return backtrack.apply(root);
    }
}
