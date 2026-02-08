package net.dflmngr.jpa;

import static org.assertj.core.api.Assertions.assertThat;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import org.junit.jupiter.api.Test;

/**
 * Tests for EntityManagerFactoryProvider singleton.
 * Verifies that only one EntityManagerFactory instance exists and it creates EntityManagers correctly.
 */
class EntityManagerFactoryProviderTest {

	@Test
	void getInstance_shouldReturnSameInstance() {
		// Given & When
		EntityManagerFactoryProvider instance1 = EntityManagerFactoryProvider.getInstance();
		EntityManagerFactoryProvider instance2 = EntityManagerFactoryProvider.getInstance();

		// Then
		assertThat(instance1).isNotNull();
		assertThat(instance1).isSameAs(instance2);
	}

	@Test
	void createEntityManager_shouldCreateNewEntityManager() {
		// Given
		EntityManagerFactoryProvider provider = EntityManagerFactoryProvider.getInstance();

		// When
		EntityManager em1 = provider.createEntityManager();
		EntityManager em2 = provider.createEntityManager();

		// Then
		assertThat(em1).isNotNull();
		assertThat(em2).isNotNull();
		assertThat(em1).isNotSameAs(em2);

		// Cleanup
		em1.close();
		em2.close();
	}

	@Test
	void getFactory_shouldReturnEntityManagerFactory() {
		// Given
		EntityManagerFactoryProvider provider = EntityManagerFactoryProvider.getInstance();

		// When
		EntityManagerFactory factory = provider.getFactory();

		// Then
		assertThat(factory).isNotNull();
		assertThat(factory.isOpen()).isTrue();
	}
}
