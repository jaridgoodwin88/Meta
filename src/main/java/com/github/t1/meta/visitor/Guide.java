package com.github.t1.meta.visitor;

import lombok.*;

import java.util.Stack;

import static lombok.AccessLevel.*;

public abstract class Guide {
    @Setter(PACKAGE) private GuideFactory factory;
    @Getter private Object destination;

    @Builder
    @AllArgsConstructor(access = PRIVATE)
    public static class Visit {
        @NonNull @Getter private final Object destination;
        @NonNull @Getter private final Visitor visitor;
        @NonNull private final GuideFactory guideFactory;
        private final Stack<Guide> guides = new Stack<>();

        public void run() {
            visitor.visit = this;
            to(destination);
        }

        public void to(Object destination) {
            Guide guide = guideFactory.getGuideTo(destination);
            guide.destination = destination;
            guides.push(guide);
            guide.run(this);
            guides.pop();
        }

        public Guide currentGuide() {
            return guides.peek();
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
