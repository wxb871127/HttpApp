package http.com.httpapp;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.http.NonstandParam;
import com.http.RequestAPI;
import com.httplib.HttpRequest;
import com.httplib.config.HttpConfig;
import com.test.Test;
import com.test2.Test2;

import java.util.ArrayList;
import java.util.Map;

import io.reactivex.disposables.Disposable;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Button appButton = findViewById(R.id.appButton);
        Button button1 = findViewById(R.id.button1);
        Button button2 = findViewById(R.id.button2);
        Button button3 = findViewById(R.id.button3);

        HttpConfig.init();
        appButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

//                HttpRequest.request("listRepos").parameter("octocat").from(RequestAPI.class)
//                        .create().execute(new HttpRequest.CallBack<List<GitHubRepo>>() {
//                    @Override
//                    public void onSubscribe(Disposable d) {
//
//                    }
//
//                    @Override
//                    public void onSuccess(List<GitHubRepo> object) {
//                        for(GitHubRepo gitHubRepo : object) {
//                            Log.e("gitHubRepo", gitHubRepo.toString());
//                        }
//                    }
//
//                    @Override
//                    public void onFailed(String msg) {
//                        Log.e("d", msg);
//                    }
//                });

                HttpRequest.request("getTjjl").parameter("1", "2").from(RequestAPI.class).create()
                        .execute(new HttpRequest.CallBack() {
                            @Override
                            public void onSubscribe(Disposable d) {

                            }

                            @Override
                            public void onSuccess(Object object) {
                                Log.e("xxxxx", object.toString());
                            }

                            @Override
                            public void onFailed(String msg) {

                            }
                        });

            }
        });

        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Test.test();
            }
        });

        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Test2.test();
            }
        });

        button3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                querySQM();

            }
        });

    }

    private void querySQM(){
        Map map = NonstandParam.getQueryParams("tjzxsqm", "yhtj");
        HttpRequest.request("getSQM").parameter(map).from(RequestAPI.class).create().execute(new HttpRequest.CallBack<Object>() {
            @Override
            public void onSubscribe(Disposable disposable) {

            }

            @Override
            public void onSuccess(Object datas) {
//                HttpRequest.initUrl("http://yapi.demo.qunar.com/mock/286/");
                HttpRequest.initUrl("http://yapi.demo.qunar.com/");
                queryYSList();
            }

            @Override
            public void onFailed(String s) {
                Log.e("xx", s);
            }
        });
    }

    private void queryYSList(){
        Map map = NonstandParam.getQueryExaminerParams(0, 10);
        HttpRequest.request("getUploadedExaminer").parameter(map).from(RequestAPI.class).create()
                .execute(new HttpRequest.CallBack<Object>() {
                    @Override
                    public void onSubscribe(Disposable disposable) {

                    }

                    @Override
                    public void onSuccess(Object listData) {

                        Log.e("xx", listData.toString());

                    }

                    @Override
                    public void onFailed(String s) {
                        Log.e("xx", s);
                    }
                });
    }


}
