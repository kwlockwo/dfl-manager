package net.dflmngr.services;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import net.dflmngr.model.entity.DflRoundInfo;
import net.dflmngr.repositories.DflRoundInfoRepository;

@Service
public class DflRoundInfoService {

    private final DflRoundInfoRepository repository;

    public DflRoundInfoService(DflRoundInfoRepository repository) {
        this.repository = repository;
    }

    public List<DflRoundInfo> getRoundsByAflRounds(List<Integer> aflRounds) {
        return repository.findRoundsByAflRounds(aflRounds);
    }

    // Generic repository methods
    public DflRoundInfo get(Integer id) {
        return repository.findById(id).orElse(null);
    }

    public List<DflRoundInfo> findAll() {
        return repository.findAll();
    }

    @Transactional
    public void insert(DflRoundInfo entity) {
        repository.save(entity);
    }

    @Transactional
    public void update(DflRoundInfo entity) {
        repository.save(entity);
    }

    @Transactional
    public void insertAll(List<DflRoundInfo> entities) {
        repository.saveAll(entities);
    }

    @Transactional
    public void updateAll(List<DflRoundInfo> entities) {
        repository.saveAll(entities);
    }

    @Transactional
    public void delete(DflRoundInfo entity) {
        repository.delete(entity);
    }

    @Transactional
    public void replaceAll(List<DflRoundInfo> entities) {
        repository.deleteAll();
        repository.flush();
        repository.saveAll(entities);
    }
}
