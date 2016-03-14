package com.github.t1.meta.visitor;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class LoggingVisitor extends VisitorDecorator {
    public LoggingVisitor(Visitor delegate) {
        super(delegate);
    }

    public void enterMapping() {
        log.debug("enterMapping");
        super.enterMapping();
    }

    public void continueMapping() {
        log.debug("continueMapping");
        super.continueMapping();
    }

    public void leaveMapping() {
        log.debug("leaveMapping");
        super.leaveMapping();
    }


    public void enterProperty(Object key) {
        log.debug("enterProperty: {}", key);
        super.enterProperty(key);
    }

    public void leaveProperty() {
        log.debug("leaveProperty");
        super.leaveProperty();
    }


    public void enterSequence() {
        log.debug("enterSequence");
        super.enterSequence();
    }

    public void enterItem() {
        log.debug("enterItem");
        super.enterItem();
    }

    public void continueSequence() {
        log.debug("continueSequence");
        super.continueSequence();
    }

    public void leaveItem() {
        log.debug("leaveItem");
        super.leaveItem();
    }

    public void leaveSequence() {
        log.debug("leaveSequence");
        super.leaveSequence();
    }


    public void visitScalar(Object value) {
        log.debug("visitScalar: {}", value);
        super.visitScalar(value);
    }
}
