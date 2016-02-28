package com.github.t1.meta3;

import lombok.extern.slf4j.Slf4j;
import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InOrder;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import static org.mockito.Mockito.*;

@Slf4j
@RunWith(MockitoJUnitRunner.class)
public abstract class AbstractVisitorTest {
    @Mock
    Visitor visitor;

    @After public void after() {
        verifyNoMoreInteractions(visitor);
    }

    private void startTour(Object object) {
        Meta.of(object).guide(visitor);
    }

    protected Visitor logging(Visitor visitor) {
        return new VisitorDecorator(visitor) {
            @Override public void visitScalar(Object value) {
                log.debug("start visit scalar '{}'", value);
                super.visitScalar(value);
                log.debug("end visit scalar '{}'", value);
            }
        };
    }

    @Test
    public void shouldVisitFieldProperties() {
        Object object = createPropertyContainer();

        startTour(object);

        InOrder inOrder = inOrder(visitor);

        verify(visitor).enterProperty((Object) "one");
        verify(visitor).visitScalar("a");
        inOrder.verify(visitor).leaveProperty();

        verify(visitor).enterProperty((Object) "two");
        verify(visitor).visitScalar("b");
        inOrder.verify(visitor).leaveProperty();
    }

    protected abstract Object createPropertyContainer();
}
