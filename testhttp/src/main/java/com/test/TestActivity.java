package com.test;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.httplib.APIManager;

import http.com.testhttp.R;



public class TestActivity extends AppCompatActivity {
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
//        MyGeneratedClass.aaaa(null);
    }

//    @Test("ccc")
    public void getId() {

    }
}
