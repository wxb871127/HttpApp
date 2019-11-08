package com.http;

import com.httplib.annotation.TypeAdapter;

import java.util.List;
import java.util.Map;

import io.reactivex.Observable;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Streaming;
import retrofit2.http.Url;

public interface RequestAPI {

    @GET("users/{user}/repos")
//    @Headers("baseUrl:https://api.github.com/")
    Call<List<GitHubRepo>> listRepos(@Path("user") String user);

    @POST
    Observable<Map> getGxyFcpg(Map requestParams);

    @GET("mock/286/baseurl/examinationrecord1/{examinationid}/{departmentid}")
    @Headers("baseUrl:http://yapi.demo.qunar.com/")
    Observable<FormResult> getTjjl(@Path("examinationid") String name, @Path("departmentid") String pwd);


    @Headers("baseUrl:http://172.18.13.233:9200/")
    @POST("sq/sqm/_search")
    Observable<Object> getSQM(@Body Map requestParams);


    /*获取医生已上传列表*/
    @Headers("baseUrl:http://172.18.13.233:9200/")
    @POST("ryxx/ryxx/_search")
    Observable<Object> getUploadedExaminer(@Body Map requestParams);


//    @Streaming //注解这个请求将获取数据流,此后将不会这些获取的请求数据保存到内存中,将交与你操作.
//    @GET
//    Call<ResponseBody> download(@Url String url);


}
