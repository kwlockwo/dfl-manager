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

    @Override
    public List<StatsRoundPlayerStats> getForRound(int round) {
        return repository.findByRound(round);
    }

    @Override
    public void replaceAllForRound(int round, List<StatsRoundPlayerStats> playerStats) {
        // Delete all stats for the round, then save the new ones
        repository.findByRound(round).forEach(repository::delete);
        repository.flush();
        repository.saveAll(playerStats);
    }

    @Override
    public java.util.Map<String, StatsRoundPlayerStats> getForRoundWithKey(int round) {
        List<StatsRoundPlayerStats> stats = repository.findByRound(round);
        java.util.Map<String, StatsRoundPlayerStats> statsMap = new java.util.HashMap<>();
        for (StatsRoundPlayerStats stat : stats) {
            // Using name as key since this entity doesn't have a playerId field
            statsMap.put(stat.getName(), stat);
        }
        return statsMap;
    }

    @Override
    public void removeStatsForRoundAndTeam(int round, String team) {
        repository.deleteByRoundAndTeam(round, team);
    }

    @Override
    public List<StatsRoundPlayerStats> getForRoundAndTeam(int round, String team) {
        return repository.findByRoundAndTeam(round, team);
    }
}
