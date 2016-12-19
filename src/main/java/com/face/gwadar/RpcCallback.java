package com.face.gwadar;

/**
 * Created by yuanxiaochun on 2016/12/16.
 */
public interface RpcCallback {

    void onSuccess(Object object);

    void onError(Throwable throwable);

}
