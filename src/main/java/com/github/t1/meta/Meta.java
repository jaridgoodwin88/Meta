package com.github.t1.meta;

import com.github.t1.meta.visitor.Guide;
import com.github.t1.meta.visitor.GuideFactory;
import org.joda.convert.StringConvert;

public class Meta {
    private final StringConvert convert = new StringConvert();
    private final GuideFactory guideFactory = new GuideFactory(convert);

    public Guide getGuideTo(Object object) {
        return guideFactory.guideTo(object);
    }
}
