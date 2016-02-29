package com.github.t1.meta3;

import lombok.RequiredArgsConstructor;

import java.util.Collection;

@RequiredArgsConstructor
class CollectionGuide extends Guide {
    private final Collection<?> list;

    @Override public void guide(Visitor visitor) {
        super.guide(visitor);
        visitor.enterSequence();
        list.forEach(visitor::visitScalar);
        visitor.leaveSequence();
    }
}
