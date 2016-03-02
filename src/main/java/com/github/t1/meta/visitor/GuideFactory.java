package com.github.t1.meta.visitor;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.joda.convert.StringConvert;

import java.util.Collection;
import java.util.Map;

@RequiredArgsConstructor
public class GuideFactory {
    private final StringConvert convert;

    public Guide getGuideTo(@NonNull Object object) {
        Guide guide = createGuide(object);
        guide.setFactory(this);
        return guide;
    }

    @SuppressWarnings("ChainOfInstanceofChecks")
    private Guide createGuide(@NonNull Object object) {
        if (convert.isConvertible(object.getClass()))
            return new ScalarGuide(convert, object);
        if (object instanceof Map)
            return new MapGuide((Map<?, ?>) object);
        if (object instanceof Collection)
            return new CollectionGuide((Collection<?>) object);
        if (object.getClass().isArray())
            return new ArrayGuide(object);
        return new ReflectionGuide(object);
    }
}
