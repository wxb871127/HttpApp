package http.com.httpcompiler;

import com.google.auto.service.AutoService;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.TypeSpec;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.Filer;
import javax.annotation.processing.Messager;
import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.Processor;
import javax.annotation.processing.RoundEnvironment;
import javax.annotation.processing.SupportedAnnotationTypes;
import javax.annotation.processing.SupportedSourceVersion;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;
import javax.lang.model.util.Elements;
import javax.tools.FileObject;
import javax.tools.JavaFileManager;
import javax.tools.JavaFileObject;

import http.com.httpannotation.Test;
import retrofit2.http.GET;

@AutoService(Processor.class)
@SupportedSourceVersion(SourceVersion.RELEASE_8)
@SupportedAnnotationTypes({"retrofit2.http.POST","retrofit2.http.GET","http.com.httpannotation.Test"})
public class HttpProcessor extends AbstractProcessor {
    private Filer mFiler;
    private Elements mElementUtils;
    private Messager mMessager;

    @Override
    public synchronized void init(ProcessingEnvironment processingEnv) {
        super.init(processingEnv);

        mElementUtils = processingEnv.getElementUtils();

        mMessager = processingEnv.getMessager();

        mFiler = processingEnv.getFiler();

    }

    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
        Set<? extends Element> elements = roundEnv.getElementsAnnotatedWith(Test.class);
        TypeSpec.Builder typeSpecBuilder = TypeSpec.classBuilder("MyGeneratedClass")
                .addModifiers(Modifier.PUBLIC);

        if(elements.size() > 0) {
            for (Element element : elements)
            {
                if (element.getKind() != ElementKind.METHOD)
                    throw new IllegalArgumentException("xxx");
                Test get = element.getAnnotation(Test.class);
                String tag = get.value();
                MethodSpec method = MethodSpec.methodBuilder(tag)
                            .addModifiers(Modifier.PUBLIC, Modifier.STATIC)
                            .returns(void.class)
                            .addParameter(String[].class, "args")
                            .addStatement("$T.out.println($S)", System.class, "Hello, JavaPoet!" + tag)
                            .build();


                if(method != null){
                    typeSpecBuilder.addMethod(method);
                }
            }
            JavaFile javaFile = JavaFile.builder("com.annotationlib", typeSpecBuilder.build()).build();
            try {

                javaFile.writeTo(mFiler);
            } catch (IOException e) {
                e.printStackTrace();
            }
            return true;
        }else
            return false;
    }
}
