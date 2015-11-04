package com.github.t1.meta.test;

import static com.github.t1.exap.reflection.ReflectionProcessingEnvironment.*;
import static java.util.Arrays.*;
import static java.util.stream.Collectors.*;
import static javax.tools.Diagnostic.Kind.*;
import static javax.tools.StandardLocation.*;
import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.io.IOException;
import java.net.URI;
import java.nio.file.*;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import com.github.t1.exap.Round;
import com.github.t1.exap.reflection.Message;
import com.github.t1.meta.*;

@RunWith(MockitoJUnitRunner.class)
public class MetaAnnotationProcessorTest {
    @Mock
    Round round;

    MetaAnnotationProcessor processor = new MetaAnnotationProcessor();

    private void given(Class<?>... types) {
        when(round.typesAnnotatedWith(GenerateMeta.class))
                .thenReturn(asList(types).stream().map(t -> ENV.type(t)).collect(toList()));
    }

    private String created(String className) {
        String pkg = getClass().getPackage().getName();
        String relativeName = getClass().getSimpleName() + className;
        String createdResource = ENV.getCreatedResource(SOURCE_OUTPUT, pkg, relativeName);
        assertThat(createdResource) //
                .describedAs("created class resource: %s ### %s\nactually found: %s", pkg, relativeName,
                        ENV.getCreatedResources())
                .isNotNull();
        return createdResource;
    }

    private String sourceOf(Class<?> type) throws IOException {
        String fileName = type.getName().replace('.', '/') + ".java";
        Path path = Paths.get("src/test/java").resolve(fileName);
        return new String(Files.readAllBytes(path));
    }

    static class Nested {
        boolean someBooleanProperty;
    }

    @GenerateMeta
    static class Pojo {
        public String publicValue;
        private int intValueWithGetter;
        private URI uriWithFluentGetter;

        @SuppressWarnings("unused")
        private boolean ungettable;
        // Nested nested;

        public int getIntValueWithGetter() {
            return intValueWithGetter;
        }

        public URI uriWithFluentGetter() {
            return uriWithFluentGetter;
        }

        public int getUngettable() {
            return 0;
        }
    }

    @Test
    public void shouldGenerateMetaForSimpleProperty() throws Exception {
        given(Pojo.class);

        processor.process(round);

        assertThat(created("$PojoProperties")) //
                .isEqualTo(sourceOf(MetaAnnotationProcessorTest$PojoProperties.class));
        assertThat(ENV.getMessages()).containsExactly( //
                new Message(ENV.type(Pojo.class).getMethod("getUngettable"), WARNING,
                        "looks like a getter for field ungettable but it has the wrong return type"), //
                new Message(ENV.type(Pojo.class).getField("ungettable"), ERROR,
                        "No matching getter found. Make the field public; " //
                                + "or add a public getter method 'getUngettable()'; " //
                                + "or a fluent public getter method 'ungettable()'.") //
        );
    }
}
