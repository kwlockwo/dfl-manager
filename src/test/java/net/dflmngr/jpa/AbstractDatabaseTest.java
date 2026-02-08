package net.dflmngr.jpa;

import org.junit.jupiter.api.BeforeAll;

/**
 * Base class for tests that need database access.
 * Configures the test persistence unit and H2 database before tests run.
 */
public abstract class AbstractDatabaseTest {

	@BeforeAll
	static void configureTestPersistenceUnit() {
		// Set system property to use test persistence unit
		System.setProperty("persistence.unit.name", "dflmngr-test");

		// Reset the singleton to force it to use the test persistence unit
		EntityManagerFactoryProvider.resetForTesting();
	}
}
