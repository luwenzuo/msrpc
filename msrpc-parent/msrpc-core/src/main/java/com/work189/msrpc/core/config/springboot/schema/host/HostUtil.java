package com.work189.msrpc.core.config.springboot.schema.host;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.NetworkInterface;
import java.net.Socket;
import java.net.SocketAddress;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.Properties;

public class HostUtil {
	
	public static Properties readProperties(String filename) {
		FileInputStream fis = null;
		try {
			fis = new FileInputStream(new File(filename));
			Properties properties = new Properties();
			properties.load(fis);
			return properties;
		} catch (Throwable e) {
			throw new RuntimeException(e.getMessage());
		} finally {
			if (fis != null) {
				try {
					fis.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}
	
	public static void saveProperties(Properties properties, String filename) {

		FileOutputStream fos = null;
		try {
			fos = new FileOutputStream( new File(filename) );
			properties.store(fos, "");
		} catch (Throwable e) {
			throw new RuntimeException( e.getMessage() );
		} finally {
			if (fos != null) {
				try {
					fos.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}
	

	@SuppressWarnings("rawtypes")
	public static String[] getAllLocalHostIP() {
		List<String> res = new ArrayList<String>();
		Enumeration netInterfaces;
		try {
			netInterfaces = NetworkInterface.getNetworkInterfaces();
			InetAddress ip = null;
			while (netInterfaces.hasMoreElements()) {
				NetworkInterface ni = (NetworkInterface) netInterfaces
						.nextElement();
				//System.out.println("---Name---:" + ni.getName());
				Enumeration nii = ni.getInetAddresses();
				while (nii.hasMoreElements()) {
					ip = (InetAddress) nii.nextElement();
					if (ip.getHostAddress().indexOf(":") == -1) {
						res.add(ip.getHostAddress());
						//System.out.println("本机的ip=" + ip.getHostAddress());
					}
				}
			}
		} catch (SocketException e) {
			e.printStackTrace();
		}
		return (String[]) res.toArray(new String[0]);
	}

	public static boolean isPortUsing(String host, int port){

		Socket socket = null;
		try{
			socket = new Socket();
			SocketAddress localAddr = new InetSocketAddress(host,port);
			socket.bind(localAddr);
			return true;
		}catch(Throwable e){
			//e.printStackTrace();
			//System.out.println(port);
		}finally{
			if(socket != null){
				try {
					socket.close();
				} catch (IOException e1) {
					e1.printStackTrace();
				}
			}
		}
		return false;
	}
	
	public static String getDefaultHostIp(){
		String []ips = getAllLocalHostIP();
		
		for(String ip:ips){
			if(ip.startsWith("192.")){
				return ip;
			}
		}
		for(String ip:ips){
			if(ip.startsWith("10.")){
				return ip;
			}
		}
		for(String ip:ips){
			if(!ip.startsWith("127.")){
				return ip;
			}
		}
		return ips[0];
	}
	
	public static int getDefaultHostPort(){
		for(int port=22901; port<30000; port++){

			Socket socket = null;
			try{
				socket = new Socket();
				SocketAddress localAddr = new InetSocketAddress("localhost",port);
				socket.bind(localAddr);
				return port;
			}catch(Throwable e){
				e.printStackTrace();
			}finally{
				if(socket != null){
					try {
						socket.close();
					} catch (IOException e1) {
						e1.printStackTrace();
					}
				}
			}
		}
		return -1;
	}

	public static void main(String[] args) throws Throwable {
	}
}
