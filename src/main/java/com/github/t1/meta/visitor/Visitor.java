package com.github.t1.meta.visitor;

import lombok.Getter;
import lombok.Setter;

public abstract class Visitor {
    @Getter
    @Setter
    protected Guide guide;

    public void enterMapping() {}

    public void continueMapping() {}

    public void leaveMapping() {}


    public void enterProperty(Object key) {
        enterProperty((key == null) ? null : key.toString());
    }

    public void enterProperty(String key) {}

    public void leaveProperty() {}


    public void enterSequence() {}

    public void continueSequence() {}

    public void leaveSequence() {}


    @SuppressWarnings("ChainOfInstanceofChecks") public void visitScalar(Object value) {
        if (value instanceof Boolean)
            visitBoolean((Boolean) value);
        else if (value instanceof Number)
            visitNumber((Number) value);
        else
            visitOtherScalar(value);
    }

    public void visitBoolean(Boolean value) {}

    public void visitNumber(Number value) {}

    public void visitOtherScalar(Object value) {}
}
