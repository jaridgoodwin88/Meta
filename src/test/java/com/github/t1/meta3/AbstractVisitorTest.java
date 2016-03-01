package com.github.t1.meta3;

import com.github.t1.meta3.visitor.Visitor;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;

import static org.assertj.core.api.Assertions.assertThat;

@Slf4j
@RunWith(MockitoJUnitRunner.class)
public abstract class AbstractVisitorTest {
    Meta meta = new Meta();

    Visitor visitor = new Visitor() {
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

        @Override public void enterProperty(String key) {
            out.append(key).append(":");
        }

        @Override public void leaveProperty() {
            out.append(";");
        }

        @Override public void enterSequence() {
            out.append("[");
        }

        @Override public void continueSequence() {
            out.append(",");
        }

        @Override public void leaveSequence() {
            out.append("]");
        }

        @Override public void visitScalar(Object value) {
            out.append("<").append(value).append(">");
        }

        @Override public String toString() {
            return out.toString();
        }
    };

    void tour(Object object) {
        meta.getGuideTo(object).guide(visitor);
    }

    @Test
    public void shouldVisitStringScalar() {
        tour("hello world");

        assertThat(visitor).hasToString("<hello world>");
    }

    @Test
    public void shouldVisitFlatMapping() {
        Object object = createFlatMapping();

        tour(object);

        assertThat(visitor).hasToString("{one:<a>;|two:<b>;}");
    }

    protected abstract Object createFlatMapping();

    @Test
    public void shouldVisitNestedMapping() {
        Object object = createNestedMapping();

        tour(object);

        assertThat(visitor).hasToString("{mappingOne:{one:<a>;|two:<b>;};}");
    }

    protected abstract Object createNestedMapping();

    @Test
    public void shouldVisitFlatSequence() {
        Object object = createFlatSequence();

        tour(object);

        assertThat(visitor).hasToString("[<a>,<b>,<c>]");
    }

    protected abstract Object createFlatSequence();

    @Test
    public void shouldVisitNestedSequence() {
        Object object = createNestedSequence();

        tour(object);

        assertThat(visitor).hasToString("[[<a1>,<a2>,<a3>],[],[<c1>]]");
    }

    protected abstract Object createNestedSequence();

    @Test
    public void shouldVisitMappingWithSequence() {
        Object object = createMappingWithSequence();

        tour(object);

        assertThat(visitor).hasToString("{a:[<x>,<y>,<z>];|b:[];|c:[<x>];}");
    }

    protected abstract Object createMappingWithSequence();

    @Test
    public void shouldVisitSequenceWithMapping() {
        Object object = createSequenceWithMapping();

        tour(object);

        assertThat(visitor).hasToString("[{one:<a>;|two:<b>;},{},{two:<b>;}]");
    }

    protected abstract Object createSequenceWithMapping();
}
