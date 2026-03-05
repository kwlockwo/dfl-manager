package net.dflmngr.logging;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LoggingUtils {

	private Logger logger;
	private String process;

	public LoggingUtils(String process) {
		this.process = process;

		boolean logToSyslog = Boolean.parseBoolean(System.getenv().getOrDefault("LOG_TO_SYSLOG", "false"));

		if(logToSyslog) {
			logger = LoggerFactory.getLogger("stdout-with-syslog-logger");
		} else {
			logger = LoggerFactory.getLogger("stdout-logger");
		}
	}

	public void log(String level, String msg, Object...arguments) {
		switch (level) {
			case "info"  : if (logger.isInfoEnabled())  logger.info(callerMsg(msg), prepend(process, arguments)); break;
			case "warn"  : if (logger.isWarnEnabled())  logger.warn(callerMsg(msg), prepend(process, arguments)); break;
			case "error" : if (logger.isErrorEnabled()) logger.error(callerMsg(msg), prepend(process, arguments)); break;
			default      : if (logger.isDebugEnabled()) logger.debug(callerMsg(msg), prepend(process, arguments)); break;
		}
	}

	public void logException(String msg, Throwable ex) {
		logger.error("[{}] {}", process, msg, ex);
	}

	private String callerMsg(String msg) {
		StackTraceElement caller = Thread.currentThread().getStackTrace()[3];
		String shortClass = caller.getClassName();
		shortClass = shortClass.substring(shortClass.lastIndexOf('.') + 1);
		return "[{}][" + shortClass + "." + caller.getMethodName() + "(Line:" + caller.getLineNumber() + ")] - " + msg;
	}

	private Object[] prepend(Object first, Object[] rest) {
		Object[] args = new Object[rest.length + 1];
		args[0] = first;
		System.arraycopy(rest, 0, args, 1, rest.length);
		return args;
	}
}
