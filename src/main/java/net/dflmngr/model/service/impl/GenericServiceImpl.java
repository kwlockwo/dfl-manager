package net.dflmngr.model.service.impl;

import java.util.List;

import net.dflmngr.jpa.TransactionHelper;
import net.dflmngr.model.dao.GenericDao;
import net.dflmngr.model.service.GenericService;

public class GenericServiceImpl<E, K> implements GenericService<E, K>  {
	
	private GenericDao<E, K> d;
	
    protected void setDao(GenericDao<E, K> dao) {
        this.d = dao;
    }
    
    public E get(K id) {
    	return d.findById(id);
    }
	
	public List<E> findAll() {
		return d.findAll();
	}
	
	public void insert(E entity) {
		TransactionHelper.executeInTransaction(d.getEntityManager(), () -> d.persist(entity));
	}
	
	public void update(E entity) {
		d.merge(entity);
	}
	
	public void insertAll(List<E> entitys, boolean inTx) {
		if (!inTx) {
			TransactionHelper.executeInTransaction(d.getEntityManager(), () -> {
				for (E e : entitys) {
					d.persist(e);
				}
			});
		} else {
			for (E e : entitys) {
				d.persist(e);
			}
		}
	}
	
	public void updateAll(List<E> entitys, boolean inTx) {
		if (!inTx) {
			TransactionHelper.executeInTransaction(d.getEntityManager(), () -> {
				for (E e : entitys) {
					d.merge(e);
				}
			});
		} else {
			for (E e : entitys) {
				d.merge(e);
			}
		}
	}
	
	public void delete(E entity) {
		d.remove(entity);
	}
	
	public void replaceAll(List<E> entitys) {
		TransactionHelper.executeInTransaction(d.getEntityManager(), () -> {
			List<E> existingEntitys = findAll();
			for (E entity : existingEntitys) {
				d.remove(entity);
			}

			d.flush();

			for (E entity : entitys) {
				d.persist(entity);
			}
		});
	}
	
	public void refresh(E entity) {
		d.refresh(entity);
	}
	
	public void close() {
		d.close();
	}
}
