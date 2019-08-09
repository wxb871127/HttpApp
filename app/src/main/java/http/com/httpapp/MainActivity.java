package http.com.httpapp;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.http.RequestAPI;
import com.httplib.config.HttpConfig;
import com.test.Test;
import com.test2.Test2;

import java.io.File;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Button button1 = findViewById(R.id.button1);
        Button button2 = findViewById(R.id.button2);

        HttpConfig.init();

        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RequestAPI.class.getMethods();//.getGenericParameterTypes();
                Test.test();
            }
        });

        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Test2.test();
            }
        });
    }
}
