package com.github.t1.meta;

import java.lang.reflect.AnnotatedElement;

/** Meta data about a property */
public interface Property extends AnnotatedElement {
    Object name();
}
