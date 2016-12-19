package com.face.gwadar;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.mina.core.future.IoFuture;
import org.apache.mina.core.future.IoFutureListener;
import org.apache.mina.core.future.WriteFuture;
import org.apache.mina.core.service.IoHandlerAdapter;
import org.apache.mina.core.session.IoSession;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Map;

/**
 * Created by yuanxiaochun on 2016/12/15.
 */
public class RpcServerHandler extends IoHandlerAdapter {

    private static Logger LOG = LogManager.getLogger(RpcServerHandler.class);

    private Map<Class<?>, Object> serviceMap;

    public RpcServerHandler(Map<Class<?>, Object> serviceMap) {
        this.serviceMap = serviceMap;
    }

    @Override
    public void exceptionCaught(IoSession session, Throwable cause) throws Exception {
        LOG.error("server caught exception", cause);
        session.closeNow();
    }

    @Override
    public void messageReceived(IoSession session, Object message) throws Exception {
        RpcRequest request = (RpcRequest) message;
        RpcResponse response = new RpcResponse();
        response.setRequestId(request.getRequestId());
        try {
            Object result = handle(request);
            response.setResult(result);
        } catch (Throwable t) {
            response.setError(t);
        }
        WriteFuture writeFuture = session.write(response);
        writeFuture.addListener(new IoFutureListener<IoFuture>() {
            @Override
            public void operationComplete(IoFuture future) {
                LOG.info("write complete");
            }
        });
        super.messageReceived(session, message);
    }

    private Object handle(RpcRequest request) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        Class<?> clazz = request.getClazz();
        String methodName = request.getMethodName();
        Class<?>[] parameterTypes = request.getParameterTypes();
        Object[] parameterValues = request.getParameterValues();
        Object bean = serviceMap.get(clazz);

        Method method = clazz.getDeclaredMethod(methodName, parameterTypes);
        return method.invoke(bean, parameterValues);
    }

    @Override
    public void messageSent(IoSession session, Object message) throws Exception {
        LOG.debug(() -> "message sent");
    }

}
