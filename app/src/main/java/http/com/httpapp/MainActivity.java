package http.com.httpapp;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.http.GitHubRepo;
import com.http.NonstandParam;
import com.http.RequestAPI;
import com.httplib.HttpRequest;
import com.httplib.config.HttpConfig;
import com.test.Test;
import com.test2.Test2;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import io.reactivex.disposables.Disposable;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Response;
import okhttp3.ResponseBody;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;

public class MainActivity extends AppCompatActivity {

    private TextView progressBar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Button appButton = findViewById(R.id.appButton);
        Button appButton1 = findViewById(R.id.appButton1);
        Button button1 = findViewById(R.id.button1);
        Button button2 = findViewById(R.id.button2);
        Button button3 = findViewById(R.id.button3);
        Button button4 = findViewById(R.id.button4);
        progressBar = findViewById(R.id.progressBar);
        Button button5 = findViewById(R.id.button5);
        Button button6 = findViewById(R.id.button6);

        HttpConfig.init();
        appButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
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

        appButton1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                HttpRequest.request("listRepos").parameter("octocat").from(RequestAPI.class)
                        .create().execute(new HttpRequest.CallBack<List<GitHubRepo>>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onSuccess(List<GitHubRepo> object) {
                        for(GitHubRepo gitHubRepo : object) {
                            Log.e("gitHubRepo", gitHubRepo.toString());
                        }
                    }

                    @Override
                    public void onFailed(String msg) {
                        Log.e("d", msg);
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

        button4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                downloadFile();
            }
        });

        button5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Test.login();
            }
        });

        button6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Test2.testTj();
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

    private void downloadFile(){
        HttpRequest.downloadFile("aaa/1.0.31_zhoushanjiankang.apk", getExternalCacheDir() + "/" + "demo.apk", new HttpRequest.CallStatus() {
            @Override
            public void onDownload(int progress) {
                progressBar.setText("下载中，已完成"+progress+"%");
            }

            @Override
            public void onSucess(String filePath) {
                progressBar.setText("成功下载"+filePath);
            }

            @Override
            public void onFailed(String msg) {
                Toast.makeText(MainActivity.this, msg, Toast.LENGTH_LONG).show();
            }
        });
    }
}
