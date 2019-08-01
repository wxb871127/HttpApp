package com.httplib;

import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.lang.reflect.Type;

import okhttp3.ResponseBody;
import retrofit2.Converter;

public class NonstandardResponseConverter<T> implements Converter<ResponseBody, T> {

    private final Gson gson;
    private final Type type;

    public NonstandardResponseConverter(Gson gson, Type type) {
        this.gson = gson;
        this.type = type;
    }

    @Override
    public T convert(ResponseBody value) throws IOException {
        //return null;

        String response = value.string();
        try {
            JSONObject object = new JSONObject(response);
            JSONObject hitsObj = object.optJSONObject("hits");
            if (hitsObj != null) {
                JSONArray array = hitsObj.optJSONArray("hits");
                return (T) gson.fromJson(array.toString(), type);

            } else {
//                ApiException exception = new ApiException(ApiException.TYPE_HTTP, new HttpErrorDetail("Nonstandard", "数据为空"));
                RuntimeException ex = new RuntimeException();
                throw ex;
            }

        } catch (JSONException e) {
            //e.printStackTrace();
            RuntimeException ex = new RuntimeException("Nonstandard", e);
            throw ex;
        } finally {
            value.close();
        }
    }
}
