package com.github.t1.metatest;

import com.github.t1.meta.Meta;
import com.github.t1.meta.builder.Builder;
import com.google.common.collect.ImmutableMap;
import lombok.Data;
import org.junit.Test;

import javax.enterprise.util.TypeLiteral;
import java.util.Map;

import static org.assertj.core.api.Assertions.*;

public class MetaBuilderTest {
    private final Meta meta = new Meta();

    @Test
    public void shouldBuildMap() {
        @SuppressWarnings("unchecked")
        Builder<Map<String, String>> builder = meta.builderFor(new TypeLiteral<Map<String, String>>() {});

        builder.set("key1", "value1");
        builder.set("key2", "value2");

        assertThat(builder.build()).isEqualTo(ImmutableMap.of("key1", "value1", "key2", "value2"));
    }

    @Data
    private static class Pojo {
        String key1;
        String key2;
    }

    @Test
    public void shouldBuildPojo() {
        Builder<Pojo> builder = meta.builderFor(Pojo.class);

        builder.set("key1", "value1");
        builder.set("key2", "value2");

        Pojo pojo = builder.build();
        assertThat(pojo.getKey1()).isEqualTo("value1");
        assertThat(pojo.getKey2()).isEqualTo("value2");
    }

    @Test
    public void shouldBuildPojoFromTypeLiteral() {
        Builder<Pojo> builder = meta.builderFor(new TypeLiteral<Pojo>() {});

        builder.set("key1", "value1");
        builder.set("key2", "value2");

        Pojo pojo = builder.build();
        assertThat(pojo.getKey1()).isEqualTo("value1");
        assertThat(pojo.getKey2()).isEqualTo("value2");
    }
}
