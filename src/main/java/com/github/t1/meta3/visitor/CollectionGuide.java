package com.github.t1.meta3.visitor;

import lombok.RequiredArgsConstructor;

import java.util.Collection;

@RequiredArgsConstructor
class CollectionGuide extends Guide {
    private final GuideFactory guideFactory;
    private final Collection<?> list;

    @Override public void guide(Visitor visitor) {
        super.guide(visitor);
        visitor.enterSequence();
        boolean first = true;
        for (Object element : list) {
            if (first)
                first = false;
            else
                visitor.continueSequence();
            guideFactory.guideTo(element).guide(visitor);
        }
        visitor.leaveSequence();
    }
}
