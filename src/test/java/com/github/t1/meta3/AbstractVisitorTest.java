package com.github.t1.meta3;

import com.github.t1.meta3.visitor.Guide;
import com.github.t1.meta3.visitor.Visitor;
import com.github.t1.meta3.visitor.VisitorDecorator;
import lombok.extern.slf4j.Slf4j;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InOrder;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.*;

@Slf4j
@RunWith(MockitoJUnitRunner.class)
public abstract class AbstractVisitorTest {
    Meta meta = new Meta();

    @Mock
    Visitor visitor;

    Guide guide;

    InOrder inOrder;

    @Before
    public void before() {
        this.inOrder = inOrder(visitor);
        when(visitor.getGuide()).thenCallRealMethod();
        doCallRealMethod().when(visitor).setGuide(any(Guide.class));
    }

    @After
    public void after() {
        verify(visitor, atLeastOnce()).getGuide();
        verify(visitor).setGuide(any());
        verifyNoMoreInteractions(visitor);
    }

    private void startTour(Object object) {
        guide = meta.getGuideTo(object);
        guide.guide(visitor);
    }

    protected Visitor logging(Visitor visitor) {
        return new VisitorDecorator(visitor) {
            @Override
            public void visitScalar(Object value) {
                log.debug("start visit scalar '{}'", value);
                super.visitScalar(value);
                log.debug("end visit scalar '{}'", value);
            }
        };
    }

    @Test
    public void shouldVisitStringScalar() {
        startTour("hello world");

        inOrder.verify(visitor).setGuide(guide);

        inOrder.verify(visitor).visitScalar("hello world");
    }

    @Test
    public void shouldVisitFlatMapping() {
        Object object = createFlatMapping();

        startTour(object);

        inOrder.verify(visitor).enterProperty((Object) "one");
        inOrder.verify(visitor).visitScalar("a");
        inOrder.verify(visitor).leaveProperty();

        inOrder.verify(visitor).enterProperty((Object) "two");
        inOrder.verify(visitor).visitScalar("b");
        inOrder.verify(visitor).leaveProperty();
    }

    protected abstract Object createFlatMapping();

    @Test
    public void shouldVisitNestedMapping() {
        Object object = createNestedMapping();

        startTour(object);

        inOrder.verify(visitor).enterProperty((Object) "mappingOne");

        inOrder.verify(visitor).enterProperty((Object) "one");
        inOrder.verify(visitor).visitScalar("a");
        inOrder.verify(visitor).leaveProperty();

        inOrder.verify(visitor).enterProperty((Object) "two");
        inOrder.verify(visitor).visitScalar("b");
        inOrder.verify(visitor, times(2)).leaveProperty();
    }

    protected abstract Object createNestedMapping();

    @Test
    public void shouldVisitFlatSequence() {
        Object object = createFlatSequence();

        startTour(object);

        inOrder.verify(visitor).setGuide(guide);

        inOrder.verify(visitor).enterSequence();
        inOrder.verify(visitor).visitScalar("a");
        inOrder.verify(visitor).visitScalar("b");
        inOrder.verify(visitor).visitScalar("c");
        inOrder.verify(visitor).leaveSequence();
    }

    protected abstract Object createFlatSequence();
}
