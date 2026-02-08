package net.dflmngr.model.dao;

import java.util.List;

import jakarta.persistence.EntityManager;

public interface GenericDao<E, K> {
	void persist(E entity);
	void merge(E entity);
	void remove(E enitiy);
	E findById(K id);
	List<E> findAll();
	void commit();
	void beginTransaction();
	void flush();
	void refresh(E entity);
	void close();
	EntityManager getEntityManager();
}
