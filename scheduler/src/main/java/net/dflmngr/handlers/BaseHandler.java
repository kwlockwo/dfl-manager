package net.dflmngr.handlers;

import net.dflmngr.logging.LoggingUtils;

/**
 * Base class for all handlers providing common functionality.
 * Simplifies handler initialization and logging configuration.
 */
public abstract class BaseHandler {

	protected LoggingUtils loggerUtils;

	protected boolean isExecutable;

	protected String defaultMdcKey = "batch.name";
	protected String defaultLoggerName = "batch-logger";
	protected String defaultLogfile;

	protected String mdcKey;
	protected String loggerName;
	protected String logfile;

	protected BaseHandler(String defaultLogfile) {
		this.defaultLogfile = defaultLogfile;
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
