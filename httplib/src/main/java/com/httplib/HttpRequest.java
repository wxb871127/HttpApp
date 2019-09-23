package com.httplib;

import com.httplib.config.HttpConfig;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Callback;
import retrofit2.Response;

public final class HttpRequest{
    private Builder builder;

    public interface CallBack<T>{
        void onSuccess(T object);
        void onFailed(String msg);
    }

    public HttpRequest(Builder builder){
        this.builder = builder;
    }

    public Observable getObservable(){
        Object object = null;
        try {
            final Method method = findMethod(builder.clas, builder.methodName, builder.params.getClass());
            if (method == null)
                throw new IllegalArgumentException("can't find method " + builder.methodName + " in class " + builder.clas.getName());
            object = method.invoke(builder.clas, builder.params);
            final Class returnType = method.getReturnType();
            if(returnType.equals(io.reactivex.Observable.class))
                return (Observable) object;
        }catch (InvocationTargetException e){
            e.printStackTrace();
        }catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void execute(final CallBack callBack){
        try {
            final Method method = findMethod(builder.clas, builder.methodName, builder.params.getClass());
            if(method == null)
                throw new IllegalArgumentException("can't find method " + builder.methodName + " in class " + builder.clas.getName());
            Object object = method.invoke(builder.clas, builder.params);
            final Class returnType = method.getReturnType();
            if(returnType.equals(retrofit2.Call.class)){
                ((retrofit2.Call)object).enqueue(new Callback<Object>() {
                    @Override
                    public void onResponse(retrofit2.Call<Object> call, Response<Object> response) {
                        //数据请求成功
                        if(callBack != null){
                            callBack.onSuccess(response.body());
                        }
                    }

                    @Override
                    public void onFailure(retrofit2.Call<Object> call, Throwable t) {
                        //数据请求失败
                        if(callBack != null)
                            callBack.onFailed(t.toString());
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
                                if(callBack != null)
                                    callBack.onSuccess(object);
                            }

                            @Override
                            public void onError(Throwable e) {
                                if(callBack != null)
                                    callBack.onFailed(e.toString());
                            }

                            @Override
                            public void onComplete() {

                            }
                        });
            }
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
    }

    private Method findMethod(Class clas, String methodName, Class paramClass){
        Method method = null;
        if(paramClass.isAnonymousClass())
            paramClass = paramClass.getSuperclass();

        Method[] methods = HttpConfig.getProxyClass(clas).getDeclaredMethods();
        for(Method item : methods){
            if(item.getName().equals(methodName)) {
                Class[] parameterTypes = item.getParameterTypes();
                for(Class parameterType : parameterTypes){
                    if(parameterType.isAssignableFrom(paramClass)){
                        return item;
                    }
                }
            }
        }
        return method;
    }

    public static Builder request(String methodName) {
        return new Builder(methodName);
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

        public HttpRequest create() {
            return new HttpRequest(this);
        }
    }
}
