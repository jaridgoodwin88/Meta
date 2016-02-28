package com.github.t1.meta3;

public class ReflectionVisitorTest extends AbstractVisitorTest {
    public static class Pojo {
        private String one = "a", two = "b";
    }

    protected Object createPropertyContainer() {
        return new Pojo();
    }
}
