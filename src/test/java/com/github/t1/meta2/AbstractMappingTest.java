package com.github.t1.meta2;

import static com.github.t1.meta2.StructureKind.*;
import static org.assertj.core.api.Assertions.*;

import java.util.List;

import org.junit.Test;

import com.github.t1.meta2.Mapping.Property;

public abstract class AbstractMappingTest<B> {

    protected abstract B createObject();

    protected abstract Mapping<B> createMapping();

    B object = createObject();

    Mapping<B> mapping = createMapping();

    @Test
    public void shouldGetPropertiesOfPojo() {
        Property<B> property = mapping.getProperty("stringProperty");
        assertThat(property).isNotNull();
        assertThat(property.getName()).isEqualTo("stringProperty");
        assertThat(property.getScalarValue().getStringValue(object)).contains("stringValue");
    }

    @Test
    public void shouldGetProperties() {
        List<Property<B>> properties = mapping.getProperties();

        assertThat(properties) //
                .extracting(property -> tuple(property.getName(), property.getKind())) //
                .containsExactly(tuple("stringProperty", scalar));
    }
}
