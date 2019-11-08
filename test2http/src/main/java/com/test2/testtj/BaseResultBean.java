package com.test2.testtj;

import com.google.gson.annotations.SerializedName;

/**
 * Created by zhaojian on 2018/2/5.
 */

public class BaseResultBean<T>
{
    public int code;
    public String message;
    public HttpErrorDetail errorDetail;
    @SerializedName("result")
    public T data; //数据
}
