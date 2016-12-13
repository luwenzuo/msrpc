package com.work189.msrpc.core.transport.network.mina.encode;

import org.apache.mina.core.buffer.IoBuffer;
import org.apache.mina.core.session.AttributeKey;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolDecoderAdapter;
import org.apache.mina.filter.codec.ProtocolDecoderOutput;

import com.work189.msrpc.core.transport.channel.swap.ChannelBuffer;

public class MinaProtocolDecoder extends ProtocolDecoderAdapter{
	private final static AttributeKey BUFFER_DECODER = new AttributeKey(MinaProtocolDecoder.class, "__BUFFER_DECODER");

	@Override
	public void decode(IoSession session, IoBuffer in, ProtocolDecoderOutput out)
			throws Exception {

		try {
			//System.out.println(in.remaining()+";"+in.limit()+";"+in.array().length);
			DecoderBuffer decoderBuffer = getDecoderBuffer(session);
			decoderBuffer.put(in);
			while(true){
				byte []msg = decoderBuffer.get();
				if(msg == null){
					break;
				}else{
					IoBuffer io = IoBuffer.wrap(msg);
					out.write( io );
				}
			}
		} catch (Throwable e) {
			e.printStackTrace();
			clearDecoderBuffer(session);
			throw e;
		}
	}
	
	private void clearDecoderBuffer(IoSession session) {
		session.close(true);
		session.setAttribute(BUFFER_DECODER, null);
	}

	private DecoderBuffer getDecoderBuffer(IoSession session) {
		Object object = session.getAttribute(BUFFER_DECODER);
		if (object == null) {
			DecoderBuffer buf = new DecoderBuffer();
			session.setAttribute(BUFFER_DECODER, buf);
			return buf;
		}else{
			return (DecoderBuffer)object;
		}
	}

	private class DecoderBuffer {
		private ChannelBuffer channelBuffer = new ChannelBuffer();;
		
		public DecoderBuffer(){
		}
		
		public byte[] get(){
			return this.channelBuffer.get();
		}

		public void put(IoBuffer in) {
			if(in.remaining() < 1){
				return ;
			}
			
			byte []data = new byte[in.remaining()];
			in.get(data);
			this.channelBuffer.put(data);
		}
	}
}
