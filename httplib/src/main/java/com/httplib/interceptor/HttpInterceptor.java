package com.httplib.interceptor;

import com.httplib.APIManager;
import com.httplib.HttpRequest;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

public class HttpInterceptor implements Interceptor {
    private HttpRequest.RequestException exception;

    public HttpInterceptor(HttpRequest.RequestException exception){
        this.exception = exception;
    }

    @Override
    public Response intercept(Chain chain) throws IOException {
        //获取request
        Request request = chain.request();
        //从request中获取原有的HttpUrl实例oldHttpUrl
        HttpUrl oldHttpUrl = request.url();
        //获取request的创建者builder
        Request.Builder builder = request.newBuilder();
        //从request中获取headers，通过给定的键url_name
        List<String> headerValues = request.headers("baseUrl");
        Response response = null;
        if (headerValues != null && headerValues.size() > 0) {
            builder.removeHeader("baseUrl");
            //匹配获得新的BaseUrl，通过domin值，判断这个是哪一个方法需要使用什么baseUrl
            String headerValue = headerValues.get(0);
            HttpUrl newBaseUrl = HttpUrl.parse(headerValue);
            //重建新的HttpUrl，修改需要修改的url部分

            String uri = oldHttpUrl.scheme()+"://"+oldHttpUrl.host()+":"+oldHttpUrl.port()+"/";
            String oldPath = APIManager.mUrl.replace(uri, "");
            List<String> rwaPathSegments = oldHttpUrl.pathSegments();
            List<String> pathSegments = new ArrayList<>();
            for(String segment : rwaPathSegments)
                pathSegments.add(segment);

            if(!oldPath.isEmpty()) {
                String[] oldpathSegments = oldPath.split("/");
                for (int i = 0; i < oldpathSegments.length; i++) {
                    pathSegments.remove(oldpathSegments[i]);
                }
            }

            HttpUrl.Builder newBuilder = new HttpUrl.Builder()
                    .scheme(newBaseUrl.scheme())
                    .host(newBaseUrl.host())
                    .port(newBaseUrl.port());
            for(String segment : pathSegments)
                newBuilder.addPathSegment(segment);

            HttpUrl newFullUrl = newBuilder.build();

            response = chain.proceed(builder.url(newFullUrl).build());
        }else {
            response = chain.proceed(request);
        }
        Response response1 = response.newBuilder().body(response.body()).build();
        Response response2 = response.newBuilder().body(response.body()).build();
        if(exception != null)
            exception.handle(response1);
        return response2;
    }
}
