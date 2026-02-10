package net.dflmngr.model.service.impl.spring;

import net.dflmngr.model.entity.AflPlayer;
import net.dflmngr.model.service.AflPlayerService;
import net.dflmngr.repositories.AflPlayerRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

@Service
@Transactional
public class AflPlayerServiceImpl implements AflPlayerService {
    
    private final AflPlayerRepository repository;
    
    public AflPlayerServiceImpl(AflPlayerRepository repository) {
        this.repository = repository;
    }
    
    public AflPlayer get(String id) {
        return repository.findById(id).orElse(null);
    }
    
    public List<AflPlayer> findAll() {
        return repository.findAll();
    }
    
    public void insert(AflPlayer entity) {
        repository.save(entity);
    }
    
    public void update(AflPlayer entity) {
        repository.save(entity);
    }
    
    public void delete(AflPlayer entity) {
        repository.delete(entity);
    }
    
    public void insertAll(List<AflPlayer> entities) {
        repository.saveAll(entities);
    }
    
    public void updateAll(List<AflPlayer> entities) {
        repository.saveAll(entities);
    }
    
    public void replaceAll(List<AflPlayer> entities) {
        repository.deleteAll();
        repository.flush();
        repository.saveAll(entities);
    }
    
    public void refresh(AflPlayer entity) {
        // No-op: Spring manages persistence context
    }
    
    public void close() {
        // No-op: Spring manages lifecycle
    }
    
    public AflPlayer findByPlayerId(int playerId) {
        // Player ID is the primary key (String), convert int to String
        return repository.findById(String.valueOf(playerId)).orElse(null);
    }

    public void insertAll(List<AflPlayer> entities, boolean inTx) {
        // inTx parameter ignored - Spring manages transactions via @Transactional
        repository.saveAll(entities);
    }

    public void updateAll(List<AflPlayer> entities, boolean inTx) {
        // inTx parameter ignored - Spring manages transactions via @Transactional
        repository.saveAll(entities);
    }

    public void bulkUpdateDflPlayerId(Map<Integer, AflPlayer> entities) {
        for (Map.Entry<Integer, AflPlayer> entry : entities.entrySet()) {
            Integer dflPlayerId = entry.getKey();
            AflPlayer player = entry.getValue();
            player.setDflPlayerId(dflPlayerId);
        }
        repository.saveAll(entities.values());
    }
}
