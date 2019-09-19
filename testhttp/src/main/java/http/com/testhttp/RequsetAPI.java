package http.com.testhttp;

import com.httplib.annotation.HttpProxy;
import com.httplib.annotation.TypeAdapter;
import com.test.NonstandardBaseResult;
import com.test.ProjectUrlResult;
import com.test.adapter.ESAdapter;

import java.util.List;
import java.util.Map;
import io.reactivex.Observable;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface RequsetAPI {
//    @POST("sq/sqm/_search")
//    Observable<List<NonstandardBaseResult<ProjectUrlResult>>> getSQM(@Body Map map);

    @POST("sq/sqm/_search")
    @TypeAdapter(adapter = ESAdapter.class)
    Observable<List<NonstandardBaseResult<ProjectUrlResult>>> getSQM(@Body Map map);

    Observable<List<ProjectUrlResult>> getSQM1(@Body Map map);

}