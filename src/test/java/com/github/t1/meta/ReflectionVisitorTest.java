package com.github.t1.meta;

import com.google.common.collect.ImmutableList;
import lombok.Data;
import org.junit.Test;

import java.io.Serializable;
import java.util.List;

import static java.util.Collections.emptyList;
import static org.assertj.core.api.Assertions.assertThat;

public class ReflectionVisitorTest extends AbstractVisitorTest {
    @Data
    public class PojoContainer implements Serializable {
        // ignore non-static 'this'
        private static final long serialVersionUID = -1L; // must be ignored

        private Pojo mappingOne = new Pojo();
    }

    @Data
    public static class Pojo {
        public static String ignoreMe = "x";
        public volatile String ignoreMeToo = "y";

        private String one = "a", two = "b";
    }

    @Override protected Object createFlatMapping() {
        return new Pojo();
    }

    @Override protected Object createNestedMapping() {
        return new PojoContainer();
    }

    @Override protected Object createFlatSequence() {
        return new String[] { "a", "b", "c" };
    }

    @Override protected Object createNestedSequence() {
        return new String[][] { { "a1", "a2", "a3" }, {}, { "c1" } };
    }

    @Data
    public static class MappingWithSequence {
        String[] a = { "x", "y", "z" };
        List<String> b = emptyList();
        List<String> c = ImmutableList.of("x");
    }

    @Override protected Object createMappingWithSequence() {
        return new MappingWithSequence();
    }

    @Override protected Object createSequenceWithMapping() {
        Pojo c = new Pojo();
        c.one = null;
        return ImmutableList.of(new Pojo(), new Object(), c);
    }

    @Test
    public void shouldVisitSubclass() {
        class Super {
            String superField = "super";
        }
        class Sub extends Super {
            String subField = "sub";
        }
        Sub sub = new Sub();

        tour(sub);

        assertThat(visitor).hasToString("{superField:<super>;|subField:<sub>;}");
    }
}
