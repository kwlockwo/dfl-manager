package net.dflmngr.model.dao.impl;

import java.util.List;

import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaDelete;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;

import net.dflmngr.jpa.EntityManagerFactoryProvider;
import net.dflmngr.model.dao.GenericDao;

public abstract class GenericDaoImpl<E, K> implements GenericDao<E, K> {
	
	protected Class<E> entityClass;
	protected CriteriaBuilder criteriaBuilder;
	protected CriteriaQuery<E> criteriaQuery;
	protected CriteriaDelete<E> criteriaDelete;
	protected TypedQuery<E> query;
	protected Root<E> entity;

	protected EntityManager entityManager;

	protected GenericDaoImpl(Class<E> entityClass) {
		this.entityClass = entityClass;
		this.entityManager = EntityManagerFactoryProvider.getInstance().createEntityManager();
	}
	
	public void persist(E entity) {
		entityManager.persist(entity);
	}
	
	public void merge(E entity) {
		entityManager.detach(entity);
		entityManager.merge(entity);
	}
	
	public void remove(E entity) {
		entityManager.remove(entity);
	}
	
	public E findById(K id) {
		return entityManager.find(entityClass, id);
	}
	
	public List<E> findAll() {		
		criteriaBuilder = entityManager.getCriteriaBuilder();
		criteriaQuery = criteriaBuilder.createQuery(entityClass);
		criteriaQuery.from(entityClass);
		return entityManager.createQuery(criteriaQuery).getResultList();
	}
	
	public void commit() {
		entityManager.getTransaction().commit();
	}
	
	public void beginTransaction() {
		entityManager.getTransaction().begin();
	}
	
	public void flush() {
		entityManager.flush();
	}
	
	public void refresh(E entity) {
		entityManager.refresh(entity);
	}
	
	public void close() {
		entityManager.clear();
		entityManager.close();
		entityManager = null;
	}

	public EntityManager getEntityManager() {
		return entityManager;
	}
}
