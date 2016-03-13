package com.github.t1.meta;

import com.github.t1.meta.visitor.*;
import lombok.*;
import org.joda.convert.StringConvert;

/**
 * This is the main entry point.
 * # Main Title
 *
 *     new Meta().visitTo(object).by(visitor).run();
 *
 * ```java
 * public class FencedCodeBlock {
 *     public void cool() {
 *         new Meta().visitTo(object).by(visitor).run();
 *     }
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
 *
 * @see "[pegdown-doclet](https://github.com/Abnaxos/pegdown-doclet)"
 */
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
