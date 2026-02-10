package net.dflmngr.services;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import net.dflmngr.model.entity.DflBest22;
import net.dflmngr.repositories.DflBest22Repository;

@Service
public class DflBest22Service {

    private final DflBest22Repository repository;

    public DflBest22Service(DflBest22Repository repository) {
        this.repository = repository;
    }

    // Business logic methods
    public List<DflBest22> getForRound(int round) {
        return repository.findByRound(round);
    }

    // Generic repository methods
    public DflBest22 get(Integer id) {
        return repository.findById(id).orElse(null);
    }

    public List<DflBest22> findAll() {
        return repository.findAll();
    }

    @Transactional
    public void insert(DflBest22 entity) {
        repository.save(entity);
    }

    @Transactional
    public void update(DflBest22 entity) {
        repository.save(entity);
    }

    @Transactional
    public void insertAll(List<DflBest22> entities) {
        repository.saveAll(entities);
    }

    @Transactional
    public void updateAll(List<DflBest22> entities) {
        repository.saveAll(entities);
    }

    @Transactional
    public void delete(DflBest22 entity) {
        repository.delete(entity);
    }

    @Transactional
    public void replaceAll(List<DflBest22> entities) {
        repository.deleteAll();
        repository.flush();
        repository.saveAll(entities);
    }
}
