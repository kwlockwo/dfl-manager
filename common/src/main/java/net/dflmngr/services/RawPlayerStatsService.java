package net.dflmngr.services;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import net.dflmngr.model.entity.RawPlayerStats;
import net.dflmngr.model.entity.keys.RawPlayerStatsPK;
import net.dflmngr.repositories.RawPlayerStatsRepository;

@Service
public class RawPlayerStatsService {

    private final RawPlayerStatsRepository repository;

    public RawPlayerStatsService(RawPlayerStatsRepository repository) {
        this.repository = repository;
    }

    public List<RawPlayerStats> getForRound(int round) {
        return repository.findByRound(round);
    }

    public Map<String, RawPlayerStats> getForRoundWithKey(int round) {
        Map<String, RawPlayerStats> playerStatsWithKey = new HashMap<>();

        List<RawPlayerStats> playerStats = repository.findByRound(round);

        for(RawPlayerStats stats : playerStats) {
            String key = stats.getTeam() + stats.getJumperNo();
            playerStatsWithKey.put(key, stats);
        }

        return playerStatsWithKey;
    }

    @Transactional
    public void replaceAllForRound(int round, List<RawPlayerStats> playerStats) {
        List<RawPlayerStats> existingStats = getForRound(round);
        for(RawPlayerStats stats : existingStats) {
            repository.delete(stats);
        }

        repository.flush();
        repository.saveAll(playerStats);
    }

    @Transactional
    public void removeStatsForRoundAndTeam(int round, String team) {
        List<RawPlayerStats> existingStats = getForRoundAndTeam(round, team);
        for(RawPlayerStats stats : existingStats) {
            repository.delete(stats);
        }
    }

    public List<RawPlayerStats> getForRoundAndTeam(int round, String team) {
        return repository.findByRoundAndTeam(round, team);
    }

    // Generic repository methods
    public RawPlayerStats get(RawPlayerStatsPK id) {
        return repository.findById(id).orElse(null);
    }

    public List<RawPlayerStats> findAll() {
        return repository.findAll();
    }

    @Transactional
    public void insert(RawPlayerStats entity) {
        repository.save(entity);
    }

    @Transactional
    public void update(RawPlayerStats entity) {
        repository.save(entity);
    }

    @Transactional
    public void insertAll(List<RawPlayerStats> entities) {
        repository.saveAll(entities);
    }

    @Transactional
    public void updateAll(List<RawPlayerStats> entities) {
        repository.saveAll(entities);
    }

    @Transactional
    public void delete(RawPlayerStats entity) {
        repository.delete(entity);
    }

    @Transactional
    public void replaceAll(List<RawPlayerStats> entities) {
        repository.deleteAll();
        repository.flush();
        repository.saveAll(entities);
    }
}
