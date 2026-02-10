package net.dflmngr.services;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import net.dflmngr.model.entity.DflFixture;
import net.dflmngr.model.entity.keys.DflFixturePK;
import net.dflmngr.repositories.DflFixtureRepository;

@Service
public class DflFixtureService {

    private final DflFixtureRepository repository;

    public DflFixtureService(DflFixtureRepository repository) {
        this.repository = repository;
    }

    public List<DflFixture> getFixturesForRound(int round) {
        return repository.findByRound(round);
    }

    // Generic repository methods
    public DflFixture get(DflFixturePK id) {
        return repository.findById(id).orElse(null);
    }

    public List<DflFixture> findAll() {
        return repository.findAll();
    }

    @Transactional
    public void insert(DflFixture entity) {
        repository.save(entity);
    }

    @Transactional
    public void update(DflFixture entity) {
        repository.save(entity);
    }

    @Transactional
    public void insertAll(List<DflFixture> entities) {
        repository.saveAll(entities);
    }

    @Transactional
    public void updateAll(List<DflFixture> entities) {
        repository.saveAll(entities);
    }

    @Transactional
    public void delete(DflFixture entity) {
        repository.delete(entity);
    }

    @Transactional
    public void replaceAll(List<DflFixture> entities) {
        repository.deleteAll();
        repository.flush();
        repository.saveAll(entities);
    }
}
