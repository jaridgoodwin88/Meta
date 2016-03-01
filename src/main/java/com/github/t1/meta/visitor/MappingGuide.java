package com.github.t1.meta.visitor;

import com.github.t1.meta.Property;
import lombok.RequiredArgsConstructor;

import java.util.stream.Stream;

@RequiredArgsConstructor
public abstract class MappingGuide extends Guide {
    private final GuideFactory guideFactory;

    @Override
    public void guide(Visitor visitor) {
        super.guide(visitor);
        visitor.enterMapping();
        Continue continueMapping = new Continue(visitor::continueMapping);
        getProperties().forEach(property -> {
            Object value = property.getValue();
            if (value != null) {
                continueMapping.call();
                visitor.enterProperty(property.getName());
                guideFactory.guideTo(value).guide(visitor);
                visitor.leaveProperty();
            }
        });
        visitor.leaveMapping();
    }

    protected abstract Stream<Property> getProperties();
}
