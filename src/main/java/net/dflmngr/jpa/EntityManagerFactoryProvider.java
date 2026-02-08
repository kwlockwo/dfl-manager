package net.dflmngr.jpa;

import java.util.HashMap;
import java.util.Map;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;

/**
 * Singleton provider for EntityManagerFactory and EntityManager instances.
 * Eliminates the overhead of creating multiple EntityManagerFactory instances.
 */
public class EntityManagerFactoryProvider {

	private static EntityManagerFactoryProvider instance;
	private final EntityManagerFactory factory;

	private EntityManagerFactoryProvider() {
		// Allow switching to test persistence unit via system property
		String persistenceUnitName = System.getProperty("persistence.unit.name", "dflmngr");

		String url = System.getenv("JDBC_DATABASE_URL");

		Map<String, Object> configOverrides = new HashMap<>();
		configOverrides.put("javax.persistence.jdbc.url", url);
		configOverrides.put("jakarta.persistence.jdbc.url", url);

		// Add system property overrides (these take precedence for tests)
		for (String propName : System.getProperties().stringPropertyNames()) {
			if (propName.startsWith("jakarta.persistence")
				|| propName.startsWith("javax.persistence")
				|| propName.startsWith("eclipselink")) {
				configOverrides.put(propName, System.getProperty(propName));
			}
		}

		// Add environment variable overrides for JPA/EclipseLink properties
		Map<String, String> env = System.getenv();
		for (Map.Entry<String, String> envVar : env.entrySet()) {
			String envVarName = envVar.getKey();
			String envVarValue = envVar.getValue();
			if (envVarName.contains("javax.persistence")
				|| envVarName.contains("eclipselink")
				|| envVarName.contains("jakarta.persistence")) {
				configOverrides.put(envVarName, envVarValue);
			}
		}

		this.factory = Persistence.createEntityManagerFactory(persistenceUnitName, configOverrides);
	}

	public static synchronized EntityManagerFactoryProvider getInstance() {
		if (instance == null) {
			instance = new EntityManagerFactoryProvider();
		}
		return instance;
	}

	public EntityManagerFactory getFactory() {
		return factory;
	}

	public EntityManager createEntityManager() {
		return factory.createEntityManager();
	}

	public void close() {
		if (factory != null && factory.isOpen()) {
			factory.close();
		}
	}
}
