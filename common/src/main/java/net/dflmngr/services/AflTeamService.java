package net.dflmngr.services;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import net.dflmngr.model.entity.AflTeam;
import net.dflmngr.repositories.AflTeamRepository;

@Service
public class AflTeamService {

    private final AflTeamRepository repository;

    public AflTeamService(AflTeamRepository repository) {
        this.repository = repository;
    }

    public AflTeam getAflTeamByName(String name) {
        return repository.findByName(name).orElse(null);
    }

    // Generic repository methods
    public AflTeam get(String id) {
        return repository.findById(id).orElse(null);
    }

    public List<AflTeam> findAll() {
        return repository.findAll();
    }

    @Transactional
    public void insert(AflTeam entity) {
        repository.save(entity);
    }

    @Transactional
    public void update(AflTeam entity) {
        repository.save(entity);
    }

    @Transactional
    public void insertAll(List<AflTeam> entities) {
        repository.saveAll(entities);
    }

    @Transactional
    public void updateAll(List<AflTeam> entities) {
        repository.saveAll(entities);
    }

    @Transactional
    public void delete(AflTeam entity) {
        repository.delete(entity);
    }

    @Transactional
    public void replaceAll(List<AflTeam> entities) {
        repository.deleteAll();
        repository.flush();
        repository.saveAll(entities);
    }
}
