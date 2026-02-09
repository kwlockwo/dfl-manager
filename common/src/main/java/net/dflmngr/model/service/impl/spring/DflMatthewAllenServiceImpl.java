package net.dflmngr.model.service.impl.spring;

import net.dflmngr.model.entity.DflMatthewAllen;
import net.dflmngr.model.service.DflMatthewAllenService;
import net.dflmngr.repositories.DflMatthewAllenRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class DflMatthewAllenServiceImpl implements DflMatthewAllenService {
    
    private final DflMatthewAllenRepository repository;
    
    public DflMatthewAllenServiceImpl(DflMatthewAllenRepository repository) {
        this.repository = repository;
    }
    
    public DflMatthewAllen get(Integer id) {
        return repository.findById(id).orElse(null);
    }
    
    public List<DflMatthewAllen> findAll() {
        return repository.findAll();
    }
    
    public void insert(DflMatthewAllen entity) {
        repository.save(entity);
    }
    
    public void update(DflMatthewAllen entity) {
        repository.save(entity);
    }
    
    public void delete(DflMatthewAllen entity) {
        repository.delete(entity);
    }
    
    public void insertAll(List<DflMatthewAllen> entities) {
        repository.saveAll(entities);
    }
    
    public void updateAll(List<DflMatthewAllen> entities) {
        repository.saveAll(entities);
    }
    
    public void replaceAll(List<DflMatthewAllen> entities) {
        repository.deleteAll();
        repository.flush();
        repository.saveAll(entities);
    }
    
    public void refresh(DflMatthewAllen entity) {
        // No-op: Spring manages persistence context
    }
    
    public void close() {
        // No-op: Spring manages lifecycle
    }
    
    public DflMatthewAllen findByPlayerId(int playerId) {
        List<DflMatthewAllen> results = repository.findByPlayerId(playerId);
        return results.isEmpty() ? null : results.get(0);
    }

    public List<DflMatthewAllen> getForRound(int round) {
        return repository.findByRound(round);
    }

    public DflMatthewAllen getLastVotes(int playerId) {
        return repository.findLastVotesByPlayerId(playerId);
    }

    public void replaceAllForRound(int round, List<DflMatthewAllen> votes) {
        repository.deleteByRound(round);
        repository.flush();
        repository.saveAll(votes);
    }

    public void deleteForRound(int round) {
        repository.deleteByRound(round);
    }

    public void insertAll(List<DflMatthewAllen> entities, boolean inTx) {
        // inTx parameter ignored - Spring manages transactions via @Transactional
        repository.saveAll(entities);
    }

    public void updateAll(List<DflMatthewAllen> entities, boolean inTx) {
        // inTx parameter ignored - Spring manages transactions via @Transactional
        repository.saveAll(entities);
    }
}
