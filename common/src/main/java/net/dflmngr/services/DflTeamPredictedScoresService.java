package net.dflmngr.services;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import net.dflmngr.model.entity.DflTeamPredictedScores;
import net.dflmngr.model.entity.keys.DflTeamPredictedScoresPK;
import net.dflmngr.repositories.DflTeamPredictedScoresRepository;

@Service
public class DflTeamPredictedScoresService {

    private final DflTeamPredictedScoresRepository repository;

    public DflTeamPredictedScoresService(DflTeamPredictedScoresRepository repository) {
        this.repository = repository;
    }

    // Business logic methods
    public DflTeamPredictedScores getTeamPredictedScoreForRound(String teamCode, int round) {
        DflTeamPredictedScoresPK pk = new DflTeamPredictedScoresPK();
        pk.setRound(round);
        pk.setTeamCode(teamCode);
        DflTeamPredictedScores teamPredictedScore = get(pk);

        if (teamPredictedScore == null || teamPredictedScore.getPredictedScore() == 0) {
            teamPredictedScore = new DflTeamPredictedScores();
            teamPredictedScore.setRound(round);
            teamPredictedScore.setTeamCode(teamCode);
            teamPredictedScore.setPredictedScore(550);
        }

        return teamPredictedScore;
    }

    public DflTeamPredictedScores getTeamPredictedScoreForRoundNoDefault(String teamCode, int round) {
        DflTeamPredictedScoresPK pk = new DflTeamPredictedScoresPK();
        pk.setRound(round);
        pk.setTeamCode(teamCode);
        return get(pk);
    }

    public List<DflTeamPredictedScores> getForRound(int round) {
        return repository.findByRound(round);
    }

    public Map<String, DflTeamPredictedScores> getForRoundWithKey(int round) {
        Map<String, DflTeamPredictedScores> teamPredictedScoresWithKey = new HashMap<>();
        List<DflTeamPredictedScores> predictedScores = getForRound(round);

        for (DflTeamPredictedScores teamScore : predictedScores) {
            teamPredictedScoresWithKey.put(teamScore.getTeamCode(), teamScore);
        }

        return teamPredictedScoresWithKey;
    }

    public List<DflTeamPredictedScores> getAllForRound(int round) {
        return repository.findByRound(round);
    }

    @Transactional
    public void replaceTeamForRound(int round, String teamCode, DflTeamPredictedScores predictedScore) {
        repository.deleteByRoundAndTeamCode(round, teamCode);
        repository.flush();
        repository.save(predictedScore);
    }

    @Transactional
    public void replaceAllForRound(int round, List<DflTeamPredictedScores> predictedScores) {
        repository.deleteByRound(round);
        repository.flush();
        repository.saveAll(predictedScores);
    }

    // Generic repository methods
    public DflTeamPredictedScores get(DflTeamPredictedScoresPK id) {
        return repository.findById(id).orElse(null);
    }

    public List<DflTeamPredictedScores> findAll() {
        return repository.findAll();
    }

    @Transactional
    public void insert(DflTeamPredictedScores entity) {
        repository.save(entity);
    }

    @Transactional
    public void update(DflTeamPredictedScores entity) {
        repository.save(entity);
    }

    @Transactional
    public void insertAll(List<DflTeamPredictedScores> entities) {
        repository.saveAll(entities);
    }

    @Transactional
    public void updateAll(List<DflTeamPredictedScores> entities) {
        repository.saveAll(entities);
    }

    @Transactional
    public void delete(DflTeamPredictedScores entity) {
        repository.delete(entity);
    }

    @Transactional
    public void replaceAll(List<DflTeamPredictedScores> entities) {
        repository.deleteAll();
        repository.flush();
        repository.saveAll(entities);
    }
}
