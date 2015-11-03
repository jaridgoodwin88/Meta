package com.github.t1.meta;

import static com.github.t1.meta.StringUtils.*;
import static javax.lang.model.SourceVersion.*;

import java.net.URI;
import java.util.Optional;
import java.util.function.Function;

import javax.annotation.processing.SupportedSourceVersion;

import com.github.t1.exap.*;
import com.github.t1.exap.generator.*;
import com.github.t1.exap.reflection.*;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@SupportedSourceVersion(RELEASE_8)
@SupportedAnnotationClasses({ GenerateMeta.class })
public class MetaAnnotationProcessor extends ExtendedAbstractProcessor {
    private static final Type STRING_PROPERTY = type(StringProperty.class);
    private static final Type INTEGER_PROPERTY = type(IntegerProperty.class);
    private static final Type BOOLEAN_PROPERTY = type(BooleanProperty.class);
    private static final Type URI_PROPERTY = type(UriProperty.class);

    private static Type type(Class<?> klass) {
        return ReflectionProcessingEnvironment.ENV.type(klass);
    }

    @Override
    public boolean process(Round round) {
        for (Type pojoType : round.typesAnnotatedWith(GenerateMeta.class)) {
            String typeName = pojoType.getRelativeName().replace('.', '_') + "Properties";
            log.debug("generate {} from {}", typeName, pojoType.getFullName());
            try (TypeGenerator type = pojoType.getPackage().openTypeGenerator(typeName)) {
                type.addTypeParameter("B");
                constructorMethod(pojoType, typeName, type);
                FieldGenerator backtrackField = backtrackField(pojoType, type);
                constructor(type, backtrackField);
                metaProperties(pojoType, type);
                fieldProperties(pojoType, type);
            }
        }
        return false;
    }

    private void constructorMethod(Type pojoType, String typeName, TypeGenerator type) {
        type.addMethod(initLower(typeName)) //
                .setStatic() //
                .body("return new " + typeName + "<>(source -> Optional.ofNullable(source));") //
                .returnType(typeName).typeArg(pojoType);
    }

    private FieldGenerator backtrackField(Type pojoType, TypeGenerator type) {
        FieldGenerator backtrackField = type.addField("backtrack").setFinal();
        backtrackField //
                .type(type(Function.class)) //
                .typeVar("B") //
                .typeArg(type(Optional.class)).typeArg(pojoType);
        return backtrackField;
    }

    private void constructor(TypeGenerator type, FieldGenerator backtrackField) {
        type.addConstructor() //
                .body("this.backtrack = backtrack;") //
                .addParameter("backtrack").type(backtrackField.getType());
    }

    private void metaProperties(Type pojoType, TypeGenerator type) {
        type.addMethod("$id") //
                .body("return \"" + initLower(pojoType.getRelativeName()) + "\";") //
                .returnType("String");
        type.addMethod("$title") //
                .body("return \"" + title(pojoType.getSimpleName()) + "\";") //
                .returnType("String");
        type.addMethod("$description") //
                .body("return \"" + title(pojoType.getSimpleName()) + "\";") //
                .returnType("String");
    }

    private void fieldProperties(Type pojoType, TypeGenerator type) {
        for (Field field : pojoType.getAllFields()) {
            type.addMethod(field.getName()) //
                    .body("return new " + propertyType(field.getType()).getSimpleName() + "<>(\"" + field.getName()
                            + "\", \"" + propertyTitle(field) + "\", \"\",\n"
                            + "                source -> this.backtrack.apply(source).map(container -> container."
                            + field.getName() + "));") //
                    .returnType(propertyType(field.getType())).typeVar("B");
        }
    }

    private Type propertyType(Type type) {
        if (type.isString())
            return STRING_PROPERTY;
        if (type.isBoolean())
            return BOOLEAN_PROPERTY;
        if (type.isInteger())
            return INTEGER_PROPERTY;
        if (type.isA(URI.class))
            return URI_PROPERTY;
        throw new UnsupportedOperationException("properties of type '" + type.getFullName() + "' not implemented, yet");
    }

    private String propertyTitle(Field field) {
        return title(field.getName());
    }

    private String title(String string) {
        return initUpper(camelToSpace(string));
    }
}
