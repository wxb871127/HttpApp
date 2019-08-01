package com.test2;

import java.util.HashMap;
import java.util.Map;

public class NonstandParam {
    public static Map getInnerMap(String key, Object value) {
        Map<String, Object> map = new HashMap<>();
        map.put(key, value);
        return map;
    }

    public static Map getMatchMap(Map<String, Object> inner) {
        Map<String, Object> map = new HashMap<>();
        map.put("match", inner);//指定key为match
        return map;
    }

    public static Map getQueryParamsMap(Map<String, Object> body) {
        Map<String, Object> map = new HashMap<>();
        map.put("query", body);//指定key为query
        return map;
    }

    public static Map getQueryParams(String key,Object value) {
        return getQueryParamsMap(getMatchMap(getInnerMap(key,value)));
    }
}
