package net.dflmngr.model.service.impl.spring;

import net.dflmngr.model.entity.DflSelectionIds;
import net.dflmngr.model.service.DflSelectionIdsService;
import net.dflmngr.repositories.DflSelectionIdsRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class DflSelectionIdsServiceImpl implements DflSelectionIdsService {
    
    private final DflSelectionIdsRepository repository;
    
    public DflSelectionIdsServiceImpl(DflSelectionIdsRepository repository) {
        this.repository = repository;
    }
    
    @Override
    public DflSelectionIds get(Integer id) {
        return repository.findById(id).orElse(null);
    }
    
    @Override
    public List<DflSelectionIds> findAll() {
        return repository.findAll();
    }
    
    @Override
    public void insert(DflSelectionIds entity) {
        repository.save(entity);
    }
    
    @Override
    public void update(DflSelectionIds entity) {
        repository.save(entity);
    }
    
    @Override
    public void delete(DflSelectionIds entity) {
        repository.delete(entity);
    }
    
    @Override
    public void insertAll(List<DflSelectionIds> entities) {
        repository.saveAll(entities);
    }
    
    @Override
    public void updateAll(List<DflSelectionIds> entities) {
        repository.saveAll(entities);
    }
    
    @Override
    public void replaceAll(List<DflSelectionIds> entities) {
        repository.deleteAll();
        repository.flush();
        repository.saveAll(entities);
    }
    
    @Override
    public void refresh(DflSelectionIds entity) {
        // No-op: Spring manages persistence context
    }
    
    @Override
    public void close() {
        // No-op: Spring manages lifecycle
    }
}
