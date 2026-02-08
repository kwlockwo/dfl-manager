package net.dflmngr.jpa;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

/**
 * Tests for TransactionHelper.
 * Verifies proper transaction management including commit and rollback behavior.
 */
@ExtendWith(MockitoExtension.class)
class TransactionHelperTest {

	@Mock
	private EntityManager entityManager;

	@Mock
	private EntityTransaction transaction;

	@BeforeEach
	void setUp() {
		when(entityManager.getTransaction()).thenReturn(transaction);
	}

	@Test
	void executeInTransaction_shouldCommitOnSuccess() {
		// Given
		Runnable operation = mock(Runnable.class);

		// When
		TransactionHelper.executeInTransaction(entityManager, operation);

		// Then
		verify(transaction).begin();
		verify(operation).run();
		verify(transaction).commit();
		verify(transaction, never()).rollback();
	}

	@Test
	void executeInTransaction_shouldRollbackOnException() {
		// Given
		when(transaction.isActive()).thenReturn(true);
		Runnable operation = () -> {
			throw new RuntimeException("Test exception");
		};

		// When & Then
		assertThatThrownBy(() -> TransactionHelper.executeInTransaction(entityManager, operation))
			.isInstanceOf(RuntimeException.class)
			.hasMessage("Transaction failed");

		verify(transaction).begin();
		verify(transaction).rollback();
		verify(transaction, never()).commit();
	}

	@Test
	void executeInTransaction_shouldNotRollbackIfTransactionNotActive() {
		// Given
		when(transaction.isActive()).thenReturn(false);
		Runnable operation = () -> {
			throw new RuntimeException("Test exception");
		};

		// When & Then
		assertThatThrownBy(() -> TransactionHelper.executeInTransaction(entityManager, operation))
			.isInstanceOf(RuntimeException.class)
			.hasMessageContaining("Transaction failed");

		verify(transaction).begin();
		verify(transaction, never()).rollback();
	}

	@Test
	void executeInTransaction_shouldPreserveOriginalException() {
		// Given
		when(transaction.isActive()).thenReturn(true);
		RuntimeException originalException = new RuntimeException("Original error");
		Runnable operation = () -> {
			throw originalException;
		};

		// When & Then
		assertThatThrownBy(() -> TransactionHelper.executeInTransaction(entityManager, operation))
			.isInstanceOf(RuntimeException.class)
			.hasMessageContaining("Transaction failed")
			.cause()
			.isSameAs(originalException);
	}
}
