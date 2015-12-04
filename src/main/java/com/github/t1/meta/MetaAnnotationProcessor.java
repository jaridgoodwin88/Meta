package com.github.t1.meta;

import static com.github.t1.meta.FieldPropertyGenerator.*;
import static com.github.t1.meta.StringUtils.*;
import static javax.lang.model.SourceVersion.*;

import java.util.*;
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
    @Override
    public boolean process(Round round) {
        for (Type pojoType : round.typesAnnotatedWith(GenerateMeta.class))
            metaTypeFor(pojoType);

        return false;
    }

    public String metaTypeFor(Type pojoType) {
        String typeName = generatedTypeName(pojoType);
        log.debug("generate {} from {}", typeName, pojoType.getFullName());
        try (TypeGenerator generator = pojoType.getPackage().openTypeGenerator(typeName)) {
            generator.addTypeParameter("B");
            constructorMethod(pojoType, typeName, generator);
            FieldGenerator backtrackField = backtrackField(pojoType, generator);
            constructor(generator, backtrackField);
            metaProperties(pojoType, generator);
            fieldProperties(pojoType, generator);
        } catch (RuntimeException e) {
            log.error("failed to process {}", pojoType.getFullName());
            pojoType.error(MetaAnnotationProcessor.class.getName() + ": " + e.toString());
        }
        return typeName;
    }

    private String generatedTypeName(Type pojoType) {
        return pojoType.getRelativeName().replace('.', '_') + "Properties";
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
        stringMethod(type, "$id", initLower(pojoType.getRelativeName()));
        stringMethod(type, "$title", camelToTitle(pojoType.getSimpleName()));
        stringMethod(type, "$description", description(pojoType));
        propertiesMethod(pojoType, type);
    }

    private void stringMethod(TypeGenerator type, String name, String result) {
        type.addMethod(name).body("return \"" + result + "\";").returnType("String");
    }

    private String description(Type pojoType) {
        if (pojoType.isAnnotated(JavaDoc.class))
            return pojoType.getAnnotation(JavaDoc.class).value();
        return "";
    }

    private void fieldProperties(Type pojoType, TypeGenerator generator) {
        for (Field field : pojoType.getAllFields())
            new FieldPropertyGenerator(this, field).addTo(generator);
    }

    private void propertiesMethod(Type pojoType, TypeGenerator type) {
        type.addImport(type(List.class)).addImport(type(Arrays.class)).addImport(type(Property.class));
        StringJoiner body = new StringJoiner(", ", "return Arrays.asList(", ");");
        for (Field field : pojoType.getAllFields())
            if (isProperty(field))
                body.add(field.getName() + "()");

        type.addMethod("$properties").body(body.toString()).returnType("List<Property<?, B>>");
    }

    private boolean isProperty(Field field) {
        FieldPropertyGenerator propertyGenerator = new FieldPropertyGenerator(this, field);
        return propertyGenerator.getter() != null && propertyGenerator.propertyType() != null;
    }
}
