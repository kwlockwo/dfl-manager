package net.dflmngr.model.service.impl.spring;

import net.dflmngr.model.entity.DflPreseasonScores;
import net.dflmngr.model.service.DflPreseasonScoresService;
import net.dflmngr.repositories.DflPreseasonScoresRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class DflPreseasonScoresServiceImpl implements DflPreseasonScoresService {
    
    private final DflPreseasonScoresRepository repository;
    
    public DflPreseasonScoresServiceImpl(DflPreseasonScoresRepository repository) {
        this.repository = repository;
    }
    
    @Override
    public DflPreseasonScores get(Integer id) {
        return repository.findById(id).orElse(null);
    }
    
    @Override
    public List<DflPreseasonScores> findAll() {
        return repository.findAll();
    }
    
    @Override
    public void insert(DflPreseasonScores entity) {
        repository.save(entity);
    }
    
    @Override
    public void update(DflPreseasonScores entity) {
        repository.save(entity);
    }
    
    @Override
    public void delete(DflPreseasonScores entity) {
        repository.delete(entity);
    }
    
    @Override
    public void insertAll(List<DflPreseasonScores> entities) {
        repository.saveAll(entities);
    }
    
    @Override
    public void updateAll(List<DflPreseasonScores> entities) {
        repository.saveAll(entities);
    }
    
    @Override
    public void replaceAll(List<DflPreseasonScores> entities) {
        repository.deleteAll();
        repository.flush();
        repository.saveAll(entities);
    }
    
    @Override
    public void refresh(DflPreseasonScores entity) {
        // No-op: Spring manages persistence context
    }
    
    @Override
    public void close() {
        // No-op: Spring manages lifecycle
    }
}
