package com.github.t1.metatest;

import com.github.t1.meta.Property;
import com.github.t1.meta.visitor.Visitor;
import lombok.Data;
import org.junit.Test;

import java.io.Serializable;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

import static java.util.Collections.*;
import static org.assertj.core.api.Assertions.*;

public class ReflectionVisitorTest extends AbstractVisitorTest {
    @Data
    public static class PojoContainer implements Serializable {
        // ignore non-static 'this'
        private static final long serialVersionUID = -1L; // must be ignored

        private Pojo mappingOne = new Pojo();
    }

    @Data
    public static class Pojo {
        public static String ignoreMe = "x";
        public volatile String ignoreMeToo = "y";

        private String one = "a", two = "b";
        private Object nil = null;
    }

    @Override protected Object createFlatMapping() {
        return new Pojo();
    }

    @Override protected Object createNestedMapping() {
        return new PojoContainer();
    }

    @Override protected Object createFlatSequence() {
        return new String[] { "a", "b", null, "c" };
    }

    @Override protected Object createNestedSequence() {
        return new String[][] { { "a1", "a2", "a3" }, {}, { "c1" } };
    }

    @Data
    public static class MappingWithSequence {
        String[] a = { "x", "y", null, "z" };
        List<String> b = emptyList();
        List<String> c = List.of("x");
    }

    @Override protected Object createMappingWithSequence() {
        return new MappingWithSequence();
    }

    @Override protected Object createSequenceWithMapping() {
        Pojo c = new Pojo();
        c.one = null;
        return List.of(new Pojo(), new Object(), c);
    }

    @Test
    public void shouldVisitSubclass() {
        class Super {
            @SuppressWarnings("unused") final String superField = "super";
        }
        class Sub extends Super {
            @SuppressWarnings("unused") final String subField = "sub";
        }
        Sub sub = new Sub();

        tour(sub);

        assertThat(visitor).hasToString("{superField:<super>;|subField:<sub>;}");
    }

    @Test
    public void shouldVisitMappingWithAnnotations() {
        @Data
        class Pojo {
            @Deprecated
            private String one = "a";
        }
        Object object = new Pojo();

        AtomicReference<Property> property = new AtomicReference<>();
        meta.visitTo(object).by(new Visitor() {
            @Override public void enterProperty(Property p) {
                property.set(p);
            }
        }).run();

        assertThat(property.get().name()).isEqualTo("one");
        assertThat(property.get().isAnnotationPresent(Deprecated.class)).isTrue();
    }
}
