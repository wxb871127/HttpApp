package http.com.test2http;


import com.test2.testtj.BaseResultBean;
import com.test2.testtj.ListData;
import com.test2.testtj.Plan;

import java.util.Map;

import io.reactivex.Observable;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface RequsetAPI {
    /*app首次使用时的授权*/
    @POST("sq/sqm/_search")
    @Headers("baseUrl:http://172.18.13.233:9200/")
    Call<ResponseBody> getSQM(@Body RequestBody body);

    /*获取计划列表*/
    @POST("baseurl/examinationplan")
    Observable<BaseResultBean<ListData<Plan>>> getPlanList(@Body Map requestParams);
}
