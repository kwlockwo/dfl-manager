package net.dflmngr.model.service.impl.spring;

import net.dflmngr.model.entity.AflFixture;
import net.dflmngr.model.entity.keys.AflFixturePK;
import net.dflmngr.model.service.AflFixtureService;
import net.dflmngr.repositories.AflFixtureRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class AflFixtureServiceImpl implements AflFixtureService {
    
    private final AflFixtureRepository repository;
    
    public AflFixtureServiceImpl(AflFixtureRepository repository) {
        this.repository = repository;
    }
    
    @Override
    public AflFixture get(AflFixturePK id) {
        return repository.findById(id).orElse(null);
    }
    
    @Override
    public List<AflFixture> findAll() {
        return repository.findAll();
    }
    
    @Override
    public void insert(AflFixture entity) {
        repository.save(entity);
    }
    
    @Override
    public void update(AflFixture entity) {
        repository.save(entity);
    }
    
    @Override
    public void delete(AflFixture entity) {
        repository.delete(entity);
    }
    
    @Override
    public void insertAll(List<AflFixture> entities) {
        repository.saveAll(entities);
    }
    
    @Override
    public void updateAll(List<AflFixture> entities) {
        repository.saveAll(entities);
    }
    
    @Override
    public void replaceAll(List<AflFixture> entities) {
        repository.deleteAll();
        repository.flush();
        repository.saveAll(entities);
    }
    
    @Override
    public void refresh(AflFixture entity) {
        // No-op: Spring manages persistence context
    }
    
    @Override
    public void close() {
        // No-op: Spring manages lifecycle
    }
    
    @Override
    public List<AflFixture> findByRound(int round) {
        return repository.findByRound(round);
    }
    
    @Override
    public AflFixture findByRoundAndGame(int round, int game) {
        return repository.findByRoundAndGame(round, game);
    }
}
