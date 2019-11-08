package com.test;

import android.util.Log;
import com.httplib.HttpRequest;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import http.com.testhttp.RequsetAPI;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class Test {
    public static void test(){

        Map map = NonstandParam.getQueryParams("tjzxsqm", "yhtj");
        HttpRequest.request("getSQM").parameter(map).from(RequsetAPI.class).create()
        .execute(new HttpRequest.CallBack<List<NonstandardBaseResult<ProjectUrlResult>>>() {
            @Override
            public void onSubscribe(Disposable d) {

            }

            @Override
            public void onSuccess(List<NonstandardBaseResult<ProjectUrlResult>> object) {
                Log.e("d", object.toString());
            }

            @Override
            public void onFailed(String msg) {
                Log.e("d", msg);
            }
        });
    }

    public static void login(){
        HashMap map = new HashMap();
        map.put("loginname", "ad");
        map.put("password", "123");
        HttpRequest.request("login").parameter(map).from(RequsetAPI.class).create()
                .execute(new HttpRequest.CallBack<Object>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onSuccess(Object object) {
                        Log.e("d", object.toString());
                    }

                    @Override
                    public void onFailed(String msg) {
                        Log.e("d", msg);
                    }
                });
    }
}
