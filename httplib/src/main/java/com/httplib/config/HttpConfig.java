package com.httplib.config;

import android.util.Log;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class HttpConfig {
    static  Map<Class, Class> BINDINGS = new LinkedHashMap<>();
    static List<String> list = new ArrayList<>();

    public static Class getProxyClass(Class clas){
        return BINDINGS.get(clas);
    }

    public static void init(){

    }

    public static void autoRegister(String clasName){
        list.add(clasName);
        try {
            Class clas = Class.forName(clasName);
            String from = (String)clas.getField("from").get(clas.newInstance());
            BINDINGS.put(Class.forName(from), clas);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
}
