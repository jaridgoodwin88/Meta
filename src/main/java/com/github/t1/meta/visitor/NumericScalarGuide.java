package com.github.t1.meta.visitor;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class NumericScalarGuide extends Guide {
    private final Object object;

    @Override public void guide(Visitor visitor) {
        super.guide(visitor);
        visitor.visitScalar(object);
    }
}
