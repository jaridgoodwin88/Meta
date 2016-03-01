package com.github.t1.meta.visitor;

import lombok.RequiredArgsConstructor;

public abstract class Guide {
    public void guide(Visitor visitor) {
        if (visitor.getGuide() == null)
            visitor.setGuide(this);
    }

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
