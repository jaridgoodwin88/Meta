package com.github.t1.meta3;

import lombok.Data;

public class ReflectionVisitorTest extends AbstractVisitorTest {
    @Data
    public static class Pojo {
        private String one = "a", two = "b";
    }

    @Override protected Object createFlatMapping() {
        return new Pojo();
    }

    @Override protected Object createFlatSequence() {
        return new String[] { "a", "b", "c" };
    }
}
