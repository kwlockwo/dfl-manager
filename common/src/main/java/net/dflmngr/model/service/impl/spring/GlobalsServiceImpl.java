package net.dflmngr.model.service.impl.spring;

import net.dflmngr.model.entity.Globals;
import net.dflmngr.model.service.GlobalsService;
import net.dflmngr.repositories.GlobalsRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class GlobalsServiceImpl implements GlobalsService {
    
    private final GlobalsRepository repository;
    
    public GlobalsServiceImpl(GlobalsRepository repository) {
        this.repository = repository;
    }
    
    @Override
    public Globals get(String id) {
        return repository.findById(id).orElse(null);
    }
    
    @Override
    public List<Globals> findAll() {
        return repository.findAll();
    }
    
    @Override
    public void insert(Globals entity) {
        repository.save(entity);
    }
    
    @Override
    public void update(Globals entity) {
        repository.save(entity);
    }
    
    @Override
    public void delete(Globals entity) {
        repository.delete(entity);
    }
    
    @Override
    public void insertAll(List<Globals> entities) {
        repository.saveAll(entities);
    }
    
    @Override
    public void updateAll(List<Globals> entities) {
        repository.saveAll(entities);
    }
    
    @Override
    public void replaceAll(List<Globals> entities) {
        repository.deleteAll();
        repository.flush();
        repository.saveAll(entities);
    }
    
    @Override
    public void refresh(Globals entity) {
        // No-op: Spring manages persistence context
    }
    
    @Override
    public void close() {
        // No-op: Spring manages lifecycle
    }
}
