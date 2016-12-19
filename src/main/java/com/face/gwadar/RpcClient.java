package com.face.gwadar;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.mina.core.filterchain.DefaultIoFilterChainBuilder;
import org.apache.mina.core.future.ConnectFuture;
import org.apache.mina.core.service.IoConnector;
import org.apache.mina.core.service.IoHandlerAdapter;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolCodecFilter;
import org.apache.mina.filter.codec.serialization.ObjectSerializationCodecFactory;
import org.apache.mina.transport.socket.nio.NioSocketConnector;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.net.InetSocketAddress;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by yuanxiaochun on 2016/12/14.
 */
public class RpcClient implements InvocationHandler {

    private static Logger LOG = LogManager.getLogger(RpcClient.class);

    private ConcurrentHashMap<String, RpcFuture> futureMap = new ConcurrentHashMap<>();

    private IoSession session;

    public void connect(String address, int port) {
        NioSocketConnector connector = new NioSocketConnector();
        connector.setConnectTimeoutMillis(3000);

        connector.getFilterChain().addLast("codec", new ProtocolCodecFilter(new RpcSerializationCodecFactory()));

        connector.setHandler(new IoHandlerAdapter() {
            @Override
            public void exceptionCaught(IoSession session, Throwable cause) throws Exception {
                LOG.error(cause);
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
                LOG.debug("send message {}", message);
                super.messageSent(session, message);
            }
        });

        ConnectFuture connectFuture = connector.connect(new InetSocketAddress(port));
        connectFuture.awaitUninterruptibly();
        this.session = connectFuture.getSession();
    }

    @SuppressWarnings("unchecked")
    public <T> T getService(Class<?> iCLazz) {
        T t = (T) Proxy.newProxyInstance(iCLazz.getClassLoader(), new Class<?>[]{iCLazz}, this);
        return t;
    }

    public void close() {
        session.closeNow();
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        if (method.getName().equals("getName")) {
            LOG.info("before method: {}", method.getName());
            RpcRequest request = new RpcRequest();
            request.setRequestId(UUID.randomUUID().toString());
            request.setClazz(method.getDeclaringClass());
            request.setMethodName(method.getName());
            request.setParameterTypes(method.getParameterTypes());
            request.setParameterValues(args);

            RpcFuture future = new RpcFuture(request);
            futureMap.put(request.getRequestId(), future);

            session.write(request);
            return future.get();
        } else {
            LOG.info(method.getName());
            return null;
        }
    }
}
