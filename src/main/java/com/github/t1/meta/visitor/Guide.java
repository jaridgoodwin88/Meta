package com.github.t1.meta.visitor;

public abstract class Guide {
    public void guide(Visitor visitor) {
        if (visitor.getGuide() == null)
            visitor.setGuide(this);
    }
}
