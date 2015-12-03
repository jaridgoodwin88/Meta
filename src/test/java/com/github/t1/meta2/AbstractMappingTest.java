package com.github.t1.meta2;

import static org.assertj.core.api.Assertions.*;

import org.junit.Test;

public abstract class AbstractMappingTest {

    protected abstract Object createObject();

    protected abstract Mapping createMapping(Object object);

    @Test
    public void shouldGetPropertiesOfPojo() {
        Object object = createObject();

        Mapping mapping = createMapping(object);

        Scalar property = mapping.getScalar("stringProperty");
        assertThat(property).isNotNull();
        assertThat(property.getName()).isEqualTo("stringProperty");
        assertThat(property.attach(object).getStringValue()).contains("stringValue");
    }
}
