package com.httplib;

import com.google.gson.Gson;

import java.io.IOException;
import java.util.Map;

import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Converter;

public class NonstandardRequstBodyConverter implements Converter<Map, RequestBody> {
    private Gson gson ;

    public NonstandardRequstBodyConverter(Gson gson) {
        this.gson = gson;
    }

    @Override
    public RequestBody convert(Map value) throws IOException {
        return FormBody.create(MediaType.parse("application/json; charset=utf-8"), getParams(value));
    }

    private String getParams(Map paramMap)
    {
        if (paramMap == null || paramMap.isEmpty())
        {
            return "";
        }
        return gson.toJson(paramMap);
    }
}
