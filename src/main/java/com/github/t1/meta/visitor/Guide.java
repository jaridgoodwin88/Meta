package com.github.t1.meta.visitor;

import lombok.*;

import static lombok.AccessLevel.PACKAGE;
import static lombok.AccessLevel.PRIVATE;

public abstract class Guide {
    @Setter(PACKAGE) private GuideFactory factory;

    @Builder
    @AllArgsConstructor(access = PRIVATE)
    public static class Visit {
        @NonNull @Getter private final Object destination;
        @NonNull @Getter private final Visitor visitor;
        @NonNull private final GuideFactory guideFactory;

        private Guide mainGuide;

        public void run() {
            this.mainGuide = guideFactory.getGuideTo(destination);
            this.mainGuide.run(this);
        }

        public void to(Object stopover) {
            guideFactory.getGuideTo(stopover).run(this);
        }
    }

    public abstract void run(Visit visit);

    /**
     * Invokes a runnable on all but the first {@link #call()}
     */
    @RequiredArgsConstructor
    public static class Continue {
        private final Runnable runnable;
        private boolean first = true;

        public void call() {
            if (first)
                first = false;
            else
                runnable.run();
        }
    }
}
