package com.http;

import org.json.JSONObject;

import java.util.List;
import java.util.Map;
import io.reactivex.Observable;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface RequestAPI {

    @GET("users/{user}/repos")
//    @Headers("baseUrl:https://api.github.com/")
    Call<List<GitHubRepo>> listRepos(@Path("user") String user);

    @POST
    Observable<Map> getGxyFcpg(Map requestParams);

    @GET("mock/286/baseurl/examinationrecord1/{examinationid}/{departmentid}")
    @Headers("baseUrl:http://yapi.demo.qunar.com/")
    Observable<Map> getTjjl(@Path("examinationid") String name, @Path("departmentid") String pwd);

}
