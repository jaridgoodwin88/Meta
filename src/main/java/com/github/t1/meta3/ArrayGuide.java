package com.github.t1.meta3;

import lombok.RequiredArgsConstructor;

import java.lang.reflect.Array;

@RequiredArgsConstructor
class ArrayGuide extends Guide {
    private final Object array;

    @Override public void guide(Visitor visitor) {
        super.guide(visitor);
        visitor.enterSequence();
        for (int i = 0; i < Array.getLength(array); i++)
            visitor.visitScalar(Array.get(array, i));
        visitor.leaveSequence();
    }
}
