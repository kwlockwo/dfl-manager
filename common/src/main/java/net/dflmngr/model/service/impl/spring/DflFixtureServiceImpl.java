package net.dflmngr.model.service.impl.spring;

import net.dflmngr.model.entity.DflFixture;
import net.dflmngr.model.entity.keys.DflFixturePK;
import net.dflmngr.model.service.DflFixtureService;
import net.dflmngr.repositories.DflFixtureRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class DflFixtureServiceImpl implements DflFixtureService {
    
    private final DflFixtureRepository repository;
    
    public DflFixtureServiceImpl(DflFixtureRepository repository) {
        this.repository = repository;
    }
    
    public DflFixture get(DflFixturePK id) {
        return repository.findById(id).orElse(null);
    }
    
    public List<DflFixture> findAll() {
        return repository.findAll();
    }
    
    public void insert(DflFixture entity) {
        repository.save(entity);
    }
    
    public void update(DflFixture entity) {
        repository.save(entity);
    }
    
    public void delete(DflFixture entity) {
        repository.delete(entity);
    }
    
    public void insertAll(List<DflFixture> entities) {
        repository.saveAll(entities);
    }
    
    public void updateAll(List<DflFixture> entities) {
        repository.saveAll(entities);
    }
    
    public void replaceAll(List<DflFixture> entities) {
        repository.deleteAll();
        repository.flush();
        repository.saveAll(entities);
    }
    
    public void refresh(DflFixture entity) {
        // No-op: Spring manages persistence context
    }
    
    public void close() {
        // No-op: Spring manages lifecycle
    }
    
    public List<DflFixture> findByRound(int round) {
        return repository.findByRound(round);
    }
    
    public DflFixture findByRoundAndGame(int round, int game) {
        return repository.findByRoundAndGame(round, game);
    }

    public void insertAll(List<DflFixture> entities, boolean inTx) {
        // inTx parameter ignored - Spring manages transactions via @Transactional
        repository.saveAll(entities);
    }

    public void updateAll(List<DflFixture> entities, boolean inTx) {
        // inTx parameter ignored - Spring manages transactions via @Transactional
        repository.saveAll(entities);
    }

    public List<DflFixture> getFixturesForRound(int round) {
        return repository.findByRound(round);
    }
}
