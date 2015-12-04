package com.github.t1.meta2;

import static com.github.t1.meta2.StructureKind.*;
import static org.assertj.core.api.Assertions.*;

import java.util.List;

import org.junit.Test;

import com.github.t1.meta2.Mapping.Property;

public abstract class AbstractMappingTest<B> {
    public static final String STRING_VALUE = "stringValue";
    public static final boolean BOOLEAN_VALUE = true;
    public static final char CHARACTER_VALUE = 'a';
    public static final byte BYTE_VALUE = (byte) 12;
    public static final short SHORT_VALUE = (short) 123;
    public static final int INT_VALUE = 12345;
    public static final int INTEGER_VALUE = 12345678;
    public static final long LONG_VALUE = 1234567890123456789L;
    public static final float FLOAT_VALUE = 12.34f;
    public static final double DOUBLE_VALUE = 12.3456d;

    protected abstract B createObject();

    protected abstract Mapping<B> createMapping();

    B object = createObject();

    Mapping<B> mapping = createMapping();

    private static final Class<?>[] SCALAR_TYPES = { Boolean.class, Character.class, Byte.class, Short.class,
            Integer.class, Long.class, Float.class, Double.class, String.class };

    @SuppressWarnings("unchecked")
    private <T> void assertGettingScalar(T expectedValue, Scalar<B> scalarValue) {
        for (Class<?> scalarType : SCALAR_TYPES)
            if (scalarType == String.class)
                assertThat(scalarValue.get(object, String.class)).contains(expectedValue.toString());
            else if (scalarType.isInstance(expectedValue))
                assertThat(scalarValue.get(object, (Class<T>) scalarType)).contains(expectedValue);
            else
                assertThatThrownBy(() -> scalarValue.get(object, scalarType)).isInstanceOf(ClassCastException.class);
    }

    @Test
    public void shouldGetStringPropertyOfObject() {
        testProperty("stringProperty", STRING_VALUE);
    }

    @Test
    public void shouldGetBooleanPropertyOfObject() {
        testProperty("booleanProperty", BOOLEAN_VALUE);
    }

    @Test
    public void shouldGetCharPropertyOfObject() {
        testProperty("charProperty", CHARACTER_VALUE);
    }

    @Test
    public void shouldGetBytePropertyOfObject() {
        testProperty("byteProperty", BYTE_VALUE);
    }

    @Test
    public void shouldGetShortPropertyOfObject() {
        testProperty("shortProperty", SHORT_VALUE);
    }

    @Test
    public void shouldGetIntPropertyOfObject() {
        testProperty("intProperty", INT_VALUE);
    }

    @Test
    public void shouldGetIntegerPropertyOfObject() {
        testProperty("integerProperty", INTEGER_VALUE);
    }

    @Test
    public void shouldGetLongPropertyOfObject() {
        testProperty("longProperty", LONG_VALUE);
    }

    @Test
    public void shouldGetFloatPropertyOfObject() {
        testProperty("floatProperty", FLOAT_VALUE);
    }

    @Test
    public void shouldGetDoublePropertyOfObject() {
        testProperty("doubleProperty", DOUBLE_VALUE);
    }

    private <T> void testProperty(String name, T expectedValue) {
        Property<B> property = mapping.getProperty(name);

        assertThat(property).isNotNull();
        assertThat(property.getName()).isEqualTo(name);
        Scalar<B> scalarValue = property.getScalarValue();

        assertThat(scalarValue.get(object, String.class)).contains(expectedValue.toString());
        assertGettingScalar(expectedValue, scalarValue);
    }

    @Test
    public void shouldGetProperties() {
        List<Property<B>> properties = mapping.getProperties();

        assertThat(properties) //
                .extracting(property -> tuple(property.getName(), property.getKind())) //
                .containsExactly( //
                        tuple("stringProperty", scalar), //
                        tuple("booleanProperty", scalar), //
                        tuple("charProperty", scalar), //
                        tuple("byteProperty", scalar), //
                        tuple("shortProperty", scalar), //
                        tuple("intProperty", scalar), //
                        tuple("integerProperty", scalar), //
                        tuple("longProperty", scalar), //
                        tuple("floatProperty", scalar), //
                        tuple("doubleProperty", scalar) //
        );
    }
}
