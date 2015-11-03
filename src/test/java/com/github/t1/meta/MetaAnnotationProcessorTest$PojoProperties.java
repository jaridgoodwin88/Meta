package com.github.t1.meta;

import java.util.Optional;
import java.util.function.Function;

import com.github.t1.meta.MetaAnnotationProcessorTest.Pojo;

public class MetaAnnotationProcessorTest$PojoProperties<B> {
    public static MetaAnnotationProcessorTest$PojoProperties<Pojo> metaAnnotationProcessorTest$PojoProperties() {
        return new MetaAnnotationProcessorTest$PojoProperties<>(source -> Optional.ofNullable(source));
    }

    private final Function<B, Optional<Pojo>> backtrack;

    public MetaAnnotationProcessorTest$PojoProperties(Function<B, Optional<Pojo>> backtrack) {
        this.backtrack = backtrack;
    }

    public StringProperty<B> value() {
        return new StringProperty<>("value", "Value", "",
                source -> this.backtrack.apply(source).map(container -> container.value));
    }

    public IntegerProperty<B> intValue() {
        return new IntegerProperty<>("intValue", "Int Value", "",
                source -> this.backtrack.apply(source).map(container -> container.intValue));
    }

}
