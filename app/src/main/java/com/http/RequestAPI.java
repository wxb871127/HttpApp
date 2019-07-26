package com.http;




import com.httplib.bean.RequestParams;

import org.json.JSONObject;

import java.util.Map;

import io.reactivex.Observable;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface RequestAPI {

    @POST
    Observable<JSONObject> getGxyZxda(@Body RequestParams requestParams);

    @POST
    Observable<Map> getGxyFcpg(Map requestParams);

}
