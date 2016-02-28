package com.github.t1.meta3;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class VisitorDecorator extends Visitor {
    @NonNull
    private final Visitor delegate;

    @Override public void enterProperty(Object key) {
        delegate.enterProperty(key);
    }

    @Override public void enterProperty(String key) {
        delegate.enterProperty(key);
    }

    @Override public void leaveProperty() {
        delegate.leaveProperty();
    }

    @Override public void visitScalar(Object value) {
        delegate.visitScalar(value);
    }
}
