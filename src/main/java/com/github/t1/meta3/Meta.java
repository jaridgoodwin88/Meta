package com.github.t1.meta3;

import java.util.Map;

public abstract class Meta {
    public static Meta of(Object object) {
        if (object instanceof Map)
            return new CollectionMeta((Map<?, ?>) object);
        return new ReflectionMeta(object);
    }

    public abstract void guide(Visitor visitor);
}
