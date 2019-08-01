package com.httplib;

import com.google.gson.Gson;

import java.lang.annotation.Annotation;
import java.lang.reflect.Type;

import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Converter;
import retrofit2.Retrofit;

public class NonstandardConverterFactory extends Converter.Factory {

    public static NonstandardConverterFactory create(Gson gson) {
        if (gson == null) throw new NullPointerException("gson == null");
        return new NonstandardConverterFactory(gson);
    }

    private final Gson gson;

    private NonstandardConverterFactory(Gson gson) {
        this.gson = gson;
    }

    @Override
    public Converter<?, RequestBody> requestBodyConverter(Type type, Annotation[] parameterAnnotations, Annotation[] methodAnnotations, Retrofit retrofit) {
        //return super.requestBodyConverter(type, parameterAnnotations, methodAnnotations, retrofit);
        return new NonstandardRequstBodyConverter(gson);
    }

    @Override
    public Converter<ResponseBody, ?> responseBodyConverter(Type type, Annotation[] annotations, Retrofit retrofit) {
        //return super.responseBodyConverter(type, annotations, retrofit);
        return new NonstandardResponseConverter<>(gson,type);
    }
}
