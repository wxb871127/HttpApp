package http.com.testhttp;

//import com.httpplugin.Linhao;


import com.http.annotation.RequestRegister;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.QueryMap;

public interface RequsetAPI {
//    @POST("updatesss$")
//    Call<List<GitHubRepo>> listRepos(@QueryMap Map<String, Object> map);


    /*app首次使用时的授权*/
    @POST("sq/sqm/_search")
    String getSQM(@Body Map map);
}
