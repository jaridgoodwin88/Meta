package com.github.t1.meta;

import static com.github.t1.meta.StringUtils.*;

import java.net.URI;

import com.github.t1.exap.JavaDoc;
import com.github.t1.exap.generator.*;
import com.github.t1.exap.reflection.*;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class FieldPropertyGenerator {
    private static final Type STRING_PROPERTY = type(StringProperty.class);
    private static final Type INTEGER_PROPERTY = type(IntegerProperty.class);
    private static final Type BOOLEAN_PROPERTY = type(BooleanProperty.class);
    private static final Type URI_PROPERTY = type(UriProperty.class);

    static Type type(Class<?> klass) {
        return ReflectionProcessingEnvironment.ENV.type(klass);
    }

    private final MetaAnnotationProcessor metaAnnotationProcessor;
    private final Field field;

    public void addTo(TypeGenerator typeGenerator) {
        String getter = getter(true);
        if (getter == null)
            return;
        Type propertyType = propertyType();
        MethodGenerator methodGenerator = typeGenerator.addMethod(field.getName());
        if (propertyType == null) {
            String typeName = metaAnnotationProcessor.metaTypeFor(field.getType());
            methodGenerator //
                    .body(nestedPropertyBody(getter, typeName)) //
                    .returnType(typeName).typeVar("B");
        } else {
            methodGenerator //
                    .body(simplePropertyBody(getter, propertyType.getSimpleName())) //
                    .returnType(propertyType).typeVar("B");
        }
    }

    public String getter() {
        return getter(false);
    }

    private String getter(boolean generateErrorsAndWarnings) {
        if (field.isPublic())
            return field.getName();

        String getter = "get" + initUpper(field.getName());
        if (hasGettingMethod(getter, generateErrorsAndWarnings))
            return getter + "()";

        String iser = field.getType().isBoolean() ? "is" + initUpper(field.getName()) : null;
        if (iser != null && hasGettingMethod(iser, generateErrorsAndWarnings))
            return iser + "()";

        String fluentGetter = field.getName();
        if (hasGettingMethod(fluentGetter, generateErrorsAndWarnings))
            return fluentGetter + "()";

        if (generateErrorsAndWarnings)
            field.error("No matching getter found. Make the field public; " //
                    + "or add a public getter method '" + getter + "()'; " //
                    + ((iser == null) ? "" : ("or add a public 'is' method '" + iser + "()'; ")) //
                    + "or a fluent public getter method '" + fluentGetter + "()'.");
        return null;
    }

    private boolean hasGettingMethod(String getter, boolean generateErrorsAndWarnings) {
        Type declaringType = field.getDeclaringType();
        if (!declaringType.hasMethod(getter))
            return false;
        Method method = declaringType.getMethod(getter);
        if (!method.getParameters().isEmpty())
            return false;
        if (method.getReturnType().isA(field.getType()))
            return true;
        if (generateErrorsAndWarnings)
            method.warning("looks like a getter for field " + field.getName() + " but it has the wrong return type");
        return false;
    }

    public Type propertyType() {
        Type type = field.getType();
        if (type.isString())
            return STRING_PROPERTY;
        if (type.isBoolean())
            return BOOLEAN_PROPERTY;
        if (type.isInteger())
            return INTEGER_PROPERTY;
        if (type.isA(URI.class))
            return URI_PROPERTY;
        return null;
    }

    private String nestedPropertyBody(String getter, String propertyType) {
        return "Function<B, Optional<" + field.getType().getRelativeName() + ">> backtrack =\n" //
                + "                source -> this.backtrack.apply(source).map(container -> container." + getter + ");\n" //
                + "        return new " + propertyType + "<>(backtrack);";
    }

    private String simplePropertyBody(String getter, String propertyType) {
        return "return new " + propertyType + "<>(" //
                + "\"" + propertyId() + "\", " //
                + "\"" + propertyTitle() + "\", " //
                + "\"" + propertyDescription() + "\",\n" //
                + "                source -> this.backtrack.apply(source).map(container -> container." + getter + "));";
    }

    private String propertyId() {
        return field.getName();
    }

    private String propertyTitle() {
        return camelToTitle(field.getName());
    }

    private String propertyDescription() {
        if (field.isAnnotated(JavaDoc.class))
            return field.getAnnotation(JavaDoc.class).value();
        return "";
    }
}
