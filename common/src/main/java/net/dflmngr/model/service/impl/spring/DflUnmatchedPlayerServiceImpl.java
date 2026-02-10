package net.dflmngr.model.service.impl.spring;

import net.dflmngr.model.entity.DflUnmatchedPlayer;
import net.dflmngr.model.service.DflUnmatchedPlayerService;
import net.dflmngr.repositories.DflUnmatchedPlayerRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class DflUnmatchedPlayerServiceImpl implements DflUnmatchedPlayerService {
    
    private final DflUnmatchedPlayerRepository repository;
    
    public DflUnmatchedPlayerServiceImpl(DflUnmatchedPlayerRepository repository) {
        this.repository = repository;
    }
    
    public DflUnmatchedPlayer get(Integer id) {
        return repository.findById(id).orElse(null);
    }
    
    public List<DflUnmatchedPlayer> findAll() {
        return repository.findAll();
    }
    
    public void insert(DflUnmatchedPlayer entity) {
        repository.save(entity);
    }
    
    public void update(DflUnmatchedPlayer entity) {
        repository.save(entity);
    }
    
    public void delete(DflUnmatchedPlayer entity) {
        repository.delete(entity);
    }
    
    public void insertAll(List<DflUnmatchedPlayer> entities) {
        repository.saveAll(entities);
    }
    
    public void updateAll(List<DflUnmatchedPlayer> entities) {
        repository.saveAll(entities);
    }
    
    public void replaceAll(List<DflUnmatchedPlayer> entities) {
        repository.deleteAll();
        repository.flush();
        repository.saveAll(entities);
    }
    
    public void refresh(DflUnmatchedPlayer entity) {
        // No-op: Spring manages persistence context
    }
    
    public void close() {
        // No-op: Spring manages lifecycle
    }

    public void insertAll(List<DflUnmatchedPlayer> entities, boolean inTx) {
        // inTx parameter ignored - Spring manages transactions via @Transactional
        repository.saveAll(entities);
    }

    public void updateAll(List<DflUnmatchedPlayer> entities, boolean inTx) {
        // inTx parameter ignored - Spring manages transactions via @Transactional
        repository.saveAll(entities);
    }
}
