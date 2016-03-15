package com.github.t1.meta.visitor;

import lombok.*;

import java.util.Stack;

import static lombok.AccessLevel.*;

public abstract class Guide {
    @Getter private Visit visit;

    public int depth() {
        return visit.guides.size() - 1; // root is 0
    }

    public Object destination() { return visit.destinations.peek(); }

    @Builder
    @AllArgsConstructor(access = PRIVATE)
    public static class Visit {
        public static class VisitBuilder {
            private Stack<Object> destinations = new Stack<>();

            public VisitBuilder destination(Object destination) {
                destinations.push(destination);
                return this;
            }
        }

        @NonNull @Getter private final Visitor visitor;
        @NonNull private final GuideFactory guideFactory;
        private final Stack<Object> destinations;
        private final Stack<Guide> guides = new Stack<>();

        public void run() {
            visitor.visit = this;
            to(destinations.peek());
        }

        public void to(Object destination) {
            Guide guide = guideFactory.getGuideTo(destination);
            guide.visit = this;
            guides.push(guide);
            destinations.push(destination);
            guide.run(this);
            destinations.pop();
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
