package com.httplib;

import android.util.Log;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.LinkedHashMap;
import java.util.Map;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import okhttp3.ResponseBody;
import retrofit2.Callback;
import retrofit2.Response;

public final class HttpRequest{
    static final Map<Class, Class> BINDINGS = new LinkedHashMap<>();
    private OnRequestListener listener;

    public interface OnRequestListener<T>{
        void onSuccess(T object);
        void onFailed(String msg);
    }

    public void setListener(OnRequestListener listener){
        this.listener = listener;
    }

    private HttpRequest(Builder builder) {
        try {
            Class paramClass = builder.params.getClass();
            if(paramClass.isAnonymousClass())
                paramClass = builder.params.getClass().getSuperclass();
            try {
//                BINDINGS.put(builder.clas, Class.forName("com.http.test2http.RequestProxy"));
                BINDINGS.put(builder.clas, Class.forName("com.http.testhttp.RequestProxy"));
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
            final Method method = getProxyClass(builder.clas).getDeclaredMethod(builder.methodName, paramClass);
            if(method == null)
                throw new IllegalArgumentException("can't find method" + builder.methodName + " in class " + builder.clas.getName());
            Object object = method.invoke(builder.clas, builder.params);
            final Class returnType = method.getReturnType();
            if(returnType.equals(retrofit2.Call.class)){
                ((retrofit2.Call)object).enqueue(new Callback<Object>() {
                    @Override
                    public void onResponse(retrofit2.Call<Object> call, Response<Object> response) {
                        //数据请求成功
                        Log.e("xxxxxx", response.toString());
                        if(listener != null){
                            listener.onSuccess(response.body());
                        }
                    }

                    @Override
                    public void onFailure(retrofit2.Call<Object> call, Throwable t) {
                        //数据请求失败
                        Log.e("yyyyyyyyyy", t.toString());
                    }
                });
            }else if(returnType.equals(io.reactivex.Observable.class)){
                ((Observable)object).subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new Observer<Object>() {
                            @Override
                            public void onSubscribe(Disposable d) {

                            }

                            @Override
                            public void onNext(Object object) {
                                Log.e("xxxxx", object.toString());
                                if(listener != null)
                                    listener.onSuccess(object);
                            }

                            @Override
                            public void onError(Throwable e) {
                                Log.e("yyyy", e.toString());
                                if(listener != null)
                                    listener.onFailed(e.toString());
                            }

                            @Override
                            public void onComplete() {

                            }
                        });
            }
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
    }

    private Class getProxyClass(Class clas){

        return BINDINGS.get(clas);
    }

    public static Builder request(String methodName) {
        return new Builder(methodName);
    }

    public void autoRegister(Class clas){
        try {
           String from = (String)clas.getField("from").get(clas.newInstance());
            this.BINDINGS.put(Class.forName(from), clas);
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

    public static final class Builder<T> {
        private final String methodName;
        private T params;
        private Class clas;

        private Builder(String methodName) {
            this.methodName = methodName;
        }

        public Builder parameter(T t){
            this.params = t;
            return this;
        }

        public Builder from(Class clas){
            this.clas = clas;
            return this;
        }

        public HttpRequest build() {
            return new HttpRequest(this);
        }
    }
}
