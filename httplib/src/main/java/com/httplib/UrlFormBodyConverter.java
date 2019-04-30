package com.httplib;

import com.google.gson.Gson;
import com.httplib.bean.RequestParams;

import java.io.IOException;
import java.util.Map;

import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Converter;

/**
 * Created by zhaojian on 2018/2/3.
 * urlForm格式消息转换器
 */

public class UrlFormBodyConverter<T> implements Converter<RequestParams<T>, RequestBody>
{
    private Gson gson = GsonFactory.createCustomGson();

    @Override
    public RequestBody convert(RequestParams<T> requestParams) throws IOException
    {
        return FormBody.create(MediaType.parse("application/json; charset=utf-8"), getParams(requestParams.paramMap));
    }

    private String getParams(Map<String, T> paramMap)
    {
        if (paramMap == null || paramMap.isEmpty())
        {
            return "";
        }
        return gson.toJson(paramMap);
    }
}
