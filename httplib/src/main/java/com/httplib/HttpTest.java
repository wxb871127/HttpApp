package com.httplib;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

public class HttpTest {
    static List<Method> methods = new ArrayList<>();
    public static void registerPostMethod(Method method){
        methods.add(method);
    }

    public static Method getMethod(String methodName){
        for (Method method : methods) {
            if(method.getName().equals(methodName)){
                return  method;
            }
        }
        return null;
    }
}
