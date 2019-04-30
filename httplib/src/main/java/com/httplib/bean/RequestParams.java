package com.httplib.bean;

import java.util.Map;

/**
 * Created by zhaojian on 2018/2/2.
 */

public class RequestParams<T>
{
    public static final String METHOD = "method";
    public static final String PARAMS = "params";
    public static final String EXTRA = "extras";
    public String method;
    public Map<String, T> paramMap;
    public String localMethodName;
}
