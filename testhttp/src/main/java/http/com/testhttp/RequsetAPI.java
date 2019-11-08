package http.com.testhttp;

import com.httplib.annotation.HttpProxy;
import com.httplib.annotation.TypeAdapter;
import com.test.NonstandardBaseResult;
import com.test.ProjectUrlResult;
import com.test.User;
import com.test.adapter.ESAdapter;

import java.util.List;
import java.util.Map;
import io.reactivex.Observable;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface RequsetAPI {

//    @POST("sq/sqm/_search")
//    @TypeAdapter(adapter = ESAdapter.class)
//    @Headers("baseUrl:http://172.18.13.233:9200/")
//    Observable<List<NonstandardBaseResult<ProjectUrlResult>>> getSQM(@Body Map map);


    @POST("sqm/_search")
    @TypeAdapter(adapter = ESAdapter.class)
//    @Headers("baseUrl:http://172.18.13.233:9200/")
    Observable<List<NonstandardBaseResult<ProjectUrlResult>>> getSQM(@Body Map map);

    Observable<List<ProjectUrlResult>> getSQM1(@Body Map map);

    /*登陆接口*/
    @POST("baseurl/user")
    @Headers("baseUrl:http://172.18.13.233:9200/")
    Observable<Object> login(@Body Map requestParams);
}