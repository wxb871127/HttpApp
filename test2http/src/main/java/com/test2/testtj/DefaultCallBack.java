package com.test2.testtj;

import com.httplib.HttpRequest;

import io.reactivex.disposables.Disposable;

public class DefaultCallBack<T> implements HttpRequest.CallBack<BaseResultBean<T>> {
    protected CallBack callBack;

    public DefaultCallBack(CallBack callBack) {
        this.callBack = callBack;
    }

    @Override
    public void onSubscribe(Disposable disposable) {

    }

    @Override
    public void onSuccess(BaseResultBean<T> bean) {
        if (bean == null) {
            callBack.onFailed("数据为空");
            return;
        }
        if (bean.code != 0) {
            callBack.onFailed(bean.message);
            return;
        }
        callBack.onSuccess(bean.data);
    }

    @Override
    public void onFailed(String s) {
        callBack.onFailed(s);
    }

    public interface CallBack<T> {

        void onSuccess(T var1);

        void onFailed(String var1);
    }
}
