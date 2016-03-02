package com.github.t1.meta;

import com.github.t1.meta.visitor.Guide;
import com.github.t1.meta.visitor.GuideFactory;
import com.github.t1.meta.visitor.Visitor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.joda.convert.StringConvert;

public class Meta {
    @Getter private final StringConvert convert = new StringConvert();
    @Getter private final GuideFactory guideFactory = new GuideFactory(convert);

    public VisitBuilder visitTo(Object object) {
        return new VisitBuilder(object);
    }

    @RequiredArgsConstructor
    public class VisitBuilder {
        private final Object destination;

        public Guide.Visit by(Visitor visitor) {
            return Guide.Visit.builder()
                    .guideFactory(guideFactory)
                    .destination(destination)
                    .visitor(visitor)
                    .build();
        }
    }
}
