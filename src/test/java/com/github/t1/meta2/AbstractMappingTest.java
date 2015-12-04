package com.github.t1.meta2;

import static org.assertj.core.api.Assertions.*;

import java.util.List;

import org.junit.Test;

import com.github.t1.meta2.Mapping.Property;

public abstract class AbstractMappingTest {

    protected abstract Object createObject();

    protected abstract Mapping createMapping(Object object);

    Object object = createObject();

    Mapping mapping = createMapping(object);

    @Test
    public void shouldGetPropertiesOfPojo() {
        Property property = mapping.getProperty("stringProperty");
        assertThat(property).isNotNull();
        assertThat(property.getName()).isEqualTo("stringProperty");
        assertThat(property.getScalarValue().getStringValue(object)).contains("stringValue");
    }

    @Test
    public void shouldGetProperties() {
        List<Property> properties = mapping.getProperties();

        assertThat(properties).extracting(property -> property.getName()).containsExactly("stringProperty");
    }
}
