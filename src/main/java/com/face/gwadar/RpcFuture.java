package com.face.gwadar;


import java.util.concurrent.*;

/**
 * Created by yuanxiaochun on 2016/12/16.
 */
public class RpcFuture implements Future<Object> {

    private CountDownLatch countDownLatch;

    private RpcRequest request;
    private RpcResponse response;

    public RpcFuture(RpcRequest request) {
        countDownLatch = new CountDownLatch(1);
        this.request = request;
    }

    @Override
    public boolean cancel(boolean mayInterruptIfRunning) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean isCancelled() {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean isDone() {
        return this.response != null;
    }

    @Override
    public Object get() throws InterruptedException, ExecutionException {
        countDownLatch.await();
        return response.getResult();
    }

    @Override
    public Object get(long timeout, TimeUnit unit) throws InterruptedException, ExecutionException, TimeoutException {
        return null;
    }

    public void setResponse(RpcResponse response) {
        this.response = response;
        countDownLatch.countDown();
    }

}
