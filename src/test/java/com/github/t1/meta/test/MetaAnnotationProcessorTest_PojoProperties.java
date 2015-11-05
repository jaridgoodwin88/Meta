package com.github.t1.meta.test;

import java.util.Optional;
import java.util.function.Function;

import com.github.t1.meta.*;
import com.github.t1.meta.test.MetaAnnotationProcessorTest.Pojo;

public class MetaAnnotationProcessorTest_PojoProperties<B> {
    public static MetaAnnotationProcessorTest_PojoProperties<Pojo> metaAnnotationProcessorTest_PojoProperties() {
        return new MetaAnnotationProcessorTest_PojoProperties<>(source -> Optional.ofNullable(source));
    }

    private final Function<B, Optional<Pojo>> backtrack;

    public MetaAnnotationProcessorTest_PojoProperties(Function<B, Optional<Pojo>> backtrack) {
        this.backtrack = backtrack;
    }

    public String $id() {
        return "metaAnnotationProcessorTest.Pojo";
    }

    public String $title() {
        return "Pojo";
    }

    public String $description() {
        return "pojo javadoc";
    }

    public StringProperty<B> publicValue() {
        return new StringProperty<>("publicValue", "Public Value", "public value javadoc",
                source -> this.backtrack.apply(source).map(container -> container.publicValue));
    }

    public IntegerProperty<B> intValueWithGetter() {
        return new IntegerProperty<>("intValueWithGetter", "Int Value With Getter", "",
                source -> this.backtrack.apply(source).map(container -> container.getIntValueWithGetter()));
    }

    public UriProperty<B> uriWithFluentGetter() {
        return new UriProperty<>("uriWithFluentGetter", "Uri With Fluent Getter", "",
                source -> this.backtrack.apply(source).map(container -> container.uriWithFluentGetter()));
    }

    public MetaAnnotationProcessorTest_NestedProperties<B> nested() {
        Function<B, Optional<MetaAnnotationProcessorTest.Nested>> backtrack =
                source -> this.backtrack.apply(source).map(container -> container.nested);
        return new MetaAnnotationProcessorTest_NestedProperties<>(backtrack);
    }

}
