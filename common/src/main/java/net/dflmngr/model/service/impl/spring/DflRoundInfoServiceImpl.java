package net.dflmngr.model.service.impl.spring;

import net.dflmngr.model.entity.DflRoundInfo;
import net.dflmngr.model.service.DflRoundInfoService;
import net.dflmngr.repositories.DflRoundInfoRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class DflRoundInfoServiceImpl implements DflRoundInfoService {
    
    private final DflRoundInfoRepository repository;
    
    public DflRoundInfoServiceImpl(DflRoundInfoRepository repository) {
        this.repository = repository;
    }
    
    public DflRoundInfo get(Integer id) {
        return repository.findById(id).orElse(null);
    }
    
    public List<DflRoundInfo> findAll() {
        return repository.findAll();
    }
    
    public void insert(DflRoundInfo entity) {
        repository.save(entity);
    }
    
    public void update(DflRoundInfo entity) {
        repository.save(entity);
    }
    
    public void delete(DflRoundInfo entity) {
        repository.delete(entity);
    }
    
    public void insertAll(List<DflRoundInfo> entities) {
        repository.saveAll(entities);
    }
    
    public void updateAll(List<DflRoundInfo> entities) {
        repository.saveAll(entities);
    }
    
    public void replaceAll(List<DflRoundInfo> entities) {
        repository.deleteAll();
        repository.flush();
        repository.saveAll(entities);
    }
    
    public void refresh(DflRoundInfo entity) {
        // No-op: Spring manages persistence context
    }
    
    public void close() {
        // No-op: Spring manages lifecycle
    }
    
    public DflRoundInfo findByRound(int round) {
        return repository.findByRound(round);
    }

    public void insertAll(List<DflRoundInfo> entities, boolean inTx) {
        // inTx parameter ignored - Spring manages transactions via @Transactional
        repository.saveAll(entities);
    }

    public void updateAll(List<DflRoundInfo> entities, boolean inTx) {
        // inTx parameter ignored - Spring manages transactions via @Transactional
        repository.saveAll(entities);
    }

    public List<DflRoundInfo> getRoundsByAflRounds(List<Integer> aflRounds) {
        return repository.findRoundsByAflRounds(aflRounds);
    }
}
