package com.github.t1.meta.visitor;

import lombok.RequiredArgsConstructor;

import java.util.stream.Stream;

@RequiredArgsConstructor
public abstract class SequenceGuide extends Guide {
    private final GuideFactory guideFactory;

    @Override public void guide(Visitor visitor) {
        super.guide(visitor);
        visitor.enterSequence();
        Continue continueMapping = new Continue(visitor::continueSequence);
        getItems().forEach(item -> {
                    continueMapping.call();
                    guideFactory.guideTo(item).guide(visitor);
                }
        );
        visitor.leaveSequence();
    }

    protected abstract Stream<?> getItems();
}
