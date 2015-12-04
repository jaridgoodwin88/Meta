package com.github.t1.meta2;

import java.util.Optional;

public interface Scalar {
    Optional<String> getStringValue(Object object);
}
