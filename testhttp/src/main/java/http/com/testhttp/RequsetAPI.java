package http.com.testhttp;

import com.test.NonstandardBaseResult;
import com.test.ProjectUrlResult;
import java.util.List;
import java.util.Map;
import io.reactivex.Observable;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface RequsetAPI {
    @POST("sq/sqm/_search")
    Observable<List<NonstandardBaseResult<ProjectUrlResult>>> getSQM(@Body Map map);
}
