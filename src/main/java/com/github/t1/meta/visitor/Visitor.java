package com.github.t1.meta.visitor;

import lombok.*;

import java.math.*;

/**
 * This is <em>not</em> the GoF Visitor pattern, as we can't do the polymorphic type resolution
 * trick with types we can't control. Instead we are forced to use <code>instanceof</code>.
 */
@SuppressWarnings("UnusedParameters")
public abstract class Visitor {
    @Getter @Setter private Visitor self = this;
    @Getter @Setter private Guide.Visit visit;

    public Guide guide() { return self.visit.currentGuide(); }

    public Object destination() { return guide().destination(); }


    public void enterMapping() {}

    public void continueMapping() {}

    public void leaveMapping() {}


    public void enterProperty(Object key) {
        self.enterProperty((key == null) ? null : key.toString());
    }

    public void enterProperty(String key) {}

    public void leaveProperty() {}


    public void enterSequence() {}

    public void enterItem() {}

    public void continueSequence() {}

    public void leaveItem() {}

    public void leaveSequence() {}


    @SuppressWarnings("ChainOfInstanceofChecks") public void visitScalar(Object value) {
        if (value instanceof Boolean)
            self.visitBoolean((Boolean) value);
        else if (value instanceof Number)
            self.visitNumber((Number) value);
        else
            self.visitOtherScalar(value);
    }

    public void visitBoolean(boolean value) {}

    @SuppressWarnings("ChainOfInstanceofChecks") public void visitNumber(Number value) {
        if (value instanceof Byte)
            self.visitByte((byte) value);
        else if (value instanceof Short)
            self.visitShort((short) value);
        else if (value instanceof Integer)
            self.visitInteger((int) value);
        else if (value instanceof Long)
            self.visitLong((long) value);
        else if (value instanceof Float)
            self.visitFloat((float) value);
        else if (value instanceof Double)
            self.visitDouble((double) value);
        else if (value instanceof BigDecimal)
            self.visitBigDecimal((BigDecimal) value);
        else if (value instanceof BigInteger)
            self.visitBigInteger((BigInteger) value);
        else
            self.visitOtherNumber(value);
    }

    public void visitNull() {}

    public void visitByte(byte value) {}

    public void visitShort(short value) {}

    public void visitInteger(int value) {}

    public void visitLong(long value) {}

    public void visitFloat(float value) {}

    public void visitDouble(double value) {}

    public void visitBigDecimal(BigDecimal value) {}

    public void visitBigInteger(BigInteger value) {}

    public void visitOtherNumber(Number value) {}

    public void visitOtherScalar(Object value) {}
}
