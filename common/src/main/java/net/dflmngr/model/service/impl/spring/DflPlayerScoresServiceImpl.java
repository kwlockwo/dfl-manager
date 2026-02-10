package net.dflmngr.model.service.impl.spring;

import net.dflmngr.model.entity.DflPlayerScores;
import net.dflmngr.model.entity.keys.DflPlayerScoresPK;
import net.dflmngr.model.service.DflPlayerScoresService;
import net.dflmngr.repositories.DflPlayerScoresRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Transactional
public class DflPlayerScoresServiceImpl implements DflPlayerScoresService {
    
    private final DflPlayerScoresRepository repository;
    
    public DflPlayerScoresServiceImpl(DflPlayerScoresRepository repository) {
        this.repository = repository;
    }
    
    public DflPlayerScores get(DflPlayerScoresPK id) {
        return repository.findById(id).orElse(null);
    }
    
    public List<DflPlayerScores> findAll() {
        return repository.findAll();
    }
    
    public void insert(DflPlayerScores entity) {
        repository.save(entity);
    }
    
    public void update(DflPlayerScores entity) {
        repository.save(entity);
    }
    
    public void delete(DflPlayerScores entity) {
        repository.delete(entity);
    }
    
    public void insertAll(List<DflPlayerScores> entities) {
        repository.saveAll(entities);
    }
    
    public void updateAll(List<DflPlayerScores> entities) {
        repository.saveAll(entities);
    }
    
    public void replaceAll(List<DflPlayerScores> entities) {
        repository.deleteAll();
        repository.flush();
        repository.saveAll(entities);
    }
    
    public void refresh(DflPlayerScores entity) {
        // No-op: Spring manages persistence context
    }
    
    public void close() {
        // No-op: Spring manages lifecycle
    }
    
    public List<DflPlayerScores> findByRound(int round) {
        return repository.findByRound(round);
    }
    
    public DflPlayerScores findByRoundAndPlayer(int round, int playerId) {
        return repository.findByRoundAndPlayerId(round, playerId);
    }

    public void insertAll(List<DflPlayerScores> entities, boolean inTx) {
        // inTx parameter ignored - Spring manages transactions via @Transactional
        repository.saveAll(entities);
    }

    public void updateAll(List<DflPlayerScores> entities, boolean inTx) {
        // inTx parameter ignored - Spring manages transactions via @Transactional
        repository.saveAll(entities);
    }

    public List<DflPlayerScores> getForRound(int round) {
        return repository.findByRound(round);
    }

    public List<DflPlayerScores> getForRoundAndTeam(int round, String teamCode) {
        return repository.findByRoundAndTeamCode(round, teamCode);
    }

    public Map<Integer, DflPlayerScores> getForRoundWithKey(int round) {
        List<DflPlayerScores> playerScores = repository.findByRound(round);
        Map<Integer, DflPlayerScores> playerScoresMap = new HashMap<>();
        for (DflPlayerScores scores : playerScores) {
            playerScoresMap.put(scores.getPlayerId(), scores);
        }
        return playerScoresMap;
    }

    public Map<Integer, DflPlayerScores> getForRoundAndTeamWithKey(int round, String teamCode) {
        List<DflPlayerScores> playerScores = repository.findByRoundAndTeamCode(round, teamCode);
        Map<Integer, DflPlayerScores> playerScoresMap = new HashMap<>();
        for (DflPlayerScores scores : playerScores) {
            playerScoresMap.put(scores.getPlayerId(), scores);
        }
        return playerScoresMap;
    }

    public void replaceAllForRound(int round, List<DflPlayerScores> playerScores) {
        repository.deleteByRound(round);
        repository.flush();
        repository.saveAll(playerScores);
    }

    public Map<Integer, List<DflPlayerScores>> getAllWithKey() {
        List<DflPlayerScores> allScores = repository.findAll();
        Map<Integer, List<DflPlayerScores>> scoresMap = new HashMap<>();
        for (DflPlayerScores scores : allScores) {
            int playerId = scores.getPlayerId();
            scoresMap.computeIfAbsent(playerId, k -> new ArrayList<>()).add(scores);
        }
        return scoresMap;
    }

    public Map<Integer, List<DflPlayerScores>> getUptoRoundWithKey(int round) {
        List<DflPlayerScores> allScores = repository.findAll();
        Map<Integer, List<DflPlayerScores>> scoresMap = new HashMap<>();
        for (DflPlayerScores scores : allScores) {
            if (scores.getRound() <= round) {
                int playerId = scores.getPlayerId();
                scoresMap.computeIfAbsent(playerId, k -> new ArrayList<>()).add(scores);
            }
        }
        return scoresMap;
    }
}
