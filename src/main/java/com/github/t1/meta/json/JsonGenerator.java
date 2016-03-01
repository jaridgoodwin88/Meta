package com.github.t1.meta.json;

import com.github.t1.meta.visitor.Visitor;

public class JsonGenerator extends Visitor {
    private final StringBuilder out = new StringBuilder();

    @Override public void enterMapping() { out.append("{"); }

    @Override public void continueMapping() { out.append(","); }

    @Override public void leaveMapping() { out.append("}"); }


    @Override public void enterProperty(String key) { out.append("\"").append(key).append("\":"); }

    @Override public void leaveProperty() { }


    @Override public void enterSequence() { out.append("["); }

    @Override public void continueSequence() { out.append(","); }

    @Override public void leaveSequence() { out.append("]"); }


    @Override public void visitBoolean(Boolean value) { out.append(value); }

    @Override public void visitNumber(Number value) { out.append(value); }

    @Override public void visitOtherScalar(Object value) { out.append("\"").append(value).append("\""); }


    @Override public String toString() {
        return out.toString();
    }
}
