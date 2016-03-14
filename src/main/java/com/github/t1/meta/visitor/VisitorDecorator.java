package com.github.t1.meta.visitor;

import lombok.NonNull;

import java.math.*;

public class VisitorDecorator extends Visitor {
    @NonNull
    private final Visitor delegate;

    public VisitorDecorator(Visitor delegate) {
        this.delegate = delegate.self(this);
    }

    public Guide guide() { return visit.currentGuide(); }

    public void enterMapping() { delegate.enterMapping(); }

    public void continueMapping() { delegate.continueMapping(); }

    public void leaveMapping() { delegate.leaveMapping(); }


    public void enterProperty(Object key) { delegate.enterProperty(key); }

    public void enterProperty(String key) { delegate.enterProperty(key); }

    public void leaveProperty() { delegate.leaveProperty(); }


    public void enterSequence() { delegate.enterSequence(); }

    public void enterItem() { delegate.enterItem(); }

    public void continueSequence() { delegate.continueSequence(); }

    public void leaveItem() { delegate.leaveItem(); }

    public void leaveSequence() { delegate.leaveSequence(); }


    @SuppressWarnings("ChainOfInstanceofChecks")
    public void visitScalar(Object value) { delegate.visitScalar(value); }

    public void visitBoolean(boolean value) { delegate.visitBoolean(value); }

    @SuppressWarnings("ChainOfInstanceofChecks")
    public void visitNumber(Number value) { delegate.visitNumber(value); }

    public void visitNull() { delegate.visitNull(); }

    public void visitByte(byte value) { delegate.visitByte(value); }

    public void visitShort(short value) { delegate.visitShort(value); }

    public void visitInteger(int value) { delegate.visitInteger(value); }

    public void visitLong(long value) { delegate.visitLong(value); }

    public void visitFloat(float value) { delegate.visitFloat(value); }

    public void visitDouble(double value) { delegate.visitDouble(value); }

    public void visitBigDecimal(BigDecimal value) { delegate.visitBigDecimal(value); }

    public void visitBigInteger(BigInteger value) { delegate.visitBigInteger(value); }

    public void visitOtherNumber(Number value) { delegate.visitOtherNumber(value); }

    public void visitOtherScalar(Object value) { delegate.visitOtherScalar(value); }
}
