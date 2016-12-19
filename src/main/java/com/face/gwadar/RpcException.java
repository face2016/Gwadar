package com.face.gwadar;

/**
 * Created by yuanxiaochun on 2016/12/15.
 */
public class RpcException extends RuntimeException {

    public RpcException(String message) {
        super(message);
    }

    public RpcException(String message, Throwable t) {
        super(message, t);
    }

    public RpcException(Throwable t) {
        super(t);
    }

}
