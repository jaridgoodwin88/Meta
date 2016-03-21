package com.github.t1.meta.visitor;

import com.github.t1.meta.Property;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class LoggingVisitor extends VisitorDecorator {
    public LoggingVisitor(Visitor delegate) {
        super(delegate);
    }

    @Override public void enterMapping() {
        log.debug("enterMapping");
        super.enterMapping();
    }

    @Override public void continueMapping() {
        log.debug("continueMapping");
        super.continueMapping();
    }

    @Override public void leaveMapping() {
        log.debug("leaveMapping");
        super.leaveMapping();
    }


    @Override public void enterProperty(Property property) {
        log.debug("enterProperty: {}", property);
        super.enterProperty(property);
    }

    @Override public void leaveProperty() {
        log.debug("leaveProperty");
        super.leaveProperty();
    }


    @Override public void enterSequence() {
        log.debug("enterSequence");
        super.enterSequence();
    }

    @Override public void enterItem(Object item) {
        log.debug("enterItem");
        super.enterItem(item);
    }

    @Override public void continueSequence() {
        log.debug("continueSequence");
        super.continueSequence();
    }

    @Override public void leaveItem() {
        log.debug("leaveItem");
        super.leaveItem();
    }

    @Override public void leaveSequence() {
        log.debug("leaveSequence");
        super.leaveSequence();
    }


    @Override public void visitScalar(Object value) {
        log.debug("visitScalar: {}", value);
        super.visitScalar(value);
    }
}
