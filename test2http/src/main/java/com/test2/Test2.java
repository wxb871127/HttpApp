package com.test2;

import android.util.Log;

import com.google.gson.Gson;
import com.httplib.HttpRequest;
import com.test2.testtj.DefaultCallBack;
import com.test2.testtj.ListData;
import com.test2.testtj.Plan;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import http.com.test2http.RequsetAPI;
import io.reactivex.disposables.Disposable;
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
        HttpRequest.request("getSQM").parameter(requestBody).from(RequsetAPI.class).create().
        execute(new HttpRequest.CallBack<ResponseBody>() {
            @Override
            public void onSubscribe(Disposable d) {

            }

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

    public static void testTj(){
        final Map<String, String> param = new HashMap<>();
        param.put("startdate", "");
        param.put("enddate", "");
        param.put("page", "20");
        param.put("rp", "1");
        param.put("search", "");
        param.put("state", "0");
        param.put("institutionalcode", "320105426020199");
        final List<Plan> dataList = new ArrayList();
        HttpRequest.request("getPlanList").parameter(param).from(RequsetAPI.class).create()
                .execute(new DefaultCallBack(new DefaultCallBack.CallBack<ListData<Plan>>() {
                    @Override
                    public void onSuccess(ListData<Plan> datas) {
                    }

                    @Override
                    public void onFailed(String var1) {

                    }
                }));
    }

}
