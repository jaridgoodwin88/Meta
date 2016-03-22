package com.github.t1.meta.builder;

import lombok.RequiredArgsConstructor;
import org.joda.convert.StringConvert;

import javax.enterprise.util.TypeLiteral;
import java.util.Map;

@RequiredArgsConstructor
public class BuilderFactory {
    private final StringConvert convert;

    public <T> Builder<T> builderFor(Class<T> type) {
        return new ReflectionBuilder<>(type);
    }

    @SuppressWarnings("unchecked")
    public <T> Builder<T> builderFor(TypeLiteral<T> type) {
        return (Builder<T>) (Map.class.isAssignableFrom(type.getRawType())
                                     ? new MapBuilder()
                                     : new ReflectionBuilder(type.getRawType()));
    }
}
