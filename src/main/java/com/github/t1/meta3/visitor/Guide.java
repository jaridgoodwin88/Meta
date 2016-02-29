package com.github.t1.meta3.visitor;

public abstract class Guide {
    public void guide(Visitor visitor) {
        if (visitor.getGuide() == null)
            visitor.setGuide(this);
    }
}
