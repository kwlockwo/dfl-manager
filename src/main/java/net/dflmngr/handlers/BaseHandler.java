package net.dflmngr.handlers;

import org.slf4j.MDC;

import net.dflmngr.logging.LoggingUtils;
import net.dflmngr.service.ServiceFactory;

/**
 * Base class for all handlers providing common functionality.
 * Simplifies handler initialization and logging configuration.
 */
public abstract class BaseHandler {

	protected LoggingUtils loggerUtils;
	protected ServiceFactory serviceFactory;

	protected boolean isExecutable;

	protected String defaultMdcKey = "batch.name";
	protected String defaultLoggerName = "batch-logger";
	protected String defaultLogfile;

	protected String mdcKey;
	protected String loggerName;
	protected String logfile;

	protected BaseHandler(String defaultLogfile) {
		this.defaultLogfile = defaultLogfile;
		this.serviceFactory = ServiceFactory.getInstance();
	}

	/**
	 * Configure logging for this handler.
	 * Must be called before execute() if custom logging is needed.
	 */
	public void configureLogging(String mdcKey, String loggerName, String logfile) {
		this.loggerUtils = new LoggingUtils(logfile);
		this.mdcKey = mdcKey;
		this.loggerName = loggerName;
		this.logfile = logfile;
		this.isExecutable = true;
		MDC.put("handler", logfile);
		MDC.put("mdcKey", mdcKey);
		MDC.put("loggerName", loggerName);
	}

	/**
	 * Configure logging with default values if not already configured.
	 * Called automatically by execute() methods.
	 */
	protected void ensureLoggingConfigured() {
		if (!isExecutable) {
			configureLogging(defaultMdcKey, defaultLoggerName, defaultLogfile);
			loggerUtils.log("info", "Default logging configured");
		}
	}
}
