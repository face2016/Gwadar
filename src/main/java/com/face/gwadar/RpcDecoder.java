package com.face.gwadar;

import org.apache.mina.core.buffer.IoBuffer;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolDecoderOutput;
import org.apache.mina.filter.codec.serialization.ObjectSerializationDecoder;

/**
 * Created by yuanxiaochun on 2016/12/14.
 */
public class RpcDecoder extends ObjectSerializationDecoder {

    public RpcDecoder() {
        super();
    }

    public RpcDecoder(ClassLoader classLoader) {
        super(classLoader);
    }

    @Override
    public void decode(IoSession session, IoBuffer in, ProtocolDecoderOutput out) throws Exception {
        super.decode(session, in, out);
    }

}
