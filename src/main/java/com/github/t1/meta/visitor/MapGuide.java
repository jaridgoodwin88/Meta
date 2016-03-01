package com.github.t1.meta.visitor;

import lombok.RequiredArgsConstructor;

import java.util.Map;

@RequiredArgsConstructor
class MapGuide extends Guide {
    private final GuideFactory guideFactory;
    private final Map<?, ?> map;

    @Override
    public void guide(Visitor visitor) {
        super.guide(visitor);
        visitor.enterMapping();
        boolean first = true;
        for (Map.Entry<?, ?> entry : map.entrySet()) {
            Object value = entry.getValue();
            if (value == null)
                continue;
            if (first)
                first = false;
            else
                visitor.continueMapping();
            visitor.enterProperty(entry.getKey());
            guideFactory.guideTo(value).guide(visitor);
            visitor.leaveProperty();
        }
        visitor.leaveMapping();
    }

}
