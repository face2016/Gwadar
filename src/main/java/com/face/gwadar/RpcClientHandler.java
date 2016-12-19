package com.face.gwadar;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.mina.core.service.IoHandlerAdapter;
import org.apache.mina.core.session.IdleStatus;
import org.apache.mina.core.session.IoSession;

import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by yuanxiaochun on 2016/12/15.
 */
public class RpcClientHandler extends IoHandlerAdapter {

    private static Logger LOG = LogManager.getLogger(RpcClientHandler.class);

    private ConcurrentHashMap<String, RpcFuture> futureMap = new ConcurrentHashMap<>();

    @Override
    public void sessionCreated(IoSession session) throws Exception {
        LOG.debug("sessionCreated");
    }

    @Override
    public void sessionOpened(IoSession session) throws Exception {
        LOG.debug("sessionOpened");
    }

    @Override
    public void sessionClosed(IoSession session) throws Exception {
        LOG.debug("sessionClosed");
    }

    @Override
    public void sessionIdle(IoSession session, IdleStatus status) throws Exception {
        LOG.debug("sessionIdle");
    }

    @Override
    public void exceptionCaught(IoSession session, Throwable cause) throws Exception {
        throw new RpcException(cause);
    }

    @Override
    public void messageReceived(IoSession session, Object message) throws Exception {
        LOG.debug("messageReceived");
        RpcResponse response = (RpcResponse) message;
        String requestId = response.getRequestId();
        RpcFuture future = futureMap.get(requestId);
        if (future != null) {
            futureMap.remove(requestId);
            future.setResponse(response);
        }
    }

    @Override
    public void messageSent(IoSession session, Object message) throws Exception {
        LOG.debug("messageSent");
    }

    public ConcurrentHashMap<String, RpcFuture> getFutureMap() {
        return futureMap;
    }
}
