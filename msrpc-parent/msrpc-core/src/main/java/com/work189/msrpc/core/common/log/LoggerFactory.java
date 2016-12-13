package com.work189.msrpc.core.common.log;

import java.util.Hashtable;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

@SuppressWarnings("rawtypes")
public class LoggerFactory {

	protected static final Hashtable<String, Logger> instances = new Hashtable<>();
	
	public static Logger getLogger(Class clazz){
		Logger logger = instances.get(clazz.getName());
		if(logger == null){
			logger = new LoggerSupport( LogFactory.getLog(clazz) );
			instances.put(clazz.getName(), logger);
		}
		return logger;
	}
	
	private static class LoggerSupport implements Logger{

		private Log log;

		public LoggerSupport(Log log){
			this.log = log;
		}

		@Override
		public void debug(Object arg0) {
			this.log.debug(arg0);
		}

		@Override
		public void debug(Object arg0, Throwable arg1) {
			this.log.debug(arg0, arg1);
		}

		@Override
		public void error(Object arg0) {
			this.log.error(arg0);
		}

		@Override
		public void error(Object arg0, Throwable throwable) {
			this.log.error(arg0, throwable);
			throwable.printStackTrace();
		}

		@Override
		public void fatal(Object arg0) {
			this.log.fatal(arg0);
		}

		@Override
		public void fatal(Object arg0, Throwable arg1) {
			this.log.fatal(arg0, arg1);
		}

		@Override
		public void info(Object arg0) {
			this.log.info(arg0);
		}

		@Override
		public void info(Object arg0, Throwable arg1) {
			this.log.info(arg0, arg1);
		}

		@Override
		public boolean isDebugEnabled() {
			return this.log.isDebugEnabled();
		}

		@Override
		public boolean isErrorEnabled() {
			return this.log.isErrorEnabled();
		}

		@Override
		public boolean isFatalEnabled() {
			return this.log.isFatalEnabled();
		}

		@Override
		public boolean isInfoEnabled() {
			return this.log.isInfoEnabled();
		}

		@Override
		public boolean isTraceEnabled() {
			return this.log.isTraceEnabled();
		}

		@Override
		public boolean isWarnEnabled() {
			return this.log.isWarnEnabled();
		}

		@Override
		public void trace(Object arg0) {
			this.log.trace(arg0);
		}

		@Override
		public void trace(Object arg0, Throwable arg1) {
			this.log.trace(arg0, arg1);
		}

		@Override
		public void warn(Object arg0) {
			this.log.warn(arg0);
		}

		@Override
		public void warn(Object arg0, Throwable arg1) {
			this.log.warn(arg0, arg1);
		}

	}

}
