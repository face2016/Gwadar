package com.face.gwadar;

import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolCodecFactory;
import org.apache.mina.filter.codec.ProtocolDecoder;
import org.apache.mina.filter.codec.ProtocolEncoder;

/**
 * Created by yuanxiaochun on 2016/12/15.
 */
public class RpcSerializationCodecFactory implements ProtocolCodecFactory {

    private final RpcEncoder rpcEncoder;

    private final RpcDecoder rpcDecoder;

    public RpcSerializationCodecFactory() {
        this(Thread.currentThread().getContextClassLoader());
    }

    public RpcSerializationCodecFactory(ClassLoader classLoader) {
        rpcEncoder = new RpcEncoder();
        rpcDecoder = new RpcDecoder(classLoader);
    }

    @Override
    public ProtocolEncoder getEncoder(IoSession session) throws Exception {
        return rpcEncoder;
    }

    @Override
    public ProtocolDecoder getDecoder(IoSession session) throws Exception {
        return rpcDecoder;
    }

    public int getEncoderMaxObjectSize() {
        return rpcEncoder.getMaxObjectSize();
    }

    public void setEncoderMaxObjectSize(int maxObjectSize) {
        rpcEncoder.setMaxObjectSize(maxObjectSize);
    }

    public int getDecoderMaxObjectSize() {
        return rpcDecoder.getMaxObjectSize();
    }

    public void setDecoderMaxObjectSize(int maxObjectSize) {
        rpcDecoder.setMaxObjectSize(maxObjectSize);
    }

}
