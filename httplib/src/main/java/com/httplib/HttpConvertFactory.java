package com.httplib;

import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.reflect.TypeToken;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Converter;
import retrofit2.Retrofit;

public class HttpConvertFactory extends Converter.Factory{
    private Annotation annotation;

    public static HttpConvertFactory create() {
        return create(new Gson());
    }

    /**
     * Create an instance using {@code gson} for conversion. Encoding to JSON and
     * decoding from JSON (when no charset is specified by a header) will use UTF-8.
     */
    @SuppressWarnings("ConstantConditions") // Guarding public API nullability.
    public static HttpConvertFactory create(Gson gson) {
        if (gson == null) throw new NullPointerException("gson == null");
        return new HttpConvertFactory(gson);
    }

    private final Gson gson;

    private HttpConvertFactory(Gson gson) {
        this.gson = gson;
    }

    @Override
    public Converter<ResponseBody, ?> responseBodyConverter(Type type, Annotation[] annotations,
                                                            Retrofit retrofit) {
//        TypeAdapter<?> adapter = gson.getAdapter(TypeToken.get(type));
        TypeAdapter<?> adapter = null;
        for(Annotation annotation : annotations){
            if(com.httplib.annotation.TypeAdapter.class == annotation.annotationType()){
                Class aClass = ((com.httplib.annotation.TypeAdapter)annotation).adapter();
                try {
                    adapter  = (TypeAdapter)aClass.newInstance();
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                } catch (InstantiationException e) {
                    e.printStackTrace();
                }
            }
        }
        if(adapter == null)
            adapter = gson.getAdapter(TypeToken.get(type));

        return new HttpResponseBodyConverter<>(gson, adapter);
    }

    @Override
    public Converter<?, RequestBody> requestBodyConverter(Type type,
                                                          Annotation[] parameterAnnotations, Annotation[] methodAnnotations, Retrofit retrofit) {
        TypeAdapter<?> adapter = gson.getAdapter(TypeToken.get(type));
        return new HttpRequestBodyConverter<>(gson, adapter);
    }
}
