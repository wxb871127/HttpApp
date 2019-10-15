package com.httplib;

import com.httplib.config.HttpConfig;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import okhttp3.Request;
import retrofit2.Callback;
import retrofit2.Response;

public final class HttpRequest{
    private Builder builder;

    public interface CallBack<T>{
        void onSubscribe(Disposable d);
        void onSuccess(T object);
        void onFailed(String msg);
    }

    public static void initUrl(String url){
        APIManager.init(url);
    }

    public static void initException(RequestException exception){
        APIManager.initException(exception);
    }

    public HttpRequest(Builder builder){
        this.builder = builder;
    }

    public Observable getObservable(){
        Object object = null;
        try {
            final Method method = findMethod(builder.clas, builder.methodName, builder.params);
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
            final Method method = findMethod(builder.clas, builder.methodName, builder.params);
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
                                if(callBack != null)
                                    callBack.onSubscribe(d);
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

    private Method findMethod(Class clas, String methodName, Object... params){
        Method method = null;
        if(params == null || params.length == 0){
            try {
                return HttpConfig.getProxyClass(clas).getMethod(methodName, null);
            } catch (NoSuchMethodException e) {
                e.printStackTrace();
            }
        }


        Class[] paramsClass = new Class[params.length];
        for(int i=0; i<paramsClass.length; i++){
            Class paramClass = params[i].getClass();
            if(paramClass.isAnonymousClass())
                paramClass = paramClass.getSuperclass();
            paramsClass[i] = paramClass;
        }

        Method[] methods = HttpConfig.getProxyClass(clas).getDeclaredMethods();
        for(Method item : methods){
            if(item.getName().equals(methodName)) {
                Class[] parameterTypes = item.getParameterTypes();
                if(parameterTypes.length == paramsClass.length){
                    for(int i=0; i<parameterTypes.length; i++){
                        if(parameterTypes[i].isAssignableFrom(paramsClass[i]))
                           continue;
                        else break;
                    }
                    return item;
                }
            }
        }
        return method;
    }

    public static Builder request(String methodName) {
        return new Builder(methodName);
    }

    public interface RequestException{
        void handle(Request request, okhttp3.Response response);
    }

    public static final class Builder {
        private final String methodName;
        private Object[] params;
        private Class clas;

        private Builder(String methodName) {
            this.methodName = methodName;
        }

        public Builder parameter(Object... t){
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
