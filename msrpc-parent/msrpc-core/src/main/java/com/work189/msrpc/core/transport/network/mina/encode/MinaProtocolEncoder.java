package com.work189.msrpc.core.transport.network.mina.encode;

import org.apache.mina.core.buffer.IoBuffer;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolEncoderAdapter;
import org.apache.mina.filter.codec.ProtocolEncoderOutput;

import com.work189.msrpc.core.transport.channel.swap.ChannelCodec;

public class MinaProtocolEncoder extends ProtocolEncoderAdapter {

	@Override
	public void encode(IoSession session, Object message,
			ProtocolEncoderOutput out) throws Exception {
		try {
			byte[] buf = (byte[]) message;
			buf = ChannelCodec.encode(buf);
			
			out.write(  IoBuffer.wrap(buf) );
		} catch (Throwable e) {
			e.printStackTrace();
			session.close(true);
			throw e;
		}
	}
}
