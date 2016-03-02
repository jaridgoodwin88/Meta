package com.github.t1.meta.visitor;

import lombok.RequiredArgsConstructor;
import org.joda.convert.StringConvert;

@RequiredArgsConstructor
public class ScalarGuide extends Guide {
    private final StringConvert convert;
    private final Object object;

    @Override public void run(Visit visit) {
        visit.getVisitor().visitScalar(object);
    }
}
