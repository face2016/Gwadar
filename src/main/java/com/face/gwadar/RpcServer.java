package com.face.gwadar;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.mina.core.filterchain.DefaultIoFilterChainBuilder;
import org.apache.mina.core.service.IoAcceptor;
import org.apache.mina.core.session.IdleStatus;
import org.apache.mina.filter.codec.ProtocolCodecFilter;
import org.apache.mina.transport.socket.nio.NioSocketAcceptor;

import java.net.InetSocketAddress;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by yuanxiaochun on 2016/12/14.
 */
public class RpcServer {

    private static Logger LOG = LogManager.getLogger(RpcServer.class);

    private String address;

    private int port;

    private Map<Class<?>, Object> serviceMap = new HashMap<>();

    public RpcServer(String address, int port) {
        this.address = address;
        this.port = port;
    }

    public void addService(Class<?> clazz, Object bean) {
        serviceMap.put(clazz, bean);
    }

    public void start() {
        NioSocketAcceptor acceptor = new NioSocketAcceptor();

        acceptor.getFilterChain().addLast("codec", new ProtocolCodecFilter(new RpcSerializationCodecFactory()));

        acceptor.setHandler(new RpcServerHandler(serviceMap));

        acceptor.getSessionConfig().setReadBufferSize(2048);
        acceptor.getSessionConfig().setIdleTime(IdleStatus.BOTH_IDLE, 60);        // 60s

        try {
            acceptor.bind(new InetSocketAddress(port));
            LOG.info("rpc server started on port {}", port);
        } catch (Throwable e) {
            LOG.error("failed to start rpc server on port {}, {}", port, e.getMessage());
        }
    }

}
