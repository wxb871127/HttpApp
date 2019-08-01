package com.httplib;

import android.util.Log;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class APIManager {
    private static OkHttpClient client;
    private static ConcurrentHashMap<String, Object> APICache = new ConcurrentHashMap<>();
    private static String mUrl = "http://172.18.13.233:9200/";
    private static Interceptor interceptor;
    private static List<Class> clazs = new ArrayList<>();

    public static void init(String url, Class clz, Interceptor interce)
    {
        mUrl = url;
        clazs.add(clz);
        interceptor = interce;
    }

    public static <T> T getAPI(Class<T> clazz)
    {
        if (null == client)
        {
            client = getOkHttpClient();
        }
        T api = (T) APICache.get(clazz.getName());
        if (api == null)
        {
            synchronized (APIManager.class)
            {
                if (api != null) return api;

                api = new Retrofit.Builder().client(client)
//                        .addConverterFactory(CustomConverterFactory.create(GsonFactory.createCustomGson()))
//                        .addConverterFactory(GsonConverterFactory.create()) //添加Gson
                        .addConverterFactory(NonstandardConverterFactory.create(GsonFactory.createCustomGson()))
                        .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                        .baseUrl(mUrl)
                        .build().create(clazz);
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
                .writeTimeout(60, TimeUnit.SECONDS);
       if(interceptor != null)
           builder = builder.addInterceptor(interceptor);
       return  builder.build();
    }

    public static void test(){
        StackTraceElement[] stackTraceElements = Thread.currentThread().getStackTrace();
        for(int i=0; i<stackTraceElements.length; i++) {
            Log.e("xxxxxxxx", Thread.currentThread().getStackTrace()[i].getClassName());
        }
    }

}
