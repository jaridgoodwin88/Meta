package com.github.t1.metatest;

import com.github.t1.meta.Meta;
import com.github.t1.meta.out.JsonGenerator;
import com.google.common.collect.*;
import org.junit.Test;

import java.math.BigInteger;

import static java.math.BigInteger.*;
import static org.assertj.core.api.Assertions.*;

public class JsonGeneratorTest {
    private static final double PI = 3.14159;
    private final Meta meta = new Meta();
    private final JsonGenerator generator = new JsonGenerator();

    @SuppressWarnings("unused")
    private static class Pojo {
        String string = "a";
        double number = PI;
        boolean bool = true;
        BigInteger big = TEN;
        Object nil = null;
    }

    private final ImmutableMap<?, ?> map = ImmutableMap.of(
            "string", "a",
            "number", PI,
            "bool", true,
            "big", TEN);

    private final Object[] array = new Object[] { "a", PI, true, TEN, null };

    private final ImmutableList<?> list = ImmutableList.of("a", PI, true, TEN);

    @Test
    public void shouldGeneratePlainJsonObjectFromPojo() {
        Pojo pojo = new Pojo();

        meta.visitTo(pojo).by(generator).run();

        assertThat(generator).hasToString(""
                + "{"
                + "\"string\":\"a\","
                + "\"number\":3.14159,"
                + "\"bool\":true,"
                + "\"big\":10"
                + "}");
    }

    @Test
    public void shouldGenerateJsonObjectFromNestedPojo() {
        @SuppressWarnings("unused")
        class Container {
            Pojo pojo = new Pojo();
            Object[] array = JsonGeneratorTest.this.array;
            Object[] nested = { new Pojo(), true };
        }
        Container container = new Container();

        meta.visitTo(container).by(generator).run();

        assertThat(generator.toString()).isEqualTo(""
                + "{"
                + "\"pojo\":{"
                + "\"string\":\"a\","
                + "\"number\":3.14159,"
                + "\"bool\":true,"
                + "\"big\":10"
                + "},"
                + "\"array\":["
                + "\"a\","
                + "3.14159,"
                + "true,"
                + "10"
                + "],"
                + "\"nested\":["
                + "{"
                + "\"string\":\"a\","
                + "\"number\":3.14159,"
                + "\"bool\":true,"
                + "\"big\":10"
                + "},"
                + "true"
                + "]"
                + "}");
    }

    @Test
    public void shouldGenerateJsonObjectFromMap() {
        meta.visitTo(map).by(generator).run();

        assertThat(generator).hasToString(""
                + "{"
                + "\"string\":\"a\","
                + "\"number\":3.14159,"
                + "\"bool\":true,"
                + "\"big\":10"
                + "}");
    }

    @Test
    public void shouldGenerateJsonArrayFromList() {
        meta.visitTo(list).by(generator).run();

        assertThat(generator).hasToString("[\"a\",3.14159,true,10]");
    }

    @Test
    public void shouldGenerateJsonArrayFromArray() {
        meta.visitTo(array).by(generator).run();

        assertThat(generator).hasToString("[\"a\",3.14159,true,10]");
    }
}
