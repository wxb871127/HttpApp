package com.test;

import android.util.Log;
import com.httplib.HttpRequest;
import java.util.List;
import java.util.Map;
import http.com.testhttp.RequsetAPI;

public class Test {
    public static void test(){
        Map map = NonstandParam.getQueryParams("tjzxsqm", "aaaa");
//        Observable<List<NonstandardBaseResult<ProjectUrlResult>>> call = RequestProxy.getSQM(map);
        HttpRequest.request("getSQM").parameter(map).from(RequsetAPI.class).build()
        .setListener(new HttpRequest.OnRequestListener<List<NonstandardBaseResult<ProjectUrlResult>>>() {
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
}
