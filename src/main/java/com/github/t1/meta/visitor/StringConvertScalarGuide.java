package com.github.t1.meta.visitor;

import lombok.RequiredArgsConstructor;
import org.joda.convert.StringConvert;

@RequiredArgsConstructor
public class StringConvertScalarGuide extends Guide {
    private final StringConvert convert;
    private final Object object;

    @Override public void guide(Visitor visitor) {
        super.guide(visitor);
        visitor.visitScalar(object);
    }
}
