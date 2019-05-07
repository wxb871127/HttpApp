package http.com.httpapp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.annotationlib$$test2http.MyGeneratedClass;
import com.httplib.HttpTest;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

//import http.com.httpannotation.Test;

public class MainActivity extends AppCompatActivity {
    private Button button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        button = findViewById(R.id.button1);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                aVoid();
            }
        });

    }

//    @Test("bbb")
    public void aVoid(){
        try {
            HttpTest.registerPostMethod(MyGeneratedClass.class.getMethod("aaa2", String[].class));
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }
        Method  method = HttpTest.getMethod("aaa2");
        try {
            String[] strings = new String[]{"xx"};
            MyGeneratedClass.aaa2(strings);
            method.invoke(null, new Object[]{strings});
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
    }
}
