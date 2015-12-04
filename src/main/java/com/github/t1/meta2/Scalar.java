package com.github.t1.meta2;

import java.util.Optional;

public interface Scalar<B> {
    <T> Optional<T> get(B object, Class<T> type);
}
