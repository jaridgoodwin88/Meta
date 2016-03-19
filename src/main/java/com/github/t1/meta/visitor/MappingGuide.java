package com.github.t1.meta.visitor;

import java.util.stream.Stream;

abstract class MappingGuide extends Guide {
    @Override public void run(Visit visit) {
        Visitor visitor = visit.getVisitor();
        visitor.enterMapping();
        Continue continueMapping = new Continue(visitor::continueMapping);
        getProperties().forEach(property -> {
            Object value = property.getValue();
            if (value == null) {
                visitor.visitNull();
            } else {
                continueMapping.call();
                visitor.enterProperty(property);
                visit.to(value);
                visitor.leaveProperty();
            }
        });
        visitor.leaveMapping();
    }

    protected abstract Stream<PropertyWithValue> getProperties();
}
