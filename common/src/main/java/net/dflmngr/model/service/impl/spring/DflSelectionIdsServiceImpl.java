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
    
    public DflSelectionIds get(Integer id) {
        return repository.findById(id).orElse(null);
    }
    
    public List<DflSelectionIds> findAll() {
        return repository.findAll();
    }
    
    public void insert(DflSelectionIds entity) {
        repository.save(entity);
    }
    
    public void update(DflSelectionIds entity) {
        repository.save(entity);
    }
    
    public void delete(DflSelectionIds entity) {
        repository.delete(entity);
    }
    
    public void insertAll(List<DflSelectionIds> entities) {
        repository.saveAll(entities);
    }
    
    public void updateAll(List<DflSelectionIds> entities) {
        repository.saveAll(entities);
    }
    
    public void replaceAll(List<DflSelectionIds> entities) {
        repository.deleteAll();
        repository.flush();
        repository.saveAll(entities);
    }
    
    public void refresh(DflSelectionIds entity) {
        // No-op: Spring manages persistence context
    }
    
    public void close() {
        // No-op: Spring manages lifecycle
    }

    public void insertAll(List<DflSelectionIds> entities, boolean inTx) {
        // inTx parameter ignored - Spring manages transactions via @Transactional
        repository.saveAll(entities);
    }

    public void updateAll(List<DflSelectionIds> entities, boolean inTx) {
        // inTx parameter ignored - Spring manages transactions via @Transactional
        repository.saveAll(entities);
    }

    public boolean selectionIdExists(int round, String teamCode, String id) {
        List<DflSelectionIds> results = repository.findByRoundAndTeamCodeAndSelectionId(round, teamCode, id);
        return !results.isEmpty();
    }
}
