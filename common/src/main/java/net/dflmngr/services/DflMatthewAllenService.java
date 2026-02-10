package net.dflmngr.services;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import net.dflmngr.model.entity.DflMatthewAllen;
import net.dflmngr.repositories.DflMatthewAllenRepository;

@Service
public class DflMatthewAllenService {

    private final DflMatthewAllenRepository repository;

    public DflMatthewAllenService(DflMatthewAllenRepository repository) {
        this.repository = repository;
    }

    // Business logic methods
    public List<DflMatthewAllen> getForRound(int round) {
        return repository.findByRound(round);
    }

    public DflMatthewAllen getLastVotes(int playerId) {
        return repository.findLastVotesByPlayerId(playerId);
    }

    @Transactional
    public void replaceAllForRound(int round, List<DflMatthewAllen> votes) {
        repository.deleteByRound(round);
        repository.flush();
        repository.saveAll(votes);
    }

    @Transactional
    public void deleteForRound(int round) {
        repository.deleteByRound(round);
    }

    // Generic repository methods
    public DflMatthewAllen get(Integer id) {
        return repository.findById(id).orElse(null);
    }

    public List<DflMatthewAllen> findAll() {
        return repository.findAll();
    }

    @Transactional
    public void insert(DflMatthewAllen entity) {
        repository.save(entity);
    }

    @Transactional
    public void update(DflMatthewAllen entity) {
        repository.save(entity);
    }

    @Transactional
    public void insertAll(List<DflMatthewAllen> entities) {
        repository.saveAll(entities);
    }

    @Transactional
    public void updateAll(List<DflMatthewAllen> entities) {
        repository.saveAll(entities);
    }

    @Transactional
    public void delete(DflMatthewAllen entity) {
        repository.delete(entity);
    }

    @Transactional
    public void replaceAll(List<DflMatthewAllen> entities) {
        repository.deleteAll();
        repository.flush();
        repository.saveAll(entities);
    }
}
