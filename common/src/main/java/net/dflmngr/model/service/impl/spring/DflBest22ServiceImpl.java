package net.dflmngr.model.service.impl.spring;

import net.dflmngr.model.entity.DflBest22;
import net.dflmngr.model.service.DflBest22Service;
import net.dflmngr.repositories.DflBest22Repository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class DflBest22ServiceImpl implements DflBest22Service {
    
    private final DflBest22Repository repository;
    
    public DflBest22ServiceImpl(DflBest22Repository repository) {
        this.repository = repository;
    }
    
    public DflBest22 get(Integer id) {
        return repository.findById(id).orElse(null);
    }
    
    public List<DflBest22> findAll() {
        return repository.findAll();
    }
    
    public void insert(DflBest22 entity) {
        repository.save(entity);
    }
    
    public void update(DflBest22 entity) {
        repository.save(entity);
    }
    
    public void delete(DflBest22 entity) {
        repository.delete(entity);
    }
    
    public void insertAll(List<DflBest22> entities) {
        repository.saveAll(entities);
    }
    
    public void updateAll(List<DflBest22> entities) {
        repository.saveAll(entities);
    }
    
    public void replaceAll(List<DflBest22> entities) {
        repository.deleteAll();
        repository.flush();
        repository.saveAll(entities);
    }
    
    public void refresh(DflBest22 entity) {
        // No-op: Spring manages persistence context
    }
    
    public void close() {
        // No-op: Spring manages lifecycle
    }
    
    public List<DflBest22> findByRound(int round) {
        return repository.findByRound(round);
    }

    public List<DflBest22> getForRound(int round) {
        return repository.findByRound(round);
    }

    public void insertAll(List<DflBest22> entities, boolean inTx) {
        // inTx parameter ignored - Spring manages transactions via @Transactional
        repository.saveAll(entities);
    }

    public void updateAll(List<DflBest22> entities, boolean inTx) {
        // inTx parameter ignored - Spring manages transactions via @Transactional
        repository.saveAll(entities);
    }
}
