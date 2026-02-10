package net.dflmngr.services;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import net.dflmngr.model.entity.AflPlayer;
import net.dflmngr.repositories.AflPlayerRepository;

@Service
public class AflPlayerService {

    private final AflPlayerRepository repository;

    public AflPlayerService(AflPlayerRepository repository) {
        this.repository = repository;
    }

    @Transactional
    public void bulkUpdateDflPlayerId(Map<Integer, AflPlayer> entities) {
        for(Map.Entry<Integer, AflPlayer> entry : entities.entrySet()) {
            Integer dflPlayerId = entry.getKey();
            AflPlayer player = entry.getValue();
            player.setDflPlayerId(dflPlayerId);
        }
        // Changes are automatically persisted at end of transaction
    }

    // Generic repository methods
    public AflPlayer get(String id) {
        return repository.findById(id).orElse(null);
    }

    public List<AflPlayer> findAll() {
        return repository.findAll();
    }

    @Transactional
    public void insert(AflPlayer entity) {
        repository.save(entity);
    }

    @Transactional
    public void update(AflPlayer entity) {
        repository.save(entity);
    }

    @Transactional
    public void insertAll(List<AflPlayer> entities) {
        repository.saveAll(entities);
    }

    @Transactional
    public void updateAll(List<AflPlayer> entities) {
        repository.saveAll(entities);
    }

    @Transactional
    public void delete(AflPlayer entity) {
        repository.delete(entity);
    }

    @Transactional
    public void replaceAll(List<AflPlayer> entities) {
        repository.deleteAll();
        repository.flush();
        repository.saveAll(entities);
    }
}
