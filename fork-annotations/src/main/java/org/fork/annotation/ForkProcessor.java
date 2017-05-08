package org.fork.annotation;

import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.TypeName;
import com.squareup.javapoet.TypeSpec;

import java.io.IOException;
import java.util.Set;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.RoundEnvironment;
import javax.annotation.processing.SupportedAnnotationTypes;
import javax.annotation.processing.SupportedSourceVersion;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;
import javax.lang.model.type.MirroredTypeException;
import javax.lang.model.type.TypeMirror;


@SupportedAnnotationTypes({"org.fork.annotation.ForkLayoutId", "org.fork.annotation.ForkPresenter"})
@SupportedSourceVersion(SourceVersion.RELEASE_7)
@SuppressWarnings("All")
public class ForkProcessor extends AbstractProcessor {
    private String packageName;
    private String activityName;
    private TypeMirror activityClass;

    private int layoutId;
    private String presenterName;

    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
        if (annotations.size() > 0) {
            parseBindViews(annotations, roundEnv);
            javaPoet();
        }
        return true;
    }

    private void javaPoet() {
        MethodSpec getPresenter = MethodSpec.methodBuilder("getPresenter")
                .addModifiers(Modifier.PUBLIC, Modifier.FINAL)
                .returns(Object.class)
                .addParameter(TypeName.OBJECT, "activity")
                .addStatement("return new " + presenterName + "((" + activityName + ")activity)")
                .build();

        MethodSpec getLayoutId = MethodSpec.methodBuilder("getLayoutId")
                .addModifiers(Modifier.PUBLIC, Modifier.FINAL)
                .returns(int.class)
                .addStatement("return " + layoutId)
                .build();

        TypeSpec clazz = TypeSpec.classBuilder(activityName + "$$Provider")
                .addModifiers(Modifier.PUBLIC, Modifier.FINAL)
                .addSuperinterface(Provider.class)
                .addMethod(getPresenter)
                .addMethod(getLayoutId)
                .build();

        JavaFile.Builder builder = JavaFile
                .builder(packageName, clazz);
        JavaFile javaFile = builder.build();

        try {
            javaFile.writeTo(processingEnv.getFiler());
            //javaFile.writeTo(System.out);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void parseBindViews(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
        for (Element element : roundEnv.getElementsAnnotatedWith(ForkLayoutId.class)) {
            if (element.getKind() == ElementKind.CLASS) {
                layoutId = element.getAnnotation(ForkLayoutId.class).value();
                activityName = element.getSimpleName().toString();
                activityClass = element.asType();
                packageName = element.toString().replace("." + activityName, "");
            }
        }

        for (Element element : roundEnv.getElementsAnnotatedWith(ForkPresenter.class)) {
            if (element.getKind() == ElementKind.CLASS) {
                try {
                    presenterName = element.getAnnotation(ForkPresenter.class).value().getSimpleName().toString();
                } catch (MirroredTypeException mte) {
                    presenterName = mte.getTypeMirror().toString().replace(packageName + ".", "");
                }
            }
        }
        System.err.println(" layout id annotation: " + layoutId);
        System.err.println(" presenter name annotation: " + presenterName);

        System.err.println(" package name annotation: " + packageName);
        System.err.println(" activity name annotation: " + activityName);
        System.err.println(" activity class annotation: " + activityClass);
    }
}
