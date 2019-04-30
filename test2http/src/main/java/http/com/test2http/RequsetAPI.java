package http.com.test2http;

import java.util.HashMap;
import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.QueryMap;

public interface RequsetAPI {
    @GET("users")
    Call<List<GitHubRepo>> listRepos(@QueryMap HashMap<String, Object> map);
}
