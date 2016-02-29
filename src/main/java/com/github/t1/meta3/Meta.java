package com.github.t1.meta3;

import java.util.Collection;
import java.util.Map;

public abstract class Meta {
    @SuppressWarnings("ChainOfInstanceofChecks") public static Guide createGuideTo(Object object) {
        if (object instanceof Map)
            return new MapGuide((Map<?, ?>) object);
        if (object instanceof Collection)
            return new CollectionGuide((Collection<?>) object);
        if (object.getClass().isArray())
            return new ArrayGuide(object);
        return new ReflectionGuide(object);
    }
}
