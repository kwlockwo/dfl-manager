package net.dflmngr.model.service.impl.spring;

import net.dflmngr.model.entity.RawPlayerStats;
import net.dflmngr.model.entity.keys.RawPlayerStatsPK;
import net.dflmngr.model.service.RawPlayerStatsService;
import net.dflmngr.repositories.RawPlayerStatsRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Transactional
public class RawPlayerStatsServiceImpl implements RawPlayerStatsService {
    
    private final RawPlayerStatsRepository repository;
    
    public RawPlayerStatsServiceImpl(RawPlayerStatsRepository repository) {
        this.repository = repository;
    }
    
    public RawPlayerStats get(RawPlayerStatsPK id) {
        return repository.findById(id).orElse(null);
    }
    
    public List<RawPlayerStats> findAll() {
        return repository.findAll();
    }
    
    public void insert(RawPlayerStats entity) {
        repository.save(entity);
    }
    
    public void update(RawPlayerStats entity) {
        repository.save(entity);
    }
    
    public void delete(RawPlayerStats entity) {
        repository.delete(entity);
    }
    
    public void insertAll(List<RawPlayerStats> entities) {
        repository.saveAll(entities);
    }
    
    public void updateAll(List<RawPlayerStats> entities) {
        repository.saveAll(entities);
    }
    
    public void replaceAll(List<RawPlayerStats> entities) {
        repository.deleteAll();
        repository.flush();
        repository.saveAll(entities);
    }
    
    public void refresh(RawPlayerStats entity) {
        // No-op: Spring manages persistence context
    }
    
    public void close() {
        // No-op: Spring manages lifecycle
    }
    
    public List<RawPlayerStats> findByRound(int round) {
        return repository.findByRound(round);
    }
    
    public RawPlayerStats findByRoundAndPlayer(int round, int playerId) {
        return repository.findByRoundAndPlayerId(round, playerId);
    }

    public void insertAll(List<RawPlayerStats> entities, boolean inTx) {
        // inTx parameter ignored - Spring manages transactions via @Transactional
        repository.saveAll(entities);
    }

    public void updateAll(List<RawPlayerStats> entities, boolean inTx) {
        // inTx parameter ignored - Spring manages transactions via @Transactional
        repository.saveAll(entities);
    }

    public List<RawPlayerStats> getForRound(int round) {
        return repository.findByRound(round);
    }

    public void replaceAllForRound(int round, List<RawPlayerStats> playerStats) {
        repository.deleteByRound(round);
        repository.flush();
        repository.saveAll(playerStats);
    }

    public Map<String, RawPlayerStats> getForRoundWithKey(int round) {
        List<RawPlayerStats> stats = repository.findByRound(round);
        Map<String, RawPlayerStats> statsMap = new HashMap<>();
        for (RawPlayerStats stat : stats) {
            String key = stat.getTeam() + "_" + stat.getJumperNo();
            statsMap.put(key, stat);
        }
        return statsMap;
    }

    public void removeStatsForRoundAndTeam(int round, String team) {
        repository.deleteByRoundAndTeam(round, team);
    }

    public List<RawPlayerStats> getForRoundAndTeam(int round, String team) {
        return repository.findByRoundAndTeam(round, team);
    }
}
