package com.github.t1.meta2;

import static com.github.t1.meta2.StructureKind.*;
import static com.github.t1.meta2.reflection.ObjectProperty.*;
import static java.util.stream.Collectors.*;
import static org.assertj.core.api.Assertions.*;
import static org.assertj.core.api.BDDAssertions.*;

import java.util.List;
import java.util.stream.IntStream;

import org.assertj.core.api.Condition;
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

    public static final int[] INT_ARRAY_VALUE = { 1, 2, 3, 4, 5 };
    public static final List<Integer> INT_LIST_VALUE = IntStream.of(INT_ARRAY_VALUE).boxed().collect(toList());

    protected abstract B createObject();

    protected abstract Mapping<B> createMapping();

    B object = createObject();

    Mapping<B> mapping = createMapping();


    @Test
    public void shouldGetProperties() {
        List<Property<B>> properties = mapping.getProperties();

        then(properties) //
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
                        tuple("doubleProperty", scalar), //
                        //
                        tuple("intArrayProperty", sequence) //
        );
    }


    private Property<B> whenGetProperty(String name) {
        Property<B> property = mapping.getProperty(name);

        then(property).isNotNull();
        then(property.getName()).isEqualTo(name);

        return property;
    }

    @Test
    public void shouldGetStringProperty() {
        Property<B> property = whenGetProperty("stringProperty");

        then(property.getScalar()).is(validScalar(STRING_VALUE));
    }

    @Test
    public void shouldGetBooleanProperty() {
        Property<B> property = whenGetProperty("booleanProperty");

        then(property.getScalar()).is(validScalar(BOOLEAN_VALUE));
    }

    @Test
    public void shouldGetCharProperty() {
        Property<B> property = whenGetProperty("charProperty");

        then(property.getScalar()).is(validScalar(CHARACTER_VALUE));
    }

    @Test
    public void shouldGetByteProperty() {
        Property<B> property = whenGetProperty("byteProperty");

        then(property.getScalar()).is(validScalar(BYTE_VALUE));
    }

    @Test
    public void shouldGetShortProperty() {
        Property<B> property = whenGetProperty("shortProperty");

        then(property.getScalar()).is(validScalar(SHORT_VALUE));
    }

    @Test
    public void shouldGetIntProperty() {
        Property<B> property = whenGetProperty("intProperty");

        then(property.getScalar()).is(validScalar(INT_VALUE));
    }

    @Test
    public void shouldGetIntegerProperty() {
        Property<B> property = whenGetProperty("integerProperty");

        then(property.getScalar()).is(validScalar(INTEGER_VALUE));
    }

    @Test
    public void shouldGetLongProperty() {
        Property<B> property = whenGetProperty("longProperty");

        then(property.getScalar()).is(validScalar(LONG_VALUE));
    }

    @Test
    public void shouldGetFloatProperty() {
        Property<B> property = whenGetProperty("floatProperty");

        then(property.getScalar()).is(validScalar(FLOAT_VALUE));
    }

    @Test
    public void shouldGetDoubleProperty() {
        Property<B> property = whenGetProperty("doubleProperty");

        then(property.getScalar()).is(validScalar(DOUBLE_VALUE));
    }


    private <T> Condition<Scalar<B>> validScalar(T expectedValue) {
        return new Condition<>(scalar -> scalarPredicate(scalar, expectedValue), "");
    }

    private <T> boolean scalarPredicate(Scalar<B> scalar, T expectedValue) {
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
        return true; // dummy return value... actually we use nested asserts
    }

    @Test
    public void shouldGetIntSequenceProperty() {
        Property<B> property = whenGetProperty("intArrayProperty");

        then(property.getSequence()).is(validSequence(INT_LIST_VALUE));
    }

    private <T> Condition<Sequence<B>> validSequence(List<T> expectedValue) {
        return new Condition<>(sequence -> sequencePredicate(sequence, expectedValue), "");
    }

    private <T> boolean sequencePredicate(Sequence<B> sequence, List<T> expectedValue) {
        then(sequence.size(object)).isEqualTo(expectedValue.size());
        return true; // dummy return value... actually we use nested asserts
    }
}
