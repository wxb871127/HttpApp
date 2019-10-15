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
        HttpRequest.initUrl("http://baidu.com");
        HttpRequest.initException(new HttpRequest.RequestException() {
            @Override
            public void handle(Request request, Response response) {
                Log.e("xx",request.toString());
                Log.e("yy", response.toString());
            }
        });
    }
}
