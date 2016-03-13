package com.github.t1.meta.visitor;

import java.util.stream.Stream;

public abstract class SequenceGuide extends Guide {
    @Override public void run(Visit visit) {
        Visitor visitor = visit.getVisitor();
        visitor.enterSequence();
        Continue continueMapping = new Continue(visitor::continueSequence);
        getItems().forEach(item -> {
            if (item == null) {
                visitor.visitNull();
            } else {
                continueMapping.call();
                visitor.enterItem();
                visit.to(item);
                visitor.leaveItem();
            }
        });
        visitor.leaveSequence();
    }

    protected abstract Stream<?> getItems();
}
