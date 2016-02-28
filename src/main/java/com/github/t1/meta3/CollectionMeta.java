package com.github.t1.meta3;

import lombok.RequiredArgsConstructor;

import java.util.Map;

@RequiredArgsConstructor
public class CollectionMeta extends Meta {
    private final Map<?, ?> map;

    @Override
    public void guide(Visitor visitor) {
        map.entrySet().forEach(entry -> guideToProperty(visitor, entry));
    }

    private void guideToProperty(Visitor visitor, Map.Entry<?, ?> entry) {
        visitor.enterProperty(entry.getKey());
        visitor.visitScalar(entry.getValue());
        visitor.leaveProperty();
    }
}
