package com.github.t1.meta3;

import lombok.Getter;
import lombok.Setter;

public abstract class Visitor {
    @Getter @Setter
    protected Guide guide;

    public void enterProperty(Object key) {
        enterProperty((key == null) ? null : key.toString());
    }

    public abstract void enterProperty(String key);

    public abstract void leaveProperty();

    public abstract void visitScalar(Object value);
}
