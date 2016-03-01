package com.github.t1.meta;

import com.github.t1.meta.visitor.Guide;
import com.github.t1.meta.visitor.GuideFactory;

public class Meta {
    private final GuideFactory guideFactory = new GuideFactory();

    public Guide getGuideTo(Object object) {
        return guideFactory.guideTo(object);
    }
}
