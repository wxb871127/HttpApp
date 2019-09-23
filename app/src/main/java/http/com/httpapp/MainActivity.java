package http.com.httpapp;

import android.app.ActivityManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import com.http.GitHubRepo;
import com.http.RequestAPI;
import com.httplib.HttpRequest;
import com.httplib.config.HttpConfig;
import com.test.Test;
import com.test2.Test2;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Button appButton = findViewById(R.id.appButton);
        Button button1 = findViewById(R.id.button1);
        Button button2 = findViewById(R.id.button2);

        HttpConfig.init();
        appButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                HttpRequest.request("listRepos").parameter("octocat").from(RequestAPI.class)
                        .create().execute(new HttpRequest.CallBack<List<GitHubRepo>>() {
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

    }
}
