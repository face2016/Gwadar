package com.face.gwadar;

import java.io.Serializable;

/**
 * Created by yuanxiaochun on 2016/12/14.
 */
public class RpcResponse implements Serializable {

    private String requestId;

    private Throwable error;

    private Object result;

    public String getRequestId() {
        return requestId;
    }

    public void setRequestId(String requestId) {
        this.requestId = requestId;
    }

    public Throwable getError() {
        return error;
    }

    public void setError(Throwable error) {
        this.error = error;
    }

    public Object getResult() {
        return result;
    }

    public void setResult(Object result) {
        this.result = result;
    }
}
