package net.dflmngr.jpa;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;

/**
 * Utility for executing operations within a transaction.
 * Automatically handles transaction begin, commit, and rollback on exceptions.
 */
public class TransactionHelper {

	/**
	 * Executes a runnable operation within a transaction.
	 * Automatically commits on success or rolls back on exception.
	 *
	 * @param entityManager the EntityManager to use
	 * @param operation the operation to execute
	 * @throws RuntimeException if the operation fails
	 */
	public static void executeInTransaction(EntityManager entityManager, Runnable operation) {
		EntityTransaction transaction = entityManager.getTransaction();

		try {
			transaction.begin();
			operation.run();
			transaction.commit();
		} catch (Exception e) {
			if (transaction.isActive()) {
				transaction.rollback();
			}
			throw new RuntimeException("Transaction failed", e);
		}
	}

	/**
	 * Executes a supplier operation within a transaction and returns the result.
	 * Automatically commits on success or rolls back on exception.
	 *
	 * @param <T> the return type
	 * @param entityManager the EntityManager to use
	 * @param operation the operation to execute
	 * @return the result of the operation
	 * @throws RuntimeException if the operation fails
	 */
	public static <T> T executeInTransaction(EntityManager entityManager, java.util.function.Supplier<T> operation) {
		EntityTransaction transaction = entityManager.getTransaction();

		try {
			transaction.begin();
			T result = operation.get();
			transaction.commit();
			return result;
		} catch (Exception e) {
			if (transaction.isActive()) {
				transaction.rollback();
			}
			throw new RuntimeException("Transaction failed", e);
		}
	}
}
