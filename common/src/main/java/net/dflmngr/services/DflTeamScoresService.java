package net.dflmngr.services;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import net.dflmngr.model.entity.DflTeamScores;
import net.dflmngr.model.entity.keys.DflTeamScoresPK;
import net.dflmngr.repositories.DflTeamScoresRepository;

@Service
public class DflTeamScoresService {

    private final DflTeamScoresRepository repository;

    public DflTeamScoresService(DflTeamScoresRepository repository) {
        this.repository = repository;
    }

    public List<DflTeamScores> getForRound(int round) {
        return repository.findByRound(round);
    }

    public Map<String, DflTeamScores> getForRoundWithKey(int round) {
        Map<String, DflTeamScores> teamScoresWithKey = new HashMap<>();
        List<DflTeamScores> scores = getForRound(round);

        for(DflTeamScores teamScore : scores) {
            teamScoresWithKey.put(teamScore.getTeamCode(), teamScore);
        }

        return teamScoresWithKey;
    }

    @Transactional
    public void replaceAllForRound(int round, List<DflTeamScores> teamScores) {
        List<DflTeamScores> existingScores = getForRound(round);
        for(DflTeamScores scores : existingScores) {
            repository.delete(scores);
        }

        repository.flush();
        repository.saveAll(teamScores);
    }

    // Generic repository methods
    public DflTeamScores get(DflTeamScoresPK id) {
        return repository.findById(id).orElse(null);
    }

    public List<DflTeamScores> findAll() {
        return repository.findAll();
    }

    @Transactional
    public void insert(DflTeamScores entity) {
        repository.save(entity);
    }

    @Transactional
    public void update(DflTeamScores entity) {
        repository.save(entity);
    }

    @Transactional
    public void insertAll(List<DflTeamScores> entities) {
        repository.saveAll(entities);
    }

    @Transactional
    public void updateAll(List<DflTeamScores> entities) {
        repository.saveAll(entities);
    }

    @Transactional
    public void delete(DflTeamScores entity) {
        repository.delete(entity);
    }

    @Transactional
    public void replaceAll(List<DflTeamScores> entities) {
        repository.deleteAll();
        repository.flush();
        repository.saveAll(entities);
    }
}
