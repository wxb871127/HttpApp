package http.com.test2http;


import com.http.annotation.RequestRegister;

import java.util.HashMap;
import java.util.List;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.QueryMap;

public interface RequsetAPI {
    @GET("getsss$")
    Call<List<GitHubRepo>> listRepos(@QueryMap HashMap<String, Object> map);
}
