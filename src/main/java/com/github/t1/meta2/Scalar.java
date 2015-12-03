package com.github.t1.meta2;

public interface Scalar extends ReadableScalar, WritableScalar {
    String getName();

    Scalar attach(Object object);
}
