package com.github.t1.metatest;

import com.github.t1.meta.Meta;
import com.github.t1.meta.json.JsonGenerator;
import com.google.common.collect.ImmutableList;
import org.junit.Test;

import java.math.BigInteger;

import static java.math.BigInteger.TEN;
import static org.assertj.core.api.Assertions.assertThat;

public class JsonGeneratorTest {
    private static final double PI = 3.14159;
    private final Meta meta = new Meta();
    private final JsonGenerator generator = new JsonGenerator();

    @Test
    public void shouldGenerateJsonObject() {
        class Pojo {
            String one = "a", two = "b";
            double three = PI;
            boolean four = true;
            BigInteger five = TEN;
        }
        Pojo pojo = new Pojo();

        meta.getGuideTo(pojo).guide(generator);

        assertThat(generator).hasToString(""
                + "{"
                + "\"one\":\"a\","
                + "\"two\":\"b\","
                + "\"three\":3.14159,"
                + "\"four\":true,"
                + "\"five\":10"
                + "}");
    }

    @Test
    public void shouldGenerateJsonArray() {
        ImmutableList<?> list = ImmutableList.of("a", "b", PI, true, TEN);

        meta.getGuideTo(list).guide(generator);

        assertThat(generator).hasToString("[\"a\",\"b\",3.14159,true,10]");
    }
}
