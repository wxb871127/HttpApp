package http.com.httpapp;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.http.AB;
import com.http.HttpRequest;
//import com.http.testhttp.RequestProxy;
import com.httplib.APIManager;
import com.test.Test;
import com.test2.Test2;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import http.com.testhttp.RequsetAPI;
import retrofit2.http.GET;
import retrofit2.http.POST;

//import com.httplib.HttpTest;

public class MainActivity extends AppCompatActivity {
    private Button button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Button button1 = findViewById(R.id.button1);
        Button button2 = findViewById(R.id.button2);


        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

//                Map map = NonstandParam.getQueryParams("tjzxsqm", "aaaa");
//                String s = RequestProxy.getSQM(map);
//                Log.e("xxxxxx", s);
            }
        });

        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                APIManager.test();
                Test2.test();
            }
        });


//        button.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                aVoid();
//
//                ClassLoader loader = Thread.currentThread().getContextClassLoader();
//                try {
//                    Class claz = loader.loadClass("Example");
//                    claz.getMethods()[0].invoke(null, new Object[] { null });
//                } catch (ClassNotFoundException e) {
//                    e.printStackTrace();
//                } catch (IllegalAccessException e) {
//                    e.printStackTrace();
//                } catch (InvocationTargetException e) {
//                    e.printStackTrace();
//                }
//            }
//        });
    }

    public List<AB<HttpRequest>> bVoid(Map<String, Object> b){

        return null;
    }

    public List<String> aVoid(int a){
//        try {
//            HttpTest.registerPostMethod(MyGeneratedClass.class.getMethod("aaa2", String[].class));
//        } catch (NoSuchMethodException e) {
//            e.printStackTrace();
//        }
//        Method  method = HttpTest.getMethod("aaa2");
//        try {
//            String[] strings = new String[]{"xx"};
//            MyGeneratedClass.aaa2(strings);
//            method.invoke(null, new Object[]{strings});
//        } catch (IllegalAccessException e) {
//            e.printStackTrace();
//        } catch (InvocationTargetException e) {
//            e.printStackTrace();
//        }

//        List<Method> list = new ArrayList<>();
        Map<String, Object> map = new HashMap<>();
        Method[] methods = RequsetAPI.class.getClass().getMethods();
        Method[] methods1 = http.com.test2http.RequsetAPI.class.getClass().getMethods();
        if(methods[0].equals(methods1[0])){
            Log.e("xxxxxxxx", "");
        }
        return null;
    }


}
