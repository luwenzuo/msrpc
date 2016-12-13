package com.work189.msrpc.core.rpc.proxy.factory.javassist;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

import javassist.ClassPool;
import javassist.CtClass;
import javassist.CtMethod;
import javassist.LoaderClassPath;

import com.work189.msrpc.core.common.log.Logger;
import com.work189.msrpc.core.common.log.LoggerFactory;
import com.work189.msrpc.core.rpc.protocol.serialize.ProtocolHession;


public class JavassistProxyObject {
	private final Logger logger = LoggerFactory.getLogger(ProtocolHession.class);

	@SuppressWarnings({ "unchecked" })
	public <T> T getProxyObject(Class<T> interfaceClass, InvocationHandler hInvoker) {
		try{
			T proxyClass = createProxyJavaClass(interfaceClass);
			if(proxyClass == null){
				throw new RuntimeException("createProxyJavaClass is empty");
			}
			T proxyObject =(T)Proxy.newProxyInstance(this.getClass().getClassLoader(), proxyClass.getClass().getInterfaces(), hInvoker);
			return proxyObject;
		}catch(Throwable e){
			logger.error(e);
			throw new RuntimeException("getProxyObject error");
		}
	}
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	private <T> T createProxyJavaClass(Class<T> interfaceClass) throws Exception{
		
		//String lasterror = null;
		try{
			ClassPool cp = ClassPool.getDefault();
			
			cp.appendClassPath( new LoaderClassPath(this.getClass().getClassLoader()) );
			CtClass ctClass = cp.makeClass(interfaceClass.getName()+"_impl");
			ctClass.addInterface( cp.get( interfaceClass.getName() ));

			//添加方法
			Method[] methods = interfaceClass.getDeclaredMethods();
			for(Method method:methods){
				method.getName();
				method.getParameterTypes();
				
				StringBuilder sb = new StringBuilder();
				sb.append("public ");
				if(method.getReturnType() != null){
					sb.append( method.getReturnType().getCanonicalName() );
				}
				sb.append(" "+ method.getName() );
				sb.append("(");
				
				int nCount = 0;
				for(Class cl:method.getParameterTypes()){
					if(nCount > 0){
						sb.append(", ");
					}
					
					sb.append( cl.getCanonicalName() +" ");
					sb.append( "arg"+nCount );
					nCount ++;
				}
				
				sb.append("){return "+PrimitiveDefault.getDefaultReturnValue(method.getReturnType())+";}");
				//lasterror = sb.toString();
				//System.out.println(sb);
				ctClass.addMethod( CtMethod.make(sb.toString(), ctClass) );
				//lasterror = null;
			}
			//System.out.println( ctClass.toString() );
			//生成类
			Class newClazz = ctClass.toClass( );
			return (T) newClazz.newInstance();
		}catch(Throwable e){
			logger.error(e);
			throw new RuntimeException(e.getMessage());
		}
	}
}
