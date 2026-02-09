package net.dflmngr.model.service.impl.spring;

import net.dflmngr.model.entity.DflMatthewAllen;
import net.dflmngr.model.service.DflMatthewAllenService;
import net.dflmngr.repositories.DflMatthewAllenRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class DflMatthewAllenServiceImpl implements DflMatthewAllenService {
    
    private final DflMatthewAllenRepository repository;
    
    public DflMatthewAllenServiceImpl(DflMatthewAllenRepository repository) {
        this.repository = repository;
    }
    
    @Override
    public DflMatthewAllen get(Integer id) {
        return repository.findById(id).orElse(null);
    }
    
    @Override
    public List<DflMatthewAllen> findAll() {
        return repository.findAll();
    }
    
    @Override
    public void insert(DflMatthewAllen entity) {
        repository.save(entity);
    }
    
    @Override
    public void update(DflMatthewAllen entity) {
        repository.save(entity);
    }
    
    @Override
    public void delete(DflMatthewAllen entity) {
        repository.delete(entity);
    }
    
    @Override
    public void insertAll(List<DflMatthewAllen> entities) {
        repository.saveAll(entities);
    }
    
    @Override
    public void updateAll(List<DflMatthewAllen> entities) {
        repository.saveAll(entities);
    }
    
    @Override
    public void replaceAll(List<DflMatthewAllen> entities) {
        repository.deleteAll();
        repository.flush();
        repository.saveAll(entities);
    }
    
    @Override
    public void refresh(DflMatthewAllen entity) {
        // No-op: Spring manages persistence context
    }
    
    @Override
    public void close() {
        // No-op: Spring manages lifecycle
    }
    
    @Override
    public DflMatthewAllen findByPlayerId(int playerId) {
        return repository.findByPlayerId(playerId);
    }
}
