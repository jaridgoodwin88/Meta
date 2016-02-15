package com.github.t1.meta2.test;

import static com.github.t1.meta2.StructureKind.*;
import static com.github.t1.meta2.reflection.ObjectProperty.*;
import static java.util.Arrays.*;
import static java.util.stream.Collectors.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;
import static org.assertj.core.api.Assertions.tuple;
import static org.assertj.core.api.BDDAssertions.*;

import java.util.*;
import java.util.stream.IntStream;

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
    public static final float FLOAT_VALUE = 1.5f;
    public static final double DOUBLE_VALUE = 12.3456d;

    public static final int[] INT_ARRAY_VALUE = { 1, 2, 3, 4, 5 };
    public static final List<Integer> INT_LIST_VALUE = IntStream.of(INT_ARRAY_VALUE).boxed().collect(toList());
    public static final List<String> STRING_LIST_VALUE = asList("one", "two", "three");

    protected abstract boolean hasSchema();

    protected abstract B createObject();

    protected abstract Mapping<B> createMapping();

    B object = createObject();

    Mapping<B> mapping = createMapping();


    @Test
    public void shouldGetProperties() {
        List<Property<B>> properties = mapping.getProperties();

        then(properties)
                .extracting(this::nameAndKind)
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
                        tuple("nestedSequenceSequenceProperty", sequence),
                        tuple("nestedMappingSequenceProperty", sequence),

                        tuple("nestedProperty", StructureKind.mapping),
                        tuple("nestingProperty", StructureKind.mapping));
    }

    @Test
    public void shouldGetNestedProperties() {
        List<Property<B>> properties = mapping.getProperty("nestedProperty").getMapping().getProperties();

        then(properties).extracting(this::nameAndKind)
                .containsExactly(tuple("nestedStringProperty", scalar));
    }

    @Test
    public void shouldGetNestingProperties() {
        List<Property<B>> properties = mapping.getProperty("nestingProperty").getMapping().getProperties();

        then(properties).extracting(this::nameAndKind)
                .containsExactly(tuple("nestedProperty", StructureKind.mapping));

        List<Property<B>> nestedProperties =
                mapping.getPropertyPath("nestingProperty/nestedProperty").getMapping().getProperties();

        then(nestedProperties).extracting(this::nameAndKind)
                .containsExactly(tuple("nestedStringProperty", scalar));
    }

    private Tuple nameAndKind(Property<?> property) {
        return tuple(property.getName(), property.getKind());
    }


    protected Property<B> getProperty(String name) {
        Property<B> property = mapping.getProperty(name);

        then(property).isNotNull();
        then(property.getName()).isEqualTo(name);

        return property;
    }

    @Test
    public void shouldGetStringProperty() {
        Property<B> property = getProperty("stringProperty");

        assertScalar(property, STRING_VALUE);
    }

    @Test
    public void shouldGetBooleanProperty() {
        Property<B> property = getProperty("booleanProperty");

        assertScalar(property, BOOLEAN_VALUE);
    }

    @Test
    public void shouldGetCharProperty() {
        Property<B> property = getProperty("charProperty");

        assertScalar(property, CHARACTER_VALUE);
    }

    @Test
    public void shouldGetByteProperty() {
        Property<B> property = getProperty("byteProperty");

        assertScalar(property, BYTE_VALUE);
    }

    @Test
    public void shouldGetShortProperty() {
        Property<B> property = getProperty("shortProperty");

        assertScalar(property, SHORT_VALUE);
    }

    @Test
    public void shouldGetIntProperty() {
        Property<B> property = getProperty("intProperty");

        assertScalar(property, INT_VALUE);
    }

    @Test
    public void shouldGetIntegerProperty() {
        Property<B> property = getProperty("integerProperty");

        assertScalar(property, INTEGER_VALUE);
    }

    @Test
    public void shouldGetLongProperty() {
        Property<B> property = getProperty("longProperty");

        assertScalar(property, LONG_VALUE);
    }

    @Test
    public void shouldGetFloatProperty() {
        Property<B> property = getProperty("floatProperty");

        assertScalar(property, FLOAT_VALUE);
    }

    @Test
    public void shouldGetDoubleProperty() {
        Property<B> property = getProperty("doubleProperty");

        assertScalar(property, DOUBLE_VALUE);
    }

    private <T> void assertScalar(Property<B> property, T expectedValue) {
        Scalar<B> scalar = property.getScalar();
        if (Character.class.isAssignableFrom(expectedValue.getClass()))
            then(scalar.get(object, String.class)).contains(Integer.toString((Character) expectedValue));
        else
            then(scalar.get(object, String.class)).contains(expectedValue.toString());
        for (Class<?> scalarType : PRIMITIVE_WRAPPER_SCALARS) {
            Object convertedExpectedValue = convert(expectedValue, scalarType);
            if (convertedExpectedValue == null)
                then(catchThrowable(() -> scalar.get(object, scalarType)))
                        .as("thrown exception when getting %s as %s", scalar, scalarType)
                        .isInstanceOf(ClassCastException.class);
            else
                then(get(scalar, scalarType).get())
                        .as("as %s", scalarType.getName())
                        .isEqualTo(convertedExpectedValue);
        }
    }

    @SuppressWarnings("ChainOfInstanceofChecks")
    private <T> Object convert(T expectedValue, Class<?> scalarType) {
        if (expectedValue instanceof Number && Number.class.isAssignableFrom(scalarType))
            return numericValue((Number) expectedValue, scalarType);
        if (expectedValue instanceof Number && Character.class.isAssignableFrom(scalarType))
            return (char) ((Number) expectedValue).shortValue();
        if (expectedValue instanceof Character && Number.class.isAssignableFrom(scalarType))
            return numericValue((short) (char) (Character) expectedValue, scalarType);
        if (expectedValue instanceof Character && Character.class.isAssignableFrom(scalarType))
            return expectedValue;
        if (scalarType.isInstance(expectedValue))
            return expectedValue;
        return null;
    }

    private <T> Object numericValue(Number expectedValue, Class<T> type) {
        if (Byte.class.isAssignableFrom(type))
            return expectedValue.byteValue();
        if (Short.class.isAssignableFrom(type))
            return expectedValue.shortValue();
        if (Integer.class.isAssignableFrom(type))
            return expectedValue.intValue();
        if (Long.class.isAssignableFrom(type))
            return expectedValue.longValue();
        if (Float.class.isAssignableFrom(type))
            return expectedValue.floatValue();
        if (Double.class.isAssignableFrom(type))
            return expectedValue.doubleValue();
        throw new UnsupportedOperationException("unexpected numeric type: " + type.getName());
    }

    private <T> Optional<T> get(Scalar<B> scalar, Class<?> scalarType) {
        @SuppressWarnings("unchecked")
        Class<T> scalarT = (Class<T>) scalarType;
        return scalar.get(object, scalarT);
    }

    @Test
    public void shouldGetIntArrayProperty() {
        Property<B> property = getProperty("intArrayProperty");

        assertSequece(property, INT_LIST_VALUE, Integer.class);
    }

    @Test
    public void shouldGetIntegerListProperty() {
        Property<B> property = getProperty("intListProperty");

        assertSequece(property, INT_LIST_VALUE, Integer.class);
    }

    @Test
    public void shouldGetStringListProperty() {
        Property<B> property = getProperty("stringListProperty");

        assertSequece(property, STRING_LIST_VALUE, String.class);
    }

    private <T> void assertSequece(Property<B> property, List<T> expectedValues, Class<T> type) {
        int expectedSize = expectedValues.size();
        then(property.getSequence().size(object)).isEqualTo(expectedSize);
        for (int i = 0; i < expectedSize; i++) {
            then(property.getSequence().get(i).getScalar().get(object, type)).contains(expectedValues.get(i));
            then(property.get(i).get(object, type)).contains(expectedValues.get(i));
        }
    }

    @Test
    public void shouldGetNestedSequenceSequence() {
        Property<B> list = getProperty("nestedSequenceSequenceProperty");

        assertThat(list.getKind()).isEqualTo(sequence);
        assertThat(list.getSequence().size(object)).isEqualTo(2);

        if (hasSchema())
            assertThat(list.get(0).getKind()).isEqualTo(sequence); // may be unknown
        assertThat(list.get(0).getSequence().size(object)).isEqualTo(2);

        if (hasSchema())
            assertThat(list.get(1).getKind()).isEqualTo(sequence);
        assertThat(list.get(1).getSequence().size(object)).isEqualTo(3);

        assertThat(list.get(0).get(0).get(object, String.class)).contains("A1");
        assertThat(list.get(0).get(1).get(object, String.class)).contains("A2");
        assertThat(list.get(1).get(0).get(object, String.class)).contains("B1");
        assertThat(list.get(1).get(1).get(object, String.class)).contains("B2");
        assertThat(list.get(1).get(2).get(object, String.class)).contains("B3");

        assertThat(list.get(99).get(2).get(object, String.class)).isEmpty();
        assertThat(list.get(1).get(99).get(object, String.class)).isEmpty();
    }


    @Test
    public void shouldGetNestedProperty() {
        Property<B> property = getProperty("nestedProperty").get("nestedStringProperty");

        assertScalar(property, "nestedString");
    }

    @Test
    public void shouldGetDoublyNestedProperty() {
        Property<B> outer = getProperty("nestingProperty");
        Property<B> middle = outer.get("nestedProperty");
        Property<B> property = middle.get("nestedStringProperty");

        assertScalar(property, "nestedString");
    }

    @Test
    public void shouldGetPropertyPath() {
        Property<B> property = mapping.getPropertyPath("nestingProperty/nestedProperty/nestedStringProperty");

        assertScalar(property, "nestedString");
    }
}
