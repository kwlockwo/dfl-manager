package net.dflmngr.services;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import net.dflmngr.model.entity.DflPlayerScores;
import net.dflmngr.model.entity.keys.DflPlayerScoresPK;
import net.dflmngr.repositories.DflPlayerScoresRepository;

@Service
public class DflPlayerScoresService {

    private final DflPlayerScoresRepository repository;

    public DflPlayerScoresService(DflPlayerScoresRepository repository) {
        this.repository = repository;
    }

    public List<DflPlayerScores> getForRound(int round) {
        return repository.findByRound(round);
    }

    public List<DflPlayerScores> getForRoundAndTeam(int round, String teamCode) {
        return repository.findByRoundAndTeamCode(round, teamCode);
    }

    public Map<Integer, DflPlayerScores> getForRoundWithKey(int round) {
        Map<Integer, DflPlayerScores> playerScoresWithKey = new HashMap<>();
        List<DflPlayerScores> scores = getForRound(round);

        for(DflPlayerScores playerScore : scores) {
            playerScoresWithKey.put(playerScore.getPlayerId(), playerScore);
        }

        return playerScoresWithKey;
    }

    public Map<Integer, DflPlayerScores> getForRoundAndTeamWithKey(int round, String teamCode) {
        Map<Integer, DflPlayerScores> playerScoresWithKey = new HashMap<>();
        List<DflPlayerScores> scores = getForRoundAndTeam(round, teamCode);

        for(DflPlayerScores playerScore : scores) {
            playerScoresWithKey.put(playerScore.getPlayerId(), playerScore);
        }

        return playerScoresWithKey;
    }

    @Transactional
    public void replaceAllForRound(int round, List<DflPlayerScores> playerScores) {
        List<DflPlayerScores> existingScores = getForRound(round);
        for(DflPlayerScores scores : existingScores) {
            repository.delete(scores);
        }

        repository.flush();
        repository.saveAll(playerScores);
    }

    public Map<Integer, List<DflPlayerScores>> getAllWithKey() {
        Map<Integer, List<DflPlayerScores>> playerScoresWithKey = new HashMap<>();
        List<DflPlayerScores> scores = repository.findAll();

        for(DflPlayerScores playerScore : scores) {
            List<DflPlayerScores> playerScores = playerScoresWithKey.getOrDefault(playerScore.getPlayerId(), new ArrayList<>());
            playerScores.add(playerScore);
            playerScoresWithKey.put(playerScore.getPlayerId(), playerScores);
        }

        return playerScoresWithKey;
    }

    public Map<Integer, List<DflPlayerScores>> getUptoRoundWithKey(int round) {
        Map<Integer, List<DflPlayerScores>> playerScoresWithKey = new HashMap<>();

        for(int i = 1; i < round; i++) {
            List<DflPlayerScores> scores = getForRound(i);

            for(DflPlayerScores playerScore : scores) {
                List<DflPlayerScores> playerScores = playerScoresWithKey.getOrDefault(playerScore.getPlayerId(), new ArrayList<>());
                playerScores.add(playerScore);
                playerScoresWithKey.put(playerScore.getPlayerId(), playerScores);
            }
        }

        return playerScoresWithKey;
    }

    // Generic repository methods
    public DflPlayerScores get(DflPlayerScoresPK id) {
        return repository.findById(id).orElse(null);
    }

    public List<DflPlayerScores> findAll() {
        return repository.findAll();
    }

    @Transactional
    public void insert(DflPlayerScores entity) {
        repository.save(entity);
    }

    @Transactional
    public void update(DflPlayerScores entity) {
        repository.save(entity);
    }

    @Transactional
    public void insertAll(List<DflPlayerScores> entities) {
        repository.saveAll(entities);
    }

    @Transactional
    public void updateAll(List<DflPlayerScores> entities) {
        repository.saveAll(entities);
    }

    @Transactional
    public void delete(DflPlayerScores entity) {
        repository.delete(entity);
    }

    @Transactional
    public void replaceAll(List<DflPlayerScores> entities) {
        repository.deleteAll();
        repository.flush();
        repository.saveAll(entities);
    }
}
