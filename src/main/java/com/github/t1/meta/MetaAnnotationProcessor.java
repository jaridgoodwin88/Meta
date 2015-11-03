package com.github.t1.meta;

import static com.github.t1.meta.StringUtils.*;
import static javax.lang.model.SourceVersion.*;

import java.util.Optional;
import java.util.function.Function;

import javax.annotation.processing.SupportedSourceVersion;

import com.github.t1.exap.*;
import com.github.t1.exap.reflection.*;

@SupportedSourceVersion(RELEASE_8)
@SupportedAnnotationClasses({ GenerateMeta.class })
public class MetaAnnotationProcessor extends ExtendedAbstractProcessor {
    @Override
    public boolean process(Round round) {
        for (Type pojoType : round.typesAnnotatedWith(GenerateMeta.class)) {
            String typeName = pojoType.getClassName() + "Properties";
            try (TypeGenerator type = pojoType.getPackage().openTypeGenerator(typeName)) {
                type.addTypeParameter("B");
                type.addMethod(initLower(typeName)) //
                        .setStatic() //
                        .body("return new " + typeName + "<>(source -> Optional.ofNullable(source));") //
                        .returnType(typeName).typeArg(pojoType);
                FieldGenerator backtrackField = type.addField("backtrack").setFinal();
                backtrackField //
                        .type(type(Function.class)) //
                        .typeVar("B") //
                        .typeArg(type(Optional.class)).typeArg(pojoType);
                type.addConstructor() //
                        .body("this.backtrack = backtrack;") //
                        .addParameter("backtrack").type(backtrackField.getType());
                for (Field field : pojoType.getAllFields()) {
                    String propertyType = propertyType(field.getType());
                    type.addMethod(field.getName()) //
                            .body("return new " + propertyType + "<>(\"" + field.getName() + "\", \""
                                    + propertyTitle(field) + "\", \"\",\n"
                                    + "                source -> this.backtrack.apply(source).map(container -> container."
                                    + field.getName() + "));") //
                            .returnType(propertyType).typeVar("B");
                }
            }
        }
        return false;
    }

    private String propertyType(Type type) {
        if (type.isString())
            return "StringProperty";
        if (type.isInteger())
            return "IntegerProperty";
        throw new UnsupportedOperationException("properties of type '" + type.getFullName() + "' not implemented, yet");
    }

    private String propertyTitle(Field field) {
        return initUpper(camelToSpace(field.getName()));
    }

    public Type type(Class<?> klass) {
        return ReflectionProcessingEnvironment.ENV.type(klass);
    }
}
