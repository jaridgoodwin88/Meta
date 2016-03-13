package com.github.t1.meta.visitor;

import lombok.Setter;

import java.math.*;

/**
 * This is <em>not</em> the GoF Visitor pattern, as we can't do the polymorphic type resolution
 * trick with types we can't control. Instead we are forced to use <code>instanceof</code>.
 */
@SuppressWarnings("UnusedParameters")
public abstract class Visitor {
    @Setter Guide.Visit visit;

    public Guide guide() { return visit.currentGuide(); }

    public void enterMapping() {}

    public void continueMapping() {}

    public void leaveMapping() {}


    public void enterProperty(Object key) {
        enterProperty((key == null) ? null : key.toString());
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
            visitBoolean((Boolean) value);
        else if (value instanceof Number)
            visitNumber((Number) value);
        else
            visitOtherScalar(value);
    }

    public void visitBoolean(boolean value) {}

    @SuppressWarnings("ChainOfInstanceofChecks") public void visitNumber(Number value) {
        if (value instanceof Byte)
            visitByte((byte) value);
        else if (value instanceof Short)
            visitShort((short) value);
        else if (value instanceof Integer)
            visitInteger((int) value);
        else if (value instanceof Long)
            visitLong((long) value);
        else if (value instanceof Float)
            visitFloat((float) value);
        else if (value instanceof Double)
            visitDouble((double) value);
        else if (value instanceof BigDecimal)
            visitBigDecimal((BigDecimal) value);
        else if (value instanceof BigInteger)
            visitBigInteger((BigInteger) value);
        else
            visitOtherNumber(value);
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
