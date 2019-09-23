package http.com.httpapp;

import android.app.Application;

import com.httplib.HttpRequest;

public class App extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        HttpRequest.initUrl("https://api.github.com/");
    }
}
