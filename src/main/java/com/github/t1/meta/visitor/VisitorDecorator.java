package com.github.t1.meta.visitor;

import lombok.*;

import java.math.*;

public class VisitorDecorator extends Visitor {
    @NonNull @Getter private Visitor delegate;

    public VisitorDecorator(Visitor delegate) { setDelegate(delegate); }

    public void setDelegate(Visitor delegate) {
        this.delegate = delegate;
        delegate.setSelf(this.getSelf());
        delegate.setVisit(this.getSelf().getVisit());
    }

    @Override public void setSelf(Visitor self) {
        delegate.setSelf(self);
        super.setSelf(self);
    }

    @Override public void setVisit(Guide.Visit visit) {
        delegate.setVisit(visit);
        super.setVisit(visit);
    }

    @Override public Guide guide() { return delegate.guide(); }

    @Override public void enterMapping() { delegate.enterMapping(); }

    @Override public void continueMapping() { delegate.continueMapping(); }

    @Override public void leaveMapping() { delegate.leaveMapping(); }


    @Override public void enterProperty(Object key) { delegate.enterProperty(key); }

    @Override public void enterProperty(String key) { delegate.enterProperty(key); }

    @Override public void leaveProperty() { delegate.leaveProperty(); }


    @Override public void enterSequence() { delegate.enterSequence(); }

    @Override public void enterItem() { delegate.enterItem(); }

    @Override public void continueSequence() { delegate.continueSequence(); }

    @Override public void leaveItem() { delegate.leaveItem(); }

    @Override public void leaveSequence() { delegate.leaveSequence(); }


    @Override public void visitScalar(Object value) { delegate.visitScalar(value); }

    @Override public void visitBoolean(boolean value) { delegate.visitBoolean(value); }

    @Override public void visitNumber(Number value) { delegate.visitNumber(value); }

    @Override public void visitNull() { delegate.visitNull(); }

    @Override public void visitByte(byte value) { delegate.visitByte(value); }

    @Override public void visitShort(short value) { delegate.visitShort(value); }

    @Override public void visitInteger(int value) { delegate.visitInteger(value); }

    @Override public void visitLong(long value) { delegate.visitLong(value); }

    @Override public void visitFloat(float value) { delegate.visitFloat(value); }

    @Override public void visitDouble(double value) { delegate.visitDouble(value); }

    @Override public void visitBigDecimal(BigDecimal value) { delegate.visitBigDecimal(value); }

    @Override public void visitBigInteger(BigInteger value) { delegate.visitBigInteger(value); }

    @Override public void visitOtherNumber(Number value) { delegate.visitOtherNumber(value); }

    @Override public void visitOtherScalar(Object value) { delegate.visitOtherScalar(value); }
}
