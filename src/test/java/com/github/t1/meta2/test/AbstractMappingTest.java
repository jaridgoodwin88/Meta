package com.github.t1.meta2.test;

import static com.github.t1.meta2.StructureKind.*;
import static com.github.t1.meta2.reflection.ObjectProperty.*;
import static java.util.Arrays.*;
import static java.util.stream.Collectors.*;
import static org.assertj.core.api.Assertions.*;
import static org.assertj.core.api.BDDAssertions.*;

import java.util.List;
import java.util.stream.IntStream;

import org.assertj.core.api.iterable.Extractor;
import org.assertj.core.groups.Tuple;
import org.junit.Test;

import com.github.t1.meta2.*;
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

    public static final int[] INT_ARRAY_VALUE = { 1, 2, 3, 4, 5 };
    public static final List<Integer> INT_LIST_VALUE = IntStream.of(INT_ARRAY_VALUE).boxed().collect(toList());
    public static final List<String> STRING_LIST_VALUE = asList("one", "two", "three");

    protected abstract B createObject();

    protected abstract Mapping<B> createMapping();

    B object = createObject();

    Mapping<B> mapping = createMapping();


    @Test
    public void shouldGetProperties() {
        List<Property<B>> properties = mapping.getProperties();

        then(properties)
                .extracting(nameAndKind())
                .containsExactly(
                        tuple("stringProperty", scalar),
                        tuple("booleanProperty", scalar),
                        tuple("charProperty", scalar),
                        tuple("byteProperty", scalar),
                        tuple("shortProperty", scalar),
                        tuple("intProperty", scalar),
                        tuple("integerProperty", scalar),
                        tuple("longProperty", scalar),
                        tuple("floatProperty", scalar),
                        tuple("doubleProperty", scalar),

                        tuple("intArrayProperty", sequence),
                        tuple("intListProperty", sequence),
                        tuple("stringListProperty", sequence),

                        tuple("nestedProperty", StructureKind.mapping),
                        tuple("nestingProperty", StructureKind.mapping));
    }

    @Test
    public void shouldGetNestedProperties() {
        List<Property<B>> properties = mapping.getProperty("nestedProperty").getMapping().getProperties();

        then(properties).extracting(nameAndKind())
                .containsExactly(tuple("nestedStringProperty", scalar));
    }

    @Test
    public void shouldGetNestingProperties() {
        List<Property<B>> properties = mapping.getProperty("nestingProperty").getMapping().getProperties();

        then(properties).extracting(nameAndKind())
                .containsExactly(tuple("nestedProperty", StructureKind.mapping));

        List<Property<B>> nestedProperties =
                mapping.getPropertyPath("nestingProperty/nestedProperty").getMapping().getProperties();

        then(nestedProperties).extracting(nameAndKind())
                .containsExactly(tuple("nestedStringProperty", scalar));
    }

    private Extractor<Property<B>, Tuple> nameAndKind() {
        return property -> tuple(property.getName(), property.getKind());
    }


    protected Property<B> whenGetProperty(String name) {
        Property<B> property = mapping.getProperty(name);

        then(property).isNotNull();
        then(property.getName()).isEqualTo(name);

        return property;
    }

    @Test
    public void shouldGetStringProperty() {
        Property<B> property = whenGetProperty("stringProperty");

        assertScalar(property, STRING_VALUE);
    }

    @Test
    public void shouldGetBooleanProperty() {
        Property<B> property = whenGetProperty("booleanProperty");

        assertScalar(property, BOOLEAN_VALUE);
    }

    @Test
    public void shouldGetCharProperty() {
        Property<B> property = whenGetProperty("charProperty");

        assertScalar(property, CHARACTER_VALUE);
    }

    @Test
    public void shouldGetByteProperty() {
        Property<B> property = whenGetProperty("byteProperty");

        assertScalar(property, BYTE_VALUE);
    }

    @Test
    public void shouldGetShortProperty() {
        Property<B> property = whenGetProperty("shortProperty");

        assertScalar(property, SHORT_VALUE);
    }

    @Test
    public void shouldGetIntProperty() {
        Property<B> property = whenGetProperty("intProperty");

        assertScalar(property, INT_VALUE);
    }

    @Test
    public void shouldGetIntegerProperty() {
        Property<B> property = whenGetProperty("integerProperty");

        assertScalar(property, INTEGER_VALUE);
    }

    @Test
    public void shouldGetLongProperty() {
        Property<B> property = whenGetProperty("longProperty");

        assertScalar(property, LONG_VALUE);
    }

    @Test
    public void shouldGetFloatProperty() {
        Property<B> property = whenGetProperty("floatProperty");

        assertScalar(property, FLOAT_VALUE);
    }

    @Test
    public void shouldGetDoubleProperty() {
        Property<B> property = whenGetProperty("doubleProperty");

        assertScalar(property, DOUBLE_VALUE);
    }

    private <T> void assertScalar(Property<B> property, T expectedValue) {
        Scalar<B> scalar = property.getScalar();
        then(scalar.get(object, String.class)).contains(expectedValue.toString());
        for (Class<?> scalarType : PRIMITIVE_WRAPPER_SCALARS)
            if (scalarType.isInstance(expectedValue)) {
                @SuppressWarnings("unchecked")
                Class<T> scalarT = (Class<T>) scalarType;
                then(scalar.get(object, scalarT)).contains(expectedValue);
            } else {
                thenThrownBy(() -> scalar.get(object, scalarType))
                        .isInstanceOf(ClassCastException.class);
            }
    }


    @Test
    public void shouldGetIntArrayProperty() {
        Property<B> property = whenGetProperty("intArrayProperty");

        assertSequece(property, INT_LIST_VALUE, Integer.class);
    }

    @Test
    public void shouldGetIntegerListProperty() {
        Property<B> property = whenGetProperty("intListProperty");

        assertSequece(property, INT_LIST_VALUE, Integer.class);
    }

    @Test
    public void shouldGetStringListProperty() {
        Property<B> property = whenGetProperty("stringListProperty");

        assertSequece(property, STRING_LIST_VALUE, String.class);
    }

    private <T> void assertSequece(Property<B> property, List<T> expectedValues, Class<T> type) {
        Sequence<B> sequence = property.getSequence();
        int expectedSize = expectedValues.size();
        then(sequence.size(object)).isEqualTo(expectedSize);
        for (int i = 0; i < expectedSize; i++)
            then(sequence.get(i).getScalar().get(object, type)).contains(expectedValues.get(i));
    }


    @Test
    public void shouldGetNestedProperty() {
        Property<B> property = whenGetProperty("nestedProperty").getProperty("nestedStringProperty");

        assertScalar(property, "nestedString");
    }

    @Test
    public void shouldGetDoublyNestedProperty() {
        Property<B> outer = whenGetProperty("nestingProperty");
        Property<B> middle = outer.getProperty("nestedProperty");
        Property<B> property = middle.getProperty("nestedStringProperty");

        assertScalar(property, "nestedString");
    }

    @Test
    public void shouldGetPropertyPath() {
        Property<B> property = mapping.getPropertyPath("nestingProperty/nestedProperty/nestedStringProperty");

        assertScalar(property, "nestedString");
    }
}
