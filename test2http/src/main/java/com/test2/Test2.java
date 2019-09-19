package com.test2;

import android.util.Log;
import com.google.gson.Gson;
import com.httplib.HttpRequest;
import java.util.Map;
import http.com.test2http.RequsetAPI;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;

public class Test2 {
    public static class Student{
        public int num;
        public String name;
    }
    public static void test(){
        Map map = NonstandParam.getQueryParams("tjzxsqm", "aaaa");
        Gson gson = new Gson();
        String jsonObject = gson.toJson(map);
        RequestBody requestBody = RequestBody.create(MediaType.parse("application/json"), jsonObject);
        HttpRequest.request("getSQM").parameter(requestBody).from(RequsetAPI.class).build().
        setListener(new HttpRequest.OnRequestListener<ResponseBody>() {
            @Override
            public void onSuccess(ResponseBody object) {
                if(object != null)
                    Log.e("d", object.toString());
            }

            @Override
            public void onFailed(String msg) {
                Log.e("d", msg);
            }
        });
    }

}
