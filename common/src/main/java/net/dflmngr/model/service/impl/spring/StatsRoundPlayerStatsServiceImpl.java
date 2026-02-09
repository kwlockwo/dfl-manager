package net.dflmngr.model.service.impl.spring;

import net.dflmngr.model.entity.StatsRoundPlayerStats;
import net.dflmngr.model.entity.keys.StatsRoundPlayerStatsPK;
import net.dflmngr.model.service.StatsRoundPlayerStatsService;
import net.dflmngr.repositories.StatsRoundPlayerStatsRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class StatsRoundPlayerStatsServiceImpl implements StatsRoundPlayerStatsService {
    
    private final StatsRoundPlayerStatsRepository repository;
    
    public StatsRoundPlayerStatsServiceImpl(StatsRoundPlayerStatsRepository repository) {
        this.repository = repository;
    }
    
    public StatsRoundPlayerStats get(StatsRoundPlayerStatsPK id) {
        return repository.findById(id).orElse(null);
    }
    
    public List<StatsRoundPlayerStats> findAll() {
        return repository.findAll();
    }
    
    public void insert(StatsRoundPlayerStats entity) {
        repository.save(entity);
    }
    
    public void update(StatsRoundPlayerStats entity) {
        repository.save(entity);
    }
    
    public void delete(StatsRoundPlayerStats entity) {
        repository.delete(entity);
    }
    
    public void insertAll(List<StatsRoundPlayerStats> entities) {
        repository.saveAll(entities);
    }
    
    public void updateAll(List<StatsRoundPlayerStats> entities) {
        repository.saveAll(entities);
    }
    
    public void replaceAll(List<StatsRoundPlayerStats> entities) {
        repository.deleteAll();
        repository.flush();
        repository.saveAll(entities);
    }
    
    public void refresh(StatsRoundPlayerStats entity) {
        // No-op: Spring manages persistence context
    }
    
    public void close() {
        // No-op: Spring manages lifecycle
    }
    
    public List<StatsRoundPlayerStats> findByRound(int round) {
        return repository.findByRound(round);
    }
    
    public StatsRoundPlayerStats findByRoundAndPlayer(int round, int playerId) {
        return repository.findByRoundAndPlayerId(round, playerId);
    }

    public void insertAll(List<StatsRoundPlayerStats> entities, boolean inTx) {
        // inTx parameter ignored - Spring manages transactions via @Transactional
        repository.saveAll(entities);
    }

    public void updateAll(List<StatsRoundPlayerStats> entities, boolean inTx) {
        // inTx parameter ignored - Spring manages transactions via @Transactional
        repository.saveAll(entities);
    }
}
