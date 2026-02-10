package net.dflmngr.services;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import net.dflmngr.model.entity.StatsRoundPlayerStats;
import net.dflmngr.model.entity.keys.StatsRoundPlayerStatsPK;
import net.dflmngr.repositories.StatsRoundPlayerStatsRepository;

@Service
public class StatsRoundPlayerStatsService {

    private final StatsRoundPlayerStatsRepository repository;

    public StatsRoundPlayerStatsService(StatsRoundPlayerStatsRepository repository) {
        this.repository = repository;
    }

    public List<StatsRoundPlayerStats> getForRound(int round) {
        return repository.findByRound(round);
    }

    public Map<String, StatsRoundPlayerStats> getForRoundWithKey(int round) {
        Map<String, StatsRoundPlayerStats> playerStatsWithKey = new HashMap<>();

        List<StatsRoundPlayerStats> playerStats = repository.findByRound(round);

        for(StatsRoundPlayerStats stats : playerStats) {
            String key = stats.getTeam() + stats.getJumperNo();
            playerStatsWithKey.put(key, stats);
        }

        return playerStatsWithKey;
    }

    @Transactional
    public void replaceAllForRound(int round, List<StatsRoundPlayerStats> playerStats) {
        List<StatsRoundPlayerStats> existingStats = getForRound(round);
        for(StatsRoundPlayerStats stats : existingStats) {
            repository.delete(stats);
        }

        repository.flush();
        repository.saveAll(playerStats);
    }

    @Transactional
    public void removeStatsForRoundAndTeam(int round, String team) {
        List<StatsRoundPlayerStats> existingStats = getForRoundAndTeam(round, team);
        for(StatsRoundPlayerStats stats : existingStats) {
            repository.delete(stats);
        }
    }

    public List<StatsRoundPlayerStats> getForRoundAndTeam(int round, String team) {
        return repository.findByRoundAndTeam(round, team);
    }

    // Generic repository methods
    public StatsRoundPlayerStats get(StatsRoundPlayerStatsPK id) {
        return repository.findById(id).orElse(null);
    }

    public List<StatsRoundPlayerStats> findAll() {
        return repository.findAll();
    }

    @Transactional
    public void insert(StatsRoundPlayerStats entity) {
        repository.save(entity);
    }

    @Transactional
    public void update(StatsRoundPlayerStats entity) {
        repository.save(entity);
    }

    @Transactional
    public void insertAll(List<StatsRoundPlayerStats> entities) {
        repository.saveAll(entities);
    }

    @Transactional
    public void updateAll(List<StatsRoundPlayerStats> entities) {
        repository.saveAll(entities);
    }

    @Transactional
    public void delete(StatsRoundPlayerStats entity) {
        repository.delete(entity);
    }

    @Transactional
    public void replaceAll(List<StatsRoundPlayerStats> entities) {
        repository.deleteAll();
        repository.flush();
        repository.saveAll(entities);
    }
}
