package com.face.gwadar;

import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolEncoderOutput;
import org.apache.mina.filter.codec.serialization.ObjectSerializationEncoder;

/**
 * Created by yuanxiaochun on 2016/12/14.
 */
public class RpcEncoder extends ObjectSerializationEncoder {

    @Override
    public void encode(IoSession session, Object message, ProtocolEncoderOutput out) throws Exception {
        if (RpcRequest.class.isInstance(message)) {   // message是否实现了RpcRequest
            super.encode(session, message, out);
        }
    }

}
