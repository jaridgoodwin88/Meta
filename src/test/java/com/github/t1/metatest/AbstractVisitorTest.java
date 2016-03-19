package com.github.t1.metatest;

import com.github.t1.meta.*;
import com.github.t1.meta.visitor.Visitor;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;

import java.math.*;
import java.time.LocalDate;
import java.util.*;

import static org.assertj.core.api.Assertions.*;

@Slf4j
@RunWith(MockitoJUnitRunner.class)
public abstract class AbstractVisitorTest {
    private static final int SOME_INT = 123;
    private static final double SOME_FLOAT = 123.4;
    private static final double SOME_DOUBLE = 123.45d;

    final Meta meta = new Meta();

    final Visitor visitor = new Visitor() {
        private final StringBuilder out = new StringBuilder();

        @Override public void enterMapping() {
            out.append("{");
        }

        @Override public void continueMapping() {
            out.append("|");
        }

        @Override public void leaveMapping() {
            out.append("}");
        }

        @Override public void enterProperty(Property property) { out.append(property.name()).append(":"); }

        @Override public void leaveProperty() {
            out.append(";");
        }

        @Override public void enterSequence() {
            out.append("[");
        }

        @Override public void enterItem() { out.append("«"); }

        @Override public void leaveItem() { out.append("»"); }

        @Override public void continueSequence() {
            out.append(",");
        }

        @Override public void leaveSequence() {
            out.append("]");
        }

        @Override public void visitScalar(Object value) {
            out.append("<").append(value).append(">");
        }

        @Override public void visitNull() { out.append('•'); }

        @Override public String toString() {
            return out.toString();
        }
    };

    void tour(Object object) {
        meta.visitTo(object).by(visitor).run();
    }

    @Test
    public void shouldVisitStringScalar() {
        tour("hello world");

        assertThat(visitor).hasToString("<hello world>");
    }

    @Test
    public void shouldVisitBooleanScalar() {
        tour(true);

        assertThat(visitor).hasToString("<true>");
    }

    @Test
    public void shouldVisitByteScalar() {
        tour((byte) SOME_INT);

        assertThat(visitor).hasToString("<123>");
    }

    @Test
    public void shouldVisitCharacterScalar() {
        tour('c');

        assertThat(visitor).hasToString("<c>");
    }

    @Test
    public void shouldVisitShortScalar() {
        tour((short) SOME_INT);

        assertThat(visitor).hasToString("<123>");
    }

    @Test
    public void shouldVisitIntegerScalar() {
        tour(SOME_INT);

        assertThat(visitor).hasToString("<123>");
    }

    @Test
    public void shouldVisitLongScalar() {
        tour((long) SOME_INT);

        assertThat(visitor).hasToString("<123>");
    }

    @Test
    public void shouldVisitFloatScalar() {
        tour(SOME_FLOAT);

        assertThat(visitor).hasToString("<123.4>");
    }

    @Test
    public void shouldVisitDoubleScalar() {
        tour(SOME_DOUBLE);

        assertThat(visitor).hasToString("<123.45>");
    }

    @Test
    public void shouldVisitBigIntegerScalar() {
        tour(BigInteger.TEN);

        assertThat(visitor).hasToString("<10>");
    }

    @Test
    public void shouldVisitBigDecimalScalar() {
        tour(new BigDecimal("123.45"));

        assertThat(visitor).hasToString("<123.45>");
    }

    @SuppressWarnings("unused")
    private enum E {
        ONE, TWO, THREE
    }

    @Test
    public void shouldVisitEnumScalar() {
        tour(E.TWO);

        assertThat(visitor).hasToString("<TWO>");
    }

    @Test
    public void shouldVisitLocalDateScalar() {
        LocalDate now = LocalDate.now();

        tour(now);

        assertThat(visitor).hasToString("<" + now + ">");
    }

    @Test
    public void shouldVisitFlatMapping() {
        Object object = createFlatMapping();

        tour(object);

        assertThat(visitor).hasToString("{one:<a>;|two:<b>;•}");
    }

    protected abstract Object createFlatMapping();

    @Test
    public void shouldVisitNestedMapping() {
        Object object = createNestedMapping();

        tour(object);

        assertThat(visitor).hasToString("{mappingOne:{one:<a>;|two:<b>;•};}");
    }

    protected abstract Object createNestedMapping();

    @Test
    public void shouldVisitFlatSequence() {
        Object object = createFlatSequence();

        tour(object);

        assertThat(visitor.toString()).isEqualTo("[«<a>»,«<b>»•,«<c>»]");
    }

    protected abstract Object createFlatSequence();

    @Test
    public void shouldVisitNestedSequence() {
        Object object = createNestedSequence();

        tour(object);

        assertThat(visitor).hasToString("[«[«<a1>»,«<a2>»,«<a3>»]»,«[]»,«[«<c1>»]»]");
    }

    protected abstract Object createNestedSequence();

    @Test
    public void shouldVisitMappingWithSequence() {
        Object object = createMappingWithSequence();

        tour(object);

        assertThat(visitor).hasToString("{a:[«<x>»,«<y>»•,«<z>»];|b:[];|c:[«<x>»];}");
    }

    protected abstract Object createMappingWithSequence();

    @Test
    public void shouldVisitSequenceWithMapping() {
        Object object = createSequenceWithMapping();

        tour(object);

        assertThat(visitor).hasToString("[«{one:<a>;|two:<b>;•}»,«{}»,«{•two:<b>;•}»]");
    }

    @Test
    public void shouldGetDepthsSequenceWithMapping() {
        List<Integer> depths = new ArrayList<>();
        Object object = createSequenceWithMapping();

        meta.visitTo(object).by(new Visitor() {
            @Override public void enterMapping() {
                depths.add(guide().depth());
            }

            @Override public void enterProperty(Property property) { depths.add(guide().depth()); }

            @Override public void enterSequence() {
                depths.add(guide().depth());
            }

            @Override public void visitScalar(Object value) {
                depths.add(guide().depth());
            }
        }).run();

        assertThat(depths).containsExactly(0, 1, 1, 2, 1, 2, 1, 1, 1, 2);
    }

    protected abstract Object createSequenceWithMapping();
}
