package com.face.gwadar;

import java.io.Serializable;
import java.util.Arrays;

/**
 * Created by yuanxiaochun on 2016/12/14.
 */
public class RpcRequest implements Serializable {

    private String requestId;

    private Class<?> clazz;

    private String methodName;

    private Class<?>[] parameterTypes;

    private Object[] parameterValues;

    private long timeout;       // TODO

    public String getRequestId() {
        return requestId;
    }

    public void setRequestId(String requestId) {
        this.requestId = requestId;
    }

    public Class<?> getClazz() {
        return clazz;
    }

    public void setClazz(Class<?> clazz) {
        this.clazz = clazz;
    }

    public String getMethodName() {
        return methodName;
    }

    public void setMethodName(String methodName) {
        this.methodName = methodName;
    }

    public Class<?>[] getParameterTypes() {
        return parameterTypes;
    }

    public void setParameterTypes(Class<?>[] parameterTypes) {
        this.parameterTypes = parameterTypes;
    }

    public Object[] getParameterValues() {
        return parameterValues;
    }

    public void setParameterValues(Object[] parameterValues) {
        this.parameterValues = parameterValues;
    }

    public long getTimeout() {
        return timeout;
    }

    public void setTimeout(long timeout) {
        this.timeout = timeout;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        return builder.append("requestId=").append(requestId).append(",")
                .append("class=").append(clazz).append(",")
                .append("methodName=").append(methodName).append(",")
                .append("paramType=").append(Arrays.toString(parameterTypes)).append(",")
                .append("paramValue=").append(Arrays.toString(parameterValues))
                .toString();
    }
}
