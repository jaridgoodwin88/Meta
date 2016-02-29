package com.github.t1.meta3.visitor;

import lombok.RequiredArgsConstructor;

import java.util.Map;

@RequiredArgsConstructor
class MapGuide extends Guide {
    private final GuideFactory guideFactory;
    private final Map<?, ?> map;

    @Override
    public void guide(Visitor visitor) {
        if (map.isEmpty())
            return;
        super.guide(visitor);
        visitor.enterMapping();
        boolean first = true;
        for (Map.Entry<?, ?> entry : map.entrySet()) {
            if (first)
                first = false;
            else
                visitor.continueMapping();
            guideToProperty(visitor, entry);
        }
        visitor.leaveMapping();
    }

    private void guideToProperty(Visitor visitor, Map.Entry<?, ?> entry) {
        visitor.enterProperty(entry.getKey());
        guideFactory.guideTo(entry.getValue()).guide(visitor);
        visitor.leaveProperty();
    }
}
