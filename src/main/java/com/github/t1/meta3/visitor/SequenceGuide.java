package com.github.t1.meta3.visitor;

import lombok.RequiredArgsConstructor;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Stream;

@RequiredArgsConstructor
public abstract class SequenceGuide extends Guide {
    private final GuideFactory guideFactory;

    @Override public void guide(Visitor visitor) {
        super.guide(visitor);
        visitor.enterSequence();
        AtomicInteger index = new AtomicInteger(0);
        getItems().forEach(item -> {
                    if (index.getAndIncrement() > 0)
                        visitor.continueSequence();
                    guideFactory.guideTo(item).guide(visitor);
                }
        );
        visitor.leaveSequence();
    }

    protected abstract Stream<?> getItems();
}
