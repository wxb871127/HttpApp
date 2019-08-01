package http.com.test2http;


import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface RequsetAPI {
    /*app首次使用时的授权*/
    @POST("sq/sqm/_search")
    Call<ResponseBody> getSQM(@Body RequestBody body);
}
