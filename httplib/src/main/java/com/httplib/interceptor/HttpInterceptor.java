package com.httplib.interceptor;

import com.httplib.HttpRequest;

import java.io.IOException;
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
            HttpUrl newFullUrl = oldHttpUrl
                    .newBuilder()
                    .scheme(newBaseUrl.scheme())
                    .host(newBaseUrl.host())
                    .port(newBaseUrl.port())
                    .build();
            response = chain.proceed(builder.url(newFullUrl).build());
        }
        response = chain.proceed(request);
        if(exception != null)
            exception.handle(request, response);
        return response;
    }
}
