package com.github.t1.meta3.visitor;

public abstract class Guide {
    public void guide(Visitor visitor) {
        visitor.setGuide(this);
    }
}
