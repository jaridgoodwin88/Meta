package com.github.t1.meta3.visitor;

import com.github.t1.meta3.Property;
import lombok.RequiredArgsConstructor;

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.stream.Stream;

@RequiredArgsConstructor
public abstract class MappingGuide extends Guide {
    private final GuideFactory guideFactory;

    @Override
    public void guide(Visitor visitor) {
        super.guide(visitor);
        visitor.enterMapping();
        AtomicBoolean first = new AtomicBoolean(true);
        getProperties().forEach(property -> {
            Object value = property.getValue();
            if (value != null) {
                if (!first.getAndSet(false))
                    visitor.continueMapping();
                visitor.enterProperty(property.getName());
                guideFactory.guideTo(value).guide(visitor);
                visitor.leaveProperty();
            }
        });
        visitor.leaveMapping();
    }

    protected abstract Stream<Property> getProperties();
}
