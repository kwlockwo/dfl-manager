package net.dflmngr.scheduler.generators;

import net.dflmngr.logging.LoggingUtils;
import net.dflmngr.service.ServiceFactory;

/**
 * Base class for all job generators.
 * Provides common functionality for logging, service management, and execution pattern.
 */
public abstract class BaseJobGenerator {

	protected LoggingUtils loggerUtils;
	protected ServiceFactory serviceFactory;

	protected BaseJobGenerator(String generatorName) {
		this.loggerUtils = new LoggingUtils(generatorName);
		this.serviceFactory = ServiceFactory.getInstance();
	}

	/**
	 * Template method that handles common execution pattern:
	 * 1. Log execution start
	 * 2. Call subclass-specific generation logic
	 * 3. Close services
	 * 4. Log completion
	 * 5. Handle exceptions
	 */
	public final void execute() {
		try {
			loggerUtils.log("info", "Executing {} ....", getGeneratorName());

			// Subclass implements specific job generation logic
			generateJobs();

			// Close all services
			closeServices();

			loggerUtils.log("info", "{} completed", getGeneratorName());
		} catch (Exception ex) {
			loggerUtils.logException("Error in " + getGeneratorName(), ex);
		}
	}

	/**
	 * Subclasses implement this method to perform their specific job generation logic.
	 * This is called after logging starts and before services are closed.
	 *
	 * @throws Exception if job generation fails
	 */
	protected abstract void generateJobs() throws Exception;

	/**
	 * Returns the name of this generator for logging purposes.
	 * Defaults to the simple class name.
	 */
	protected String getGeneratorName() {
		return this.getClass().getSimpleName();
	}

	/**
	 * Close all services used by this generator.
	 * Subclasses can override to close additional services.
	 */
	protected void closeServices() {
		// Services are created per-request via factory, so they need to be closed
		// Subclasses should override and close their specific services
	}
}
