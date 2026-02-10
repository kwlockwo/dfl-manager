package net.dflmngr.services;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import net.dflmngr.model.entity.DflTeam;
import net.dflmngr.repositories.DflTeamRepository;

@Service
public class DflTeamService {

    private final DflTeamRepository repository;

    public DflTeamService(DflTeamRepository repository) {
        this.repository = repository;
    }

    // Generic repository methods
    public DflTeam get(String id) {
        return repository.findById(id).orElse(null);
    }

    public List<DflTeam> findAll() {
        return repository.findAll();
    }

    @Transactional
    public void insert(DflTeam entity) {
        repository.save(entity);
    }

    @Transactional
    public void update(DflTeam entity) {
        repository.save(entity);
    }

    @Transactional
    public void insertAll(List<DflTeam> entities) {
        repository.saveAll(entities);
    }

    @Transactional
    public void updateAll(List<DflTeam> entities) {
        repository.saveAll(entities);
    }

    @Transactional
    public void delete(DflTeam entity) {
        repository.delete(entity);
    }

    @Transactional
    public void replaceAll(List<DflTeam> entities) {
        repository.deleteAll();
        repository.flush();
        repository.saveAll(entities);
    }
}
