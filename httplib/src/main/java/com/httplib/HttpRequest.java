package com.httplib;

import android.os.Handler;
import android.os.Message;
import com.httplib.config.HttpConfig;

import org.apache.http.params.HttpParams;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import okhttp3.ResponseBody;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.adapter.rxjava2.HttpException;

public final class HttpRequest{
    private Builder builder;

    public interface CallBack<T>{
        void onSubscribe(Disposable d);
        void onSuccess(T object);
        void onFailed(String msg);
    }

    public interface CallStatus{
        void onDownload(int progress);
        void onSucess(String filePath);
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



    private void downloadFile(final CallStatus callStatus, final String file){
        try {
            final Method method = findMethod(builder.clas, builder.methodName, builder.params);
            if (method == null)
                throw new IllegalArgumentException("can't find method " + builder.methodName + " in class " + builder.clas.getName());
            Object object = method.invoke(builder.clas, builder.params);
            ((Observable)object).subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Observer<ResponseBody>() {
                        @Override
                        public void onSubscribe(Disposable d) {

                        }

                        @Override
                        public void onNext(final ResponseBody response) {
                           final Handler handler = new Handler(){
                                @Override
                                public void handleMessage(Message msg) {
                                    super.handleMessage(msg);
                                    switch (msg.what){
                                        case 1:
                                            if(callStatus != null)
                                                callStatus.onDownload(msg.arg1);
                                            break;
                                        case 2:
                                            if(callStatus != null)
                                                callStatus.onSucess(file);
                                            break;
                                    }
                                }
                            };

                            new Thread(){
                                @Override
                                public void run() {
                                    try {
                                        long total = response.contentLength();//需要下载的总大小
                                        long current = 0;
                                        InputStream inputStream = response.byteStream();
                                        FileOutputStream fileOutputStream = new FileOutputStream(file);
                                        byte[] bytes = new byte[1024];
                                        int len = 0;
                                        while ((len = inputStream.read(bytes)) > 0) {
                                            fileOutputStream.write(bytes, 0, len);
                                            fileOutputStream.flush();
                                            current = current + len;
                                            Message message = Message.obtain();
                                            message.what = 1;
                                            message.arg1 = (int) (current*100/total);
                                            handler.sendMessage(message);
                                        }
                                        fileOutputStream.flush();
                                        fileOutputStream.close();
                                        inputStream.close();
                                    } catch (FileNotFoundException e) {
                                        e.printStackTrace();
                                    } catch (IOException e) {
                                        e.printStackTrace();
                                    }
                                    Message message = Message.obtain();
                                    message.what = 2;
                                    message.obj = file;
                                    handler.sendMessage(message);
                                }
                            }.start();
                        }

                        @Override
                        public void onError(Throwable e) {
                            if(callStatus != null)
                                callStatus.onFailed(e.toString());
                        }

                        @Override
                        public void onComplete() {

                        }
                    });
        }catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
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
                                    callBack.onFailed("网络异常:"+((HttpException)e).code());
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

    public static void downloadFile(String urlPath, String filePath, final CallStatus callBack){
        final File file = new File(filePath);
        if (file.exists()) {
            file.delete();
        }
        request("download").parameter(urlPath).from(RequestAPI.class)
                .create().downloadFile(new CallStatus() {

            @Override
            public void onDownload(int progress) {
                if(callBack != null)
                    callBack.onDownload(progress);
            }

            @Override
            public void onSucess(String filePath) {
                if(callBack != null)
                    callBack.onSucess(filePath);
            }

            @Override
            public void onFailed(String msg) {
                if(callBack != null)
                    callBack.onFailed(msg);
            }
        }, filePath);
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
        void handle(okhttp3.Response response);
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
