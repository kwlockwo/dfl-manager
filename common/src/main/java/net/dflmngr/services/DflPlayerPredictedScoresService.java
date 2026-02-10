package net.dflmngr.services;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import net.dflmngr.model.entity.DflPlayerPredictedScores;
import net.dflmngr.model.entity.keys.DflPlayerPredictedScoresPK;
import net.dflmngr.repositories.DflPlayerPredictedScoresRepository;

@Service
public class DflPlayerPredictedScoresService {

    private final DflPlayerPredictedScoresRepository repository;

    public DflPlayerPredictedScoresService(DflPlayerPredictedScoresRepository repository) {
        this.repository = repository;
    }

    public List<DflPlayerPredictedScores> getForRound(int round) {
        return repository.findByRound(round);
    }

    public Map<Integer, DflPlayerPredictedScores> getForRoundWithKey(int round) {
        Map<Integer, DflPlayerPredictedScores> playerPredictedScoresWithKey = new HashMap<>();
        List<DflPlayerPredictedScores> scores = getForRound(round);

        for(DflPlayerPredictedScores playerScore : scores) {
            playerPredictedScoresWithKey.put(playerScore.getPlayerId(), playerScore);
        }

        return playerPredictedScoresWithKey;
    }

    @Transactional
    public void replaceAllForRound(int round, List<DflPlayerPredictedScores> predictedScores) {
        List<DflPlayerPredictedScores> existingPredictions = getForRound(round);
        for(DflPlayerPredictedScores prediction : existingPredictions) {
            repository.delete(prediction);
        }

        repository.flush();
        repository.saveAll(predictedScores);
    }

    // Generic repository methods
    public DflPlayerPredictedScores get(DflPlayerPredictedScoresPK id) {
        return repository.findById(id).orElse(null);
    }

    public List<DflPlayerPredictedScores> findAll() {
        return repository.findAll();
    }

    @Transactional
    public void insert(DflPlayerPredictedScores entity) {
        repository.save(entity);
    }

    @Transactional
    public void update(DflPlayerPredictedScores entity) {
        repository.save(entity);
    }

    @Transactional
    public void insertAll(List<DflPlayerPredictedScores> entities) {
        repository.saveAll(entities);
    }

    @Transactional
    public void updateAll(List<DflPlayerPredictedScores> entities) {
        repository.saveAll(entities);
    }

    @Transactional
    public void delete(DflPlayerPredictedScores entity) {
        repository.delete(entity);
    }

    @Transactional
    public void replaceAll(List<DflPlayerPredictedScores> entities) {
        repository.deleteAll();
        repository.flush();
        repository.saveAll(entities);
    }
}
