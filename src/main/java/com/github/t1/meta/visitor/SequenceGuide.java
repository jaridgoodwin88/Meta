package com.github.t1.meta.visitor;

import java.util.stream.Stream;

public abstract class SequenceGuide extends Guide {
    @Override public void run(Visit visit) {
        Visitor visitor = visit.getVisitor();
        visitor.enterSequence();
        Continue continueMapping = new Continue(visitor::continueSequence);
        getItems().forEach(item -> {
            continueMapping.call();
            visit.to(item);
        });
        visitor.leaveSequence();
    }

    protected abstract Stream<?> getItems();
}
