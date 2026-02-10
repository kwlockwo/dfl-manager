package net.dflmngr.services;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import net.dflmngr.model.entity.DflUnmatchedPlayer;
import net.dflmngr.repositories.DflUnmatchedPlayerRepository;

@Service
public class DflUnmatchedPlayerService {

    private final DflUnmatchedPlayerRepository repository;

    public DflUnmatchedPlayerService(DflUnmatchedPlayerRepository repository) {
        this.repository = repository;
    }

    // Generic repository methods
    public DflUnmatchedPlayer get(Integer id) {
        return repository.findById(id).orElse(null);
    }

    public List<DflUnmatchedPlayer> findAll() {
        return repository.findAll();
    }

    @Transactional
    public void insert(DflUnmatchedPlayer entity) {
        repository.save(entity);
    }

    @Transactional
    public void update(DflUnmatchedPlayer entity) {
        repository.save(entity);
    }

    @Transactional
    public void insertAll(List<DflUnmatchedPlayer> entities) {
        repository.saveAll(entities);
    }

    @Transactional
    public void updateAll(List<DflUnmatchedPlayer> entities) {
        repository.saveAll(entities);
    }

    @Transactional
    public void delete(DflUnmatchedPlayer entity) {
        repository.delete(entity);
    }

    @Transactional
    public void replaceAll(List<DflUnmatchedPlayer> entities) {
        repository.deleteAll();
        repository.flush();
        repository.saveAll(entities);
    }
}
