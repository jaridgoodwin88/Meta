package com.github.t1.meta.visitor;

import lombok.*;
import org.joda.convert.StringConvert;

import java.util.*;

@RequiredArgsConstructor
public class GuideFactory {
    private final StringConvert convert;

    @SuppressWarnings("ChainOfInstanceofChecks")
    public Guide getGuideTo(@NonNull Object object) {
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
