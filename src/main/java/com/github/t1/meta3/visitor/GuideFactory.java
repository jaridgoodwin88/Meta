package com.github.t1.meta3.visitor;

import java.util.Collection;
import java.util.Map;

public class GuideFactory {
    @SuppressWarnings("ChainOfInstanceofChecks") public Guide createGuideTo(Object object) {
        if (object instanceof Map)
            return new MapGuide((Map<?, ?>) object);
        if (object instanceof Collection)
            return new CollectionGuide((Collection<?>) object);
        if (object.getClass().isArray())
            return new ArrayGuide(object);
        return new ReflectionGuide(object);
    }
}
