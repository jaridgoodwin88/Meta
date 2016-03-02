package com.github.t1.meta.visitor;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.joda.convert.StringConvert;

import java.util.Collection;
import java.util.Map;

@RequiredArgsConstructor
public class GuideFactory {
    private final StringConvert convert;

    @SuppressWarnings("ChainOfInstanceofChecks") public Guide guideTo(@NonNull Object object) {
        if (convert.isConvertible(object.getClass()))
            return new StringConvertScalarGuide(convert, object);
        if (object instanceof Map)
            return new MapGuide(this, (Map<?, ?>) object);
        if (object instanceof Collection)
            return new CollectionGuide(this, (Collection<?>) object);
        if (object.getClass().isArray())
            return new ArrayGuide(this, object);
        return new ReflectionGuide(this, object);
    }
}
