package com.github.t1.meta.test;

import java.util.Optional;
import java.util.function.Function;

import com.github.t1.meta.BooleanProperty;
import com.github.t1.meta.test.MetaAnnotationProcessorTest.Nested;

public class MetaAnnotationProcessorTest_NestedProperties<B> {
    public static MetaAnnotationProcessorTest_NestedProperties<Nested> metaAnnotationProcessorTest_NestedProperties() {
        return new MetaAnnotationProcessorTest_NestedProperties<>(source -> Optional.ofNullable(source));
    }

    private final Function<B, Optional<Nested>> backtrack;

    public MetaAnnotationProcessorTest_NestedProperties(Function<B, Optional<Nested>> backtrack) {
        this.backtrack = backtrack;
    }

    public String $id() {
        return "metaAnnotationProcessorTest.Nested";
    }

    public String $title() {
        return "Nested";
    }

    public String $description() {
        return "";
    }

    public BooleanProperty<B> someBooleanProperty() {
        return new BooleanProperty<>("someBooleanProperty", "Some Boolean Property", "",
                source -> this.backtrack.apply(source).map(container -> container.isSomeBooleanProperty()));
    }

}
