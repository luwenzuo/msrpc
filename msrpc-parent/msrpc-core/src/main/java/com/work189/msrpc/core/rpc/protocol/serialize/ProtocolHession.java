package com.work189.msrpc.core.rpc.protocol.serialize;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

import com.caucho.hessian.io.HessianInput;
import com.caucho.hessian.io.HessianOutput;
import com.work189.msrpc.core.common.log.Logger;
import com.work189.msrpc.core.common.log.LoggerFactory;
import com.work189.msrpc.core.rpc.exchange.buffer.rpcbuffer.RpcBuffer;

public class ProtocolHession implements ProtocolSerialize{

	private final static Logger logger = LoggerFactory.getLogger(ProtocolHession.class);

	@Override
	public byte[] serialize(Object object) {
		return serializeByte(object);
	}
	@Override
	public Object deserialize(byte[] data) {
		// TODO Auto-generated method stub
		return deserializeByte(data);
	}

	public static byte[] serializeByte(Object message){
		try {
			if (message == null)
				throw new NullPointerException();
			ByteArrayOutputStream os = new ByteArrayOutputStream();
			HessianOutputUnserialize ho = new HessianOutputUnserialize(os);
			ho.writeObject(message);
			return os.toByteArray();
		}catch(Throwable e){
			logger.error(e);
			throw new RuntimeException("serializeMessage error");
		}
	}
	public static Object deserializeByte(byte[] message){
		try {
			if (message == null)
				throw new NullPointerException();
			ByteArrayInputStream is = new ByteArrayInputStream(message);
			HessianInputUnserialize hi = new HessianInputUnserialize(is);
			// System.out.println("deserializeMessage-length="+b.length+";"+ioBuffer.limit()+";"+ioBuffer.array().length+";"+ioBuffer.remaining()+";"+ioBuffer.position());
			return hi.readObject();
		}catch(Throwable e){
			logger.error(e);
			throw new RuntimeException("serializeMessage error");
		}
	}
	
	public static RpcBuffer serializeMessage(Object message){
		try {
			if (message == null)
				throw new NullPointerException();
			ByteArrayOutputStream os = new ByteArrayOutputStream();
			HessianOutputUnserialize ho = new HessianOutputUnserialize(os);
			ho.writeObject(message);
			RpcBuffer ioBuffer = RpcBuffer.wrap(os.toByteArray());
			// System.out.println("serializeMessage-length="+ioBuffer.array().length+";"+ioBuffer.limit());
			return ioBuffer;
		}catch(Throwable e){
			logger.error(e);
			throw new RuntimeException("serializeMessage error");
		}
	}

	public static Object deserializeMessage(Object message){
		try {
			if (message == null)
				throw new NullPointerException();

			RpcBuffer ioBuffer = (RpcBuffer) message;
			ByteArrayInputStream is = new ByteArrayInputStream(ioBuffer.array());
			HessianInputUnserialize hi = new HessianInputUnserialize(is);
			// System.out.println("deserializeMessage-length="+b.length+";"+ioBuffer.limit()+";"+ioBuffer.array().length+";"+ioBuffer.remaining()+";"+ioBuffer.position());
			return hi.readObject();
		}catch(Throwable e){
			logger.error(e);
			throw new RuntimeException("serializeMessage error");
		}
	}

	public static class HessianOutputUnserialize extends HessianOutput{

		public HessianOutputUnserialize(ByteArrayOutputStream os) {
			super(os);
		}

		public void writeObject(Object object) throws IOException {
			_serializerFactory.setAllowNonSerializable(true);
			super.writeObject(object);
		}
		
	}

	public static class HessianInputUnserialize extends HessianInput{

		public HessianInputUnserialize(ByteArrayInputStream is){
			super(is);
		}
		
		public Object readObject() throws IOException{
			_serializerFactory.setAllowNonSerializable(true);
			return super.readObject();
		}

		
	}
}
