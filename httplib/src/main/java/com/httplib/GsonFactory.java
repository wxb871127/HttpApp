package com.httplib;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.TypeAdapterFactory;
import com.httplib.util.ReflectUtils;

import java.lang.reflect.Field;
import java.util.List;

public class GsonFactory
{

    public static Gson createCustomGson()
    {
        Gson gson =  new GsonBuilder()
                .registerTypeAdapter(Integer.class, new IntegerDefault0Adapter())
                .registerTypeAdapter(int.class, new IntegerDefault0Adapter())
                .registerTypeAdapter(Double.class, new DoubleDefault0Adapter())
                .registerTypeAdapter(double.class, new DoubleDefault0Adapter())
                .registerTypeAdapter(Long.class, new LongDefault0Adapter())
                .registerTypeAdapter(long.class, new LongDefault0Adapter())
                .registerTypeAdapterFactory(DefaultObjectTypeAdapter.FACTORY)
                .disableHtmlEscaping()
                .create();

        //通过反射替换gson中factories集合中系统自带的ObjectTypeAdapter为我们自定义的DefaultObjectTypeAdapter
        //在此Adapter中对数字类型进行处理
        Field field = null;//Object是已经被赋值的对象实例
        try
        {
            field = gson.getClass().getDeclaredField("factories");
            field.setAccessible(true);
            List<TypeAdapterFactory> factories = (List<TypeAdapterFactory>) field.get(gson);
            Field pro = ReflectUtils.getDeclaredField(factories, "list");
            pro.setAccessible(true);
            List<TypeAdapterFactory> modifyerList = (List<TypeAdapterFactory>) pro.get(factories);
            modifyerList.remove(1);
        }
        catch (NoSuchFieldException e)
        {
            e.printStackTrace();
        }
        catch (IllegalAccessException e)
        {
            e.printStackTrace();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

        return gson;
    }
}
