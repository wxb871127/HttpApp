package http.com.httpapp;

import android.app.Application;
import android.util.Log;

import com.httplib.HttpRequest;

import okhttp3.Request;
import okhttp3.Response;

public class App extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
//        HttpRequest.initUrl("https://api.github.com/");
//        HttpRequest.initUrl("http://baidu.com");
//        HttpRequest.initUrl("http://172.18.13.233:9090/");
        HttpRequest.initUrl("http://203.110.176.174:9090/pes/");
        HttpRequest.initException(new HttpRequest.RequestException() {
            @Override
            public void handle(Response response) {
                Log.e("xx",response.request().toString());
                Log.e("yy", response.toString());
                response.request().url().uri().toString();

            }
        });
    }
}
