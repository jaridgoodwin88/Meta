package com.github.t1.meta2.test;

import com.github.t1.meta2.Mapping;
import com.github.t1.meta2.Scalar;
import com.github.t1.meta2.Sequence;
import com.github.t1.meta2.Structure;
import com.github.t1.meta2.Structure.Property;
import org.assertj.core.groups.Tuple;
import org.junit.Ignore;
import org.junit.Test;

import java.util.List;
import java.util.Optional;
import java.util.stream.IntStream;

import static com.github.t1.meta2.Structure.Kind.scalar;
import static com.github.t1.meta2.Structure.Kind.sequence;
import static com.github.t1.meta2.util.JavaCast.PRIMITIVE_WRAPPER_SCALARS;
import static java.util.Arrays.asList;
import static java.util.stream.Collectors.toList;
import static org.assertj.core.api.Assertions.catchThrowable;
import static org.assertj.core.api.Assertions.tuple;
import static org.assertj.core.api.BDDAssertions.assertThat;
import static org.assertj.core.api.BDDAssertions.then;
import static org.junit.Assume.assumeTrue;

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
    private static final int OUT_OF_INDEX = 99;

    final B object = createObject();

    protected abstract B createObject();

    final Mapping<B> mapping = createMapping();

    protected abstract Mapping<B> createMapping();

    protected boolean hasSchema() {
        return false;
    }

    @Test
    public void shouldGetProperties() {
        assumeTrue(hasSchema());

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

                        tuple("nestedProperty", Structure.Kind.mapping),
                        tuple("nestingProperty", Structure.Kind.mapping),
                        tuple("outerNestingProperty", Structure.Kind.mapping));
    }

    @Test
    public void shouldGetNestedProperties() {
        assumeTrue(hasSchema());

        List<Property<B>> nestedProperties = mapping.getMapping("nestedProperty").getProperties();

        then(nestedProperties)
                .extracting(this::nameAndKind)
                .containsExactly(
                        tuple("nestedStringProperty", scalar),
                        tuple("nestedIntegerProperty", scalar),
                        tuple("nestedSequenceProperty", sequence));
    }

    @Test
    public void shouldGetNestingProperties() {
        assumeTrue(hasSchema());

        List<Property<B>> nestingProperties = mapping.getMapping("nestingProperty").getProperties();

        then(nestingProperties)
                .extracting(this::nameAndKind)
                .containsExactly(
                        tuple("nested0", Structure.Kind.mapping),
                        tuple("nested1", Structure.Kind.mapping));

        List<Property<B>> nested0 = mapping.getMapping("nestingProperty").getMapping("nested0").getProperties();

        then(nested0)
                .extracting(this::nameAndKind)
                .containsExactly(
                        tuple("nestedStringProperty", scalar),
                        tuple("nestedIntegerProperty", scalar),
                        tuple("nestedSequenceProperty", sequence));
    }

    @Test
    public void shouldGetOuterNestingProperties() {
        assumeTrue(hasSchema());

        List<Property<B>> outerNestingProperties = mapping.getMapping("outerNestingProperty").getProperties();

        then(outerNestingProperties)
                .extracting(this::nameAndKind)
                .containsExactly(
                        tuple("nestingPojo", Structure.Kind.mapping),
                        tuple("nestingPojoList", sequence),
                        tuple("nestedPojoArray", sequence));

        List<Property<B>> nested0 =
                mapping.getMapping("outerNestingProperty").getMapping("nestingPojo").getProperties();

        then(nested0)
                .extracting(this::nameAndKind)
                .containsExactly(
                        tuple("nested0", Structure.Kind.mapping),
                        tuple("nested1", Structure.Kind.mapping));
    }

    @Test
    public void shouldGetRootPath() {
        assertThat(mapping.getPath()).hasToString("/");
    }

    @Test
    public void shouldGetNestedPropertyPath() {
        assertThat(mapping.getMapping("nestedProperty").getPath()).hasToString("/nestedProperty");
    }

    @Test
    public void shouldGetNestingPropertyPath() {
        assertThat(mapping.getMapping("nestingProperty").getMapping("nested0").getPath())
                .hasToString("/nestingProperty/nested0");
    }

    @Test
    public void shouldGetOuterNestingPropertyPath() {
        assertThat(mapping.getMapping("outerNestingProperty").getMapping("nestingPojo").getMapping("nested1").getPath())
                .hasToString("/outerNestingProperty/nestingPojo/nested1");
    }

    @Test
    @Ignore
    public void shouldGetOuterNestingListPropertyPath() {
        Mapping<B> m =
                this.mapping.getMapping("outerNestingProperty").getMapping("nestingPojoList").getMapping("nested1");
        assertThat(m.getScalar("nestedStringProperty").get(object, String.class)).contains("x");
        assertThat(m.getPath()).hasToString("/outerNestingProperty/nestingPojoList/nested1");
    }

    private Tuple nameAndKind(Property<?> property) {
        return tuple(property.getName(), property.getKind());
    }


    protected Property<B> getProperty(String name) {
        Property<B> property = mapping.getProperty(name).get();

        then(property).isNotNull();
        then(property.getName()).isEqualTo(name);

        return property;
    }

    @Test
    public void shouldGetUndefinedScalarAsEmpty() {
        Scalar<B> scalar = mapping.getScalar("undefined");

        then(scalar.get(object, String.class)).isEmpty();
    }

    @Test
    public void shouldGetStringScalar() {
        Scalar<B> property = mapping.getScalar("stringProperty");

        assertScalar(property, STRING_VALUE);
    }

    @Test
    public void shouldGetBooleanScalar() {
        Scalar<B> property = mapping.getScalar("booleanProperty");

        assertScalar(property, BOOLEAN_VALUE);
    }

    @Test
    public void shouldGetCharScalar() {
        Scalar<B> property = mapping.getScalar("charProperty");

        assertScalar(property, CHARACTER_VALUE);
    }

    @Test
    public void shouldGetByteScalar() {
        Scalar<B> property = mapping.getScalar("byteProperty");

        assertScalar(property, BYTE_VALUE);
    }

    @Test
    public void shouldGetShortScalar() {
        Scalar<B> property = mapping.getScalar("shortProperty");

        assertScalar(property, SHORT_VALUE);
    }

    @Test
    public void shouldGetIntScalar() {
        Scalar<B> property = mapping.getScalar("intProperty");

        assertScalar(property, INT_VALUE);
    }

    @Test
    public void shouldGetIntegerScalar() {
        Scalar<B> property = mapping.getScalar("integerProperty");

        assertScalar(property, INTEGER_VALUE);
    }

    @Test
    public void shouldGetLongScalar() {
        Scalar<B> property = mapping.getScalar("longProperty");

        assertScalar(property, LONG_VALUE);
    }

    @Test
    public void shouldGetFloatScalar() {
        Scalar<B> property = mapping.getScalar("floatProperty");

        assertScalar(property, FLOAT_VALUE);
    }

    @Test
    public void shouldGetDoubleScalar() {
        Scalar<B> property = mapping.getScalar("doubleProperty");

        assertScalar(property, DOUBLE_VALUE);
    }

    private <T> void assertScalar(Scalar<B> scalar, T expectedValue) {
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
    public void shouldGetIntArraySequence() {
        Sequence<B> property = mapping.getSequence("intArrayProperty");

        assertSequence(property, INT_LIST_VALUE, Integer.class);
    }

    @Test
    public void shouldGetIntegerListSequence() {
        Sequence<B> property = mapping.getSequence("intListProperty");

        assertSequence(property, INT_LIST_VALUE, Integer.class);
    }

    @Test
    public void shouldGetStringListSequence() {
        Sequence<B> property = mapping.getSequence("stringListProperty");

        assertSequence(property, STRING_LIST_VALUE, String.class);
    }

    private <T> void assertSequence(Sequence<B> sequence, List<T> expectedValues, Class<T> elementType) {
        int expectedSize = expectedValues.size();
        for (int i = 0; i < expectedSize; i++)
            then(sequence.getScalar(i).get(object, elementType)).contains(expectedValues.get(i));
        then(sequence.get(object, elementType)).isEqualTo(expectedValues);
    }

    @Test
    public void shouldGetNestedScalar() {
        Scalar<B> scalar = mapping.getMapping("nestedProperty").getScalar("nestedStringProperty");

        assertScalar(scalar, "nestedString");
    }

    @Test
    public void shouldGetNestedSequence() {
        Sequence<B> sequence = mapping.getMapping("nestedProperty").getSequence("nestedSequenceProperty");

        assertSequence(sequence, STRING_LIST_VALUE, String.class);
    }

    @Test
    public void shouldGetNestedSequenceSequence() {
        Sequence<B> list = mapping.getSequence("nestedSequenceSequenceProperty");

        // TODO assertThat(list.size(object)).isEqualTo(2);
        // TODO assertThat(list.getSequence(object, 0).size(object)).isEqualTo(2);

        // TODO if (hasSchema())
        // TODO assertThat(list.get(1).getKind()).isEqualTo(sequence);
        // TODO assertThat(list.getSequence(1).size(object)).isEqualTo(3);

        Sequence<B> sequence0 = list.getSequence(0);
        assertThat(sequence0.getScalar(0).get(object, String.class)).contains("A1");
        assertThat(sequence0.getScalar(1).get(object, String.class)).contains("A2");

        Sequence<B> sequence1 = list.getSequence(1);
        assertThat(sequence1.getScalar(0).get(object, String.class)).contains("B1");
        assertThat(sequence1.getScalar(1).get(object, String.class)).contains("B2");
        assertThat(sequence1.getScalar(2).get(object, String.class)).contains("B3");

        assertThat(list.getSequence(OUT_OF_INDEX).getScalar(2).get(object, String.class)).isEmpty();
        assertThat(sequence1.getScalar(OUT_OF_INDEX).get(object, String.class)).isEmpty();
    }

    @Test
    public void shouldGetUndefinedNestedScalarAsEmpty() {
        Scalar<B> scalar = mapping.getMapping("nestedProperty").getScalar("undefined");

        then(scalar.get(object, String.class)).isEmpty();
    }

    @Test
    public void shouldGetDoublyNestedProperty() {
        // TODO Property<B> outer = getProperty("nestingProperty");
        // TODO Property<B> middle = outer.get("nestedProperty");
        // TODO Property<B> property = middle.get("nestedStringProperty");

        // TODO assertScalar(property, "nestedString");
    }

    @Test
    @Ignore
    public void shouldGetPropertyPath() {
        // TODO Property<B> property = mapping.getPropertyPath("nestingProperty/nestedProperty/nestedStringProperty");
        //
        // assertScalar(property.getScalar(), "nestedString");
    }
}
