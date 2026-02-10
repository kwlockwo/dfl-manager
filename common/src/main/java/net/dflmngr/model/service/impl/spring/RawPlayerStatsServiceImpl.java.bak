package net.dflmngr.model.service.impl.spring;

import net.dflmngr.model.entity.RawPlayerStats;
import net.dflmngr.model.entity.keys.RawPlayerStatsPK;
import net.dflmngr.model.service.RawPlayerStatsService;
import net.dflmngr.repositories.RawPlayerStatsRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class RawPlayerStatsServiceImpl implements RawPlayerStatsService {
    
    private final RawPlayerStatsRepository repository;
    
    public RawPlayerStatsServiceImpl(RawPlayerStatsRepository repository) {
        this.repository = repository;
    }
    
    @Override
    public RawPlayerStats get(RawPlayerStatsPK id) {
        return repository.findById(id).orElse(null);
    }
    
    @Override
    public List<RawPlayerStats> findAll() {
        return repository.findAll();
    }
    
    @Override
    public void insert(RawPlayerStats entity) {
        repository.save(entity);
    }
    
    @Override
    public void update(RawPlayerStats entity) {
        repository.save(entity);
    }
    
    @Override
    public void delete(RawPlayerStats entity) {
        repository.delete(entity);
    }
    
    @Override
    public void insertAll(List<RawPlayerStats> entities) {
        repository.saveAll(entities);
    }
    
    @Override
    public void updateAll(List<RawPlayerStats> entities) {
        repository.saveAll(entities);
    }
    
    @Override
    public void replaceAll(List<RawPlayerStats> entities) {
        repository.deleteAll();
        repository.flush();
        repository.saveAll(entities);
    }
    
    @Override
    public void refresh(RawPlayerStats entity) {
        // No-op: Spring manages persistence context
    }
    
    @Override
    public void close() {
        // No-op: Spring manages lifecycle
    }
    
    @Override
    public List<RawPlayerStats> findByRound(int round) {
        return repository.findByRound(round);
    }
    
    @Override
    public RawPlayerStats findByRoundAndPlayer(int round, int playerId) {
        return repository.findByRoundAndPlayerId(round, playerId);
    }
}
