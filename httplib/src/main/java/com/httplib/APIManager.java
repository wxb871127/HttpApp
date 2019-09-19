package com.httplib;

import com.google.gson.GsonBuilder;
import com.httplib.interceptor.HttpInterceptor;
import java.lang.reflect.Type;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;

public class APIManager {
    private static OkHttpClient client;
    private static ConcurrentHashMap<String, Object> APICache = new ConcurrentHashMap<>();
    private static String mUrl = "http://172.18.13.233:9200/";
    private static Retrofit retrofit;
    private static GsonBuilder gsonBuilder = new GsonBuilder();

    public void registerAdapter(Type type, Object object){
        gsonBuilder.registerTypeAdapter(type, object);
    }

    public static <T> T getAPI(Class<T> clazz)
    {
        if (null == client)
            client = getOkHttpClient();

        if(retrofit == null){
            Retrofit.Builder builder = new Retrofit.Builder().client(client)
                    .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                    .baseUrl(mUrl);
            if(gsonBuilder != null)
                builder.addConverterFactory(HttpConvertFactory.create(gsonBuilder.create()));
            else
                builder.addConverterFactory(HttpConvertFactory.create());
            retrofit = builder.build();
        }

        T api = (T) APICache.get(clazz.getName());
        if (api == null)
        {
            synchronized (APIManager.class)
            {
                if (api != null) return api;

                api = retrofit.create(clazz);
                APICache.put(clazz.getName(), api);
            }
        }
        return api;
    }

    private static OkHttpClient getOkHttpClient()
    {
       OkHttpClient.Builder builder =  new OkHttpClient.Builder()
                .readTimeout(60, TimeUnit.SECONDS)
                .connectTimeout(10, TimeUnit.SECONDS)
                .writeTimeout(60, TimeUnit.SECONDS)
               .addInterceptor(new HttpInterceptor());
       return  builder.build();
    }
}
