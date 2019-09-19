package com.http.compiler;

import com.google.auto.service.AutoService;
import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.CodeBlock;
import com.squareup.javapoet.FieldSpec;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.ParameterSpec;
import com.squareup.javapoet.TypeName;
import com.squareup.javapoet.TypeSpec;
import org.apache.commons.collections4.MapUtils;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.Filer;
import javax.annotation.processing.Messager;
import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.Processor;
import javax.annotation.processing.RoundEnvironment;
import javax.annotation.processing.SupportedAnnotationTypes;
import javax.annotation.processing.SupportedOptions;
import javax.annotation.processing.SupportedSourceVersion;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.AnnotationMirror;
import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;
import javax.lang.model.element.VariableElement;
import javax.lang.model.type.DeclaredType;
import javax.lang.model.type.TypeMirror;
import javax.lang.model.util.Elements;
import javax.lang.model.util.Types;
import javax.tools.Diagnostic;
import io.reactivex.Observable;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.POST;

@AutoService(Processor.class)
@SupportedOptions({"moduleName"})
@SupportedSourceVersion(SourceVersion.RELEASE_8)
@SupportedAnnotationTypes({"retrofit2.http.POST","retrofit2.http.GET"})
public class HttpProcessor extends AbstractProcessor {
    private Filer mFiler;
    private Elements mElementUtils;
    private Messager mMessager;
    private String moduleName = null;
    private String from;

    @Override
    public synchronized void init(ProcessingEnvironment processingEnv) {
        super.init(processingEnv);
        mElementUtils = processingEnv.getElementUtils();
        mMessager = processingEnv.getMessager();
        mFiler = processingEnv.getFiler();
        Map<String, String> options = processingEnv.getOptions();
        if (MapUtils.isNotEmpty(options)) {
            moduleName = options.get("moduleName");
        }
    }

    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
        mMessager.printMessage(Diagnostic.Kind.WARNING, "start annotationProcess..............");
        Set<? extends Element> postElements = roundEnv.getElementsAnnotatedWith(POST.class);
        Set<? extends Element> getElements = roundEnv.getElementsAnnotatedWith(GET.class);
        if(postElements.size() == 0 && getElements.size() == 0)
            return true;
        try {
            TypeSpec.Builder typeSpecBuilder = TypeSpec.classBuilder("RequestProxy")
                    .addModifiers(Modifier.PUBLIC);
            if(postElements.size() > 0) {
                for(Element element : postElements) {
                    typeSpecBuilder.addMethod(parseMethod(element, POST.class));
                }
            }
            if(getElements.size() > 0){
                for(Element element : getElements)
                    typeSpecBuilder.addMethod(parseMethod(element, GET.class));
            }

            typeSpecBuilder.addField(parseField());

            ClassName annotation = ClassName.get("com.httplib.annotation", "HttpProxy");
            typeSpecBuilder.addAnnotation(annotation);


//            annotation1.
            //            try {
//                Class a = annotation.getClass();
//                Field field = a.getField("from");
//                field.setAccessible(true);
//                field.set(annotation.getClass().newInstance(), from);
//                typeSpecBuilder.addAnnotation(a);
//            } catch (NoSuchFieldException e) {
//                e.printStackTrace();
//            } catch (IllegalAccessException e) {
//                e.printStackTrace();
//            } catch (InstantiationException e) {
//                e.printStackTrace();
//            }


            JavaFile javaFile = JavaFile.builder("com.http."+moduleName, typeSpecBuilder.build()).build();
            javaFile.writeTo(mFiler);
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    private List<ParameterSpec> parseParams(ExecutableElement executableElement){
        List<? extends VariableElement> params = executableElement.getParameters();
        List<ParameterSpec> list = new ArrayList<>();
        for(VariableElement variableElement : params){
            ParameterSpec parameterSpec = ParameterSpec.builder(ClassName.get(variableElement.asType()),
                    variableElement.getSimpleName().toString(), Modifier.FINAL).build();
            list.add(parameterSpec);
        }
        return list;
    }

    private String parseArguments(ExecutableElement executableElement){
        List<? extends VariableElement> params = executableElement.getParameters();
        String arguments = "";
        for(VariableElement variableElement : params){
            arguments += variableElement.getSimpleName().toString() + ",";
        }
        return arguments.substring(0, arguments.length()-1);
    }

    private FieldSpec parseField(){
        return FieldSpec.builder(String.class, "from")
                .addModifiers(Modifier.PUBLIC)
                .initializer(CodeBlock.of("$S", from)).build();
    }

    private MethodSpec parseMethod(Element element, Class annotationClass){
        if (element.getKind() != ElementKind.METHOD)
            mMessager.printMessage(Diagnostic.Kind.ERROR, "only method annotation will be processor");

        List<? extends AnnotationMirror> tannotations = element.getAnnotationMirrors();
        TypeElement annotationElement = mElementUtils.getTypeElement(annotationClass.getName());
        String methodName = element.getSimpleName().toString();
        String annotationName = null;
//        for (AnnotationMirror mirror : tannotations) {
//            if (mirror.getAnnotationType().asElement().equals(annotationElement)) {
//                Map<? extends ExecutableElement, ? extends AnnotationValue> values = mirror.getElementValues();
//                if(values.size() == 1) {
//                    AnnotationValue[] annotationValues = new AnnotationValue[values.size()];
//                    values.values().toArray(annotationValues);
//                    annotationName = annotationValues[0].getValue().toString();
//                }
//                break;
//            }
//        }

        ExecutableElement methodElement = (ExecutableElement) element;
        TypeName returnType = ClassName.get(methodElement.getReturnType());
        List<ParameterSpec> params = parseParams(methodElement);
        ClassName apiManger = ClassName.get("com.httplib", "APIManager");
        TypeElement typeElement = (TypeElement)(element.getEnclosingElement());
        ClassName requestAPI = ClassName.get(typeElement);
        from = requestAPI.toString();


        TypeMirror typeMirror = methodElement.getReturnType();
        typeMirror = ((DeclaredType)typeMirror).asElement().asType();
        TypeMirror typeMirror1 = mElementUtils.getTypeElement(Call.class.getName()).asType();
        TypeMirror typeMirror2 = mElementUtils.getTypeElement(Observable.class.getName()).asType();
        Types types = processingEnv.getTypeUtils();


        typeMirror.toString();
        MethodSpec.Builder methodBuilder = MethodSpec.methodBuilder(methodName)
                .addModifiers(Modifier.PUBLIC, Modifier.STATIC)
                .addStatement("return $T.getAPI($T.class).$L("+ parseArguments(methodElement) +")",
                        apiManger, requestAPI, methodName);

//        if(types.isSameType(typeMirror, typeMirror1)){
////             methodBuilder.addStatement("call.");
//        }else if(types.isSameType(typeMirror, typeMirror2)){
//            Observable<ResponseBody> call = null;
//
//        }
        methodBuilder.addParameters(params).returns(returnType);
        return methodBuilder.build();
    }

}
