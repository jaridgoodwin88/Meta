package com.github.t1.meta3.visitor;

import lombok.NonNull;

import java.util.Collection;
import java.util.Map;

public class GuideFactory {
    @SuppressWarnings("ChainOfInstanceofChecks") public Guide guideTo(@NonNull Object object) {
        if (object instanceof Map)
            return new MapGuide(this, (Map<?, ?>) object);
        if (object instanceof Collection)
            return new CollectionGuide((Collection<?>) object);
        if (object.getClass().isArray())
            return new ArrayGuide(object);
        if (object instanceof String)
            return new ScalarGuide(object);
        return new ReflectionGuide(this, object);
    }
}
