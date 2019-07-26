package com.test2;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.httplib.APIManager;

import http.com.test2http.BuildConfig;
import http.com.test2http.R;

//import com.annotationlib$$test2http.MyGeneratedClass;
//import com.httplib.APIManager;


public class Test2Activity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Button button = findViewById(R.id.httplib);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                APIManager.init("https://api.github.com/",RequsetAPI.class, null);
                APIManager.test();
            }
        });

//        MyGeneratedClass.aaa2(null);
    }

//     void register(String fileName) throws IOException {
//        TypeSpec.Builder classBuilder = TypeSpec.classBuilder("RequestProxy")
//                .addModifiers(Modifier.PUBLIC, Modifier.FINAL);
//        String tag = "linhao";
//        MethodSpec method = MethodSpec.methodBuilder(tag)
//                .addModifiers(Modifier.PUBLIC, Modifier.STATIC)
//                .returns(void.class)
//                .addParameter(String[].class, "args")
//                .addStatement("$T.out.println($S)", System.class, "Hello, JavaPoet!" + tag)
//                .build();
//        classBuilder.addMethod(method);
//        JavaFile javaFile = JavaFile.builder("com.httplib", classBuilder.build())
//                .build();
//        String path = "./src/main/java/com/http/";
//        File dir = new File(path);
//        if(!dir.exists())
//            dir.mkdir();
//        File file = new File(path,"RequestProxy.java");
//        if(!file.exists())
//            file.createNewFile();
//        javaFile.writeTo(System.out);
////        println 'linhao ...... end'
//        javaFile.writeTo(file);
//    }
}
