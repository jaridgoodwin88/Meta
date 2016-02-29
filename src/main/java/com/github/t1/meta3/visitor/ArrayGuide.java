package com.github.t1.meta3.visitor;

import lombok.RequiredArgsConstructor;

import java.lang.reflect.Array;

@RequiredArgsConstructor
class ArrayGuide extends Guide {
    private final GuideFactory guideFactory;
    private final Object array;

    @Override public void guide(Visitor visitor) {
        super.guide(visitor);
        visitor.enterSequence();
        for (int i = 0; i < Array.getLength(array); i++) {
            if (i > 0)
                visitor.continueSequence();
            guideFactory.guideTo(Array.get(array, i)).guide(visitor);
        }
        visitor.leaveSequence();
    }
}
