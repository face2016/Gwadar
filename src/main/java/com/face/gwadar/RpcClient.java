package com.face.gwadar;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.mina.core.future.ConnectFuture;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolCodecFilter;
import org.apache.mina.transport.socket.nio.NioSocketConnector;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.net.InetSocketAddress;
import java.util.UUID;

/**
 * Created by yuanxiaochun on 2016/12/14.
 */
public class RpcClient implements InvocationHandler {

    private static Logger LOG = LogManager.getLogger(RpcClient.class);

    private NioSocketConnector connector;
    private IoSession session;

    public void connect(String address, int port) {
        this.session = getSession(address, port);
    }

    private IoSession getSession(String address, int port) {
        if (session != null) {
            if (session.isConnected() && session.isClosing()) {
                return session;
            }
        }
        connector = new NioSocketConnector();
        connector.setConnectTimeoutMillis(3000);

        connector.getFilterChain().addLast("codec", new ProtocolCodecFilter(new RpcSerializationCodecFactory()));
        connector.setHandler(new RpcClientHandler());

        ConnectFuture connectFuture = connector.connect(new InetSocketAddress(address, port));
        connectFuture.awaitUninterruptibly();
        LOG.debug("connected to {}, port {}", () -> address, () -> port);
        return connectFuture.getSession();
    }

    @SuppressWarnings("unchecked")
    public <T> T getService(Class<?> iCLazz) {
        T t = (T) Proxy.newProxyInstance(iCLazz.getClassLoader(), new Class<?>[]{iCLazz}, this);
        return t;
    }

    public void close() {
        connector.dispose();
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        if (method.getName().equals("toString")) {
            return null;
        }
        RpcRequest request = new RpcRequest();
        request.setRequestId(UUID.randomUUID().toString());
        request.setClazz(method.getDeclaringClass());
        request.setMethodName(method.getName());
        request.setParameterTypes(method.getParameterTypes());
        request.setParameterValues(args);

        RpcFuture future = new RpcFuture(request);
        RpcClientHandler handler = (RpcClientHandler) session.getHandler();
        handler.getFutureMap().put(request.getRequestId(), future);

        session.write(request);     // TODO get session abstraction
        return future.get();
    }
}
