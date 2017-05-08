package com.example;


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


@SupportedAnnotationTypes({"com.example.ForkLayoutId", "com.example.ForkPresenter"})
@SupportedSourceVersion(SourceVersion.RELEASE_7)
@SuppressWarnings("All")
public class ForkProcessor extends AbstractProcessor {
    private String packageName;
    private String activityName;
    private TypeMirror activityClass;

    private String bindingParamName;
    private String presenterParamName;

    private int layoutId;
    private String presenterName;

    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
        if (annotations.size() > 0) {
            parseBindViews(annotations, roundEnv);


            MethodSpec injectPresenter = MethodSpec.methodBuilder("injectPresenter")
                    .addModifiers(Modifier.PUBLIC, Modifier.FINAL)
                    .returns(void.class)
                    .addParameter(TypeName.get(activityClass), "activity")
                    //.addStatement("activity.setContentView(" + layoutId + ")")
                    .addStatement("activity." + presenterParamName + " = new " + presenterName + "(activity)")
                    .build();

            MethodSpec inject = MethodSpec.methodBuilder("inject")
                    .addModifiers(Modifier.PUBLIC, Modifier.FINAL)
                    .returns(void.class)
                    .addParameter(TypeName.OBJECT, "obj")
                    .addStatement("injectPresenter((" + activityName + ") obj)")
                    .addStatement("mActivity = ((" + activityName + ") obj)")
                    .build();

            MethodSpec getLayoutId = MethodSpec.methodBuilder("getLayoutId")
                    .addModifiers(Modifier.PUBLIC, Modifier.FINAL)
                    .returns(int.class)
                    .addStatement("return " + layoutId)
                    .build();

            MethodSpec getViewBinding = MethodSpec.methodBuilder("getViewBinding")
                    .addModifiers(Modifier.PUBLIC, Modifier.FINAL)
                    .returns(Object.class)
                    .addStatement("return mActivity." + bindingParamName)
                    .build();

            TypeSpec clazz = TypeSpec.classBuilder(activityName + "$$Provider")
                    .addModifiers(Modifier.PUBLIC, Modifier.FINAL)
                    .addField(TypeName.get(activityClass), "mActivity",Modifier.PRIVATE)
                    .addSuperinterface(Provider.class)
                    .addMethod(inject)
                    .addMethod(injectPresenter)
                    .addMethod(getLayoutId)
                    .addMethod(getViewBinding)
                    .build();

            JavaFile.Builder builder = JavaFile
                    .builder(packageName, clazz);
            JavaFile javaFile = builder.build();

            try {
                javaFile.writeTo(processingEnv.getFiler());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

  /*      try {
            //javaFile.writeTo(processingEnv.getFiler());
            javaFile.writeTo(System.out);
        } catch (IOException e) {
        }
*/
        return true;
    }

    private void parseBindViews(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
        for (Element element : roundEnv.getElementsAnnotatedWith(ForkLayoutId.class)) {
            TypeElement enclosingElement = (TypeElement) element.getEnclosingElement();
            activityName = enclosingElement.getSimpleName().toString();
            packageName = enclosingElement.getQualifiedName().toString().replace("." + activityName, "");
            activityClass = enclosingElement.asType();

            if (element.getKind() == ElementKind.FIELD) {
                bindingParamName = element.getSimpleName().toString();
                layoutId = element.getAnnotation(ForkLayoutId.class).value();
            }
        }

        for (Element element : roundEnv.getElementsAnnotatedWith(ForkPresenter.class)) {
            if (element.getKind() == ElementKind.FIELD) {
                presenterParamName = element.getSimpleName().toString();

                try {
                    presenterName = element.getAnnotation(ForkPresenter.class).value().getSimpleName().toString();
                } catch (MirroredTypeException mte) {
                    presenterName = mte.getTypeMirror().toString().replace(packageName + ".", "");
                }
            }
        }
        System.err.println(" binding param annotation: " + bindingParamName);
        System.err.println(" presenter param annotation: " + presenterParamName);

        System.err.println(" layout id annotation: " + layoutId);
        System.err.println(" presenter name annotation: " + presenterName);

        System.err.println(" package name annotation: " + packageName);
        System.err.println(" activity name annotation: " + activityName);
        System.err.println(" activity class annotation: " + activityClass);
    }
}
