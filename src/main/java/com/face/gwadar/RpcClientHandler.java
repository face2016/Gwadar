package com.face.gwadar;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.mina.core.service.IoHandlerAdapter;
import org.apache.mina.core.session.IoSession;

import java.net.InetSocketAddress;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by yuanxiaochun on 2016/12/15.
 */
public class RpcClientHandler extends IoHandlerAdapter {

    private static Logger LOG = LogManager.getLogger(RpcClientHandler.class);

    private ConcurrentHashMap<String, RpcFuture> futureMap = new ConcurrentHashMap<>();

    @Override
    public void exceptionCaught(IoSession session, Throwable cause) throws Exception {
        session.closeNow();
        throw new RpcException(cause);
    }

    @Override
    public void messageReceived(IoSession session, Object message) throws Exception {
        if (message instanceof RpcResponse) {
            RpcResponse response = (RpcResponse) message;
            LOG.debug("received response from server, address={}, response={}", () ->
                    ((InetSocketAddress)session.getServiceAddress()).getHostName(), response::toString);
            String requestId = response.getRequestId();
            RpcFuture future = futureMap.get(requestId);
            if (future == null) {
                throw new RpcException("could not find RpcFuture in futuremap by requestId " + requestId);
            }
            futureMap.remove(requestId);
            future.setResponse(response);
        } else {
            throw new RpcException("response message is not an instance of RpcResponse");
        }
    }

    @Override
    public void messageSent(IoSession session, Object message) throws Exception {
        if (message instanceof RpcRequest) {
            RpcRequest request = (RpcRequest) message;
            LOG.debug("send message to server, address={}, request={}", () ->
                    ((InetSocketAddress)session.getServiceAddress()).getHostName(), request::toString);
        }
    }

    public ConcurrentHashMap<String, RpcFuture> getFutureMap() {
        return futureMap;
    }
}
