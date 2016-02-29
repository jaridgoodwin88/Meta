package com.github.t1.meta3;

import com.github.t1.meta3.visitor.Guide;
import com.github.t1.meta3.visitor.GuideFactory;

public class Meta {
    private final GuideFactory guideFactory = new GuideFactory();

    public Guide createGuideTo(Object object) {
        return guideFactory.createGuideTo(object);
    }
}
