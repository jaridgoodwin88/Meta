package com.github.t1.meta;


import com.github.t1.meta.builder.Builder;
import com.github.t1.meta.builder.*;
import com.github.t1.meta.visitor.*;
import lombok.*;
import org.joda.convert.StringConvert;

import javax.enterprise.util.TypeLiteral;

/**
 * This is the main entry point.
 * # Main Title
 *
 * new Meta().visitTo(object).by(visitor).run();
 *
 * ```java
 * public class FencedCodeBlock {
 * public void cool() {
 * new Meta().visitTo(object).by(visitor).run();
 * }
 * }
 * ```
 *
 * Foo | Bar
 * ----|----
 * A   | B
 * C   | D
 *
 * ![Example Diagram](example.png)
 *
 * @uml example.png
 * Alice -> Bob: Authentication Request
 * Bob --> Alice: Authentication Response
 * @see "[pegdown-doclet](https://github.com/Abnaxos/pegdown-doclet)"
 */
public class Meta {
    @Getter private final StringConvert convert = new StringConvert();
    @Getter private final GuideFactory guideFactory = new GuideFactory(convert);
    @Getter private final BuilderFactory builderFactory = new BuilderFactory(convert);

    public VisitBuilder visitTo(Object object) {
        return new VisitBuilder(object);
    }

    public <T> Builder<T> builderFor(Class<T> type) {
        return builderFactory.builderFor(type);
    }

    public <T> Builder<T> builderFor(TypeLiteral<T> type) {
        return builderFactory.builderFor(type);
    }

    @RequiredArgsConstructor
    @SuppressWarnings("WeakerAccess")
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
