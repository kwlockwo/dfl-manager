package net.dflmngr.handlers;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.MDC;

import net.dflmngr.logging.LoggingUtils;
import net.dflmngr.model.service.GenericService;
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

	private final List<GenericService<?, ?>> managedServices = new ArrayList<>();

	protected BaseHandler(String defaultLogfile) {
		this.defaultLogfile = defaultLogfile;
		this.serviceFactory = ServiceFactory.getInstance();
	}

	/**
	 * Register a service so closeServices() will close it. Wrap service
	 * creation in the constructor: manage(serviceFactory.createXxx()).
	 */
	protected <S extends GenericService<?, ?>> S manage(S service) {
		managedServices.add(service);
		return service;
	}

	/**
	 * Close all managed services. Call from a finally block in execute().
	 */
	protected void closeServices() {
		for (GenericService<?, ?> service : managedServices) {
			try {
				service.close();
			} catch (Exception ex) {
				if (loggerUtils != null) {
					loggerUtils.log("warn", "Error closing service: {}", ex.getMessage());
				}
			}
		}
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
