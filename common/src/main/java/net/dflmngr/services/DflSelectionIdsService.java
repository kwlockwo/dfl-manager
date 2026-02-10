package net.dflmngr.services;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import net.dflmngr.model.entity.DflSelectionIds;
import net.dflmngr.repositories.DflSelectionIdsRepository;

@Service
public class DflSelectionIdsService {

    private final DflSelectionIdsRepository repository;

    public DflSelectionIdsService(DflSelectionIdsRepository repository) {
        this.repository = repository;
    }

    // Business logic methods
    public boolean selectionIdExists(int round, String teamCode, String id) {
        List<DflSelectionIds> selectionIds = repository.findByRoundAndTeamCodeAndSelectionId(round, teamCode, id);
        return selectionIds != null && !selectionIds.isEmpty();
    }

    // Generic repository methods
    public DflSelectionIds get(Integer id) {
        return repository.findById(id).orElse(null);
    }

    public List<DflSelectionIds> findAll() {
        return repository.findAll();
    }

    @Transactional
    public void insert(DflSelectionIds entity) {
        repository.save(entity);
    }

    @Transactional
    public void update(DflSelectionIds entity) {
        repository.save(entity);
    }

    @Transactional
    public void insertAll(List<DflSelectionIds> entities) {
        repository.saveAll(entities);
    }

    @Transactional
    public void updateAll(List<DflSelectionIds> entities) {
        repository.saveAll(entities);
    }

    @Transactional
    public void delete(DflSelectionIds entity) {
        repository.delete(entity);
    }

    @Transactional
    public void replaceAll(List<DflSelectionIds> entities) {
        repository.deleteAll();
        repository.flush();
        repository.saveAll(entities);
    }
}
