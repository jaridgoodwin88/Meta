package com.github.t1.meta.visitor;

import lombok.Getter;
import lombok.Setter;

public abstract class Visitor {
    @Getter
    @Setter
    protected Guide guide;

    public void enterProperty(Object key) {
        enterProperty((key == null) ? null : key.toString());
    }

    public void enterMapping() { }

    public void continueMapping() { }

    public void leaveMapping() { }


    public void enterProperty(String key) { }

    public void leaveProperty() { }


    public void enterSequence() { }

    public void continueSequence() { }

    public void leaveSequence() { }


    public void visitScalar(Object value) { }
}
