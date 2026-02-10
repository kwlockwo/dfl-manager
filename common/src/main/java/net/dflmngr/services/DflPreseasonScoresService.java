package net.dflmngr.services;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import net.dflmngr.model.entity.DflPreseasonScores;
import net.dflmngr.model.entity.keys.DflPreseasonScoresPK;
import net.dflmngr.repositories.DflPreseasonScoresRepository;

@Service
public class DflPreseasonScoresService {

    private final DflPreseasonScoresRepository repository;

    public DflPreseasonScoresService(DflPreseasonScoresRepository repository) {
        this.repository = repository;
    }

    // Generic repository methods
    public DflPreseasonScores get(DflPreseasonScoresPK id) {
        return repository.findById(id).orElse(null);
    }

    public List<DflPreseasonScores> findAll() {
        return repository.findAll();
    }

    @Transactional
    public void insert(DflPreseasonScores entity) {
        repository.save(entity);
    }

    @Transactional
    public void update(DflPreseasonScores entity) {
        repository.save(entity);
    }

    @Transactional
    public void insertAll(List<DflPreseasonScores> entities) {
        repository.saveAll(entities);
    }

    @Transactional
    public void updateAll(List<DflPreseasonScores> entities) {
        repository.saveAll(entities);
    }

    @Transactional
    public void delete(DflPreseasonScores entity) {
        repository.delete(entity);
    }

    @Transactional
    public void replaceAll(List<DflPreseasonScores> entities) {
        repository.deleteAll();
        repository.flush();
        repository.saveAll(entities);
    }
}
