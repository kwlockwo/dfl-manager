package net.dflmngr.model.service.impl.spring;

import net.dflmngr.model.entity.DflTeamPredictedScores;
import net.dflmngr.model.entity.keys.DflTeamPredictedScoresPK;
import net.dflmngr.model.service.DflTeamPredictedScoresService;
import net.dflmngr.repositories.DflTeamPredictedScoresRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Transactional
public class DflTeamPredictedScoresServiceImpl implements DflTeamPredictedScoresService {
    
    private final DflTeamPredictedScoresRepository repository;
    
    public DflTeamPredictedScoresServiceImpl(DflTeamPredictedScoresRepository repository) {
        this.repository = repository;
    }
    
    public DflTeamPredictedScores get(DflTeamPredictedScoresPK id) {
        return repository.findById(id).orElse(null);
    }
    
    public List<DflTeamPredictedScores> findAll() {
        return repository.findAll();
    }
    
    public void insert(DflTeamPredictedScores entity) {
        repository.save(entity);
    }
    
    public void update(DflTeamPredictedScores entity) {
        repository.save(entity);
    }
    
    public void delete(DflTeamPredictedScores entity) {
        repository.delete(entity);
    }
    
    public void insertAll(List<DflTeamPredictedScores> entities) {
        repository.saveAll(entities);
    }
    
    public void updateAll(List<DflTeamPredictedScores> entities) {
        repository.saveAll(entities);
    }
    
    public void replaceAll(List<DflTeamPredictedScores> entities) {
        repository.deleteAll();
        repository.flush();
        repository.saveAll(entities);
    }
    
    public void refresh(DflTeamPredictedScores entity) {
        // No-op: Spring manages persistence context
    }
    
    public void close() {
        // No-op: Spring manages lifecycle
    }
    
    public List<DflTeamPredictedScores> findByRound(int round) {
        return repository.findByRound(round);
    }
    
    public DflTeamPredictedScores findByRoundAndTeam(int round, String teamCode) {
        return repository.findByRoundAndTeamCode(round, teamCode);
    }

    public void insertAll(List<DflTeamPredictedScores> entities, boolean inTx) {
        // inTx parameter ignored - Spring manages transactions via @Transactional
        repository.saveAll(entities);
    }

    public void updateAll(List<DflTeamPredictedScores> entities, boolean inTx) {
        // inTx parameter ignored - Spring manages transactions via @Transactional
        repository.saveAll(entities);
    }

    public DflTeamPredictedScores getTeamPredictedScoreForRound(String teamCode, int round) {
        return repository.findByRoundAndTeamCode(round, teamCode);
    }

    public DflTeamPredictedScores getTeamPredictedScoreForRoundNoDefault(String teamCode, int round) {
        return repository.findByRoundAndTeamCode(round, teamCode);
    }

    public List<DflTeamPredictedScores> getForRound(int round) {
        return repository.findByRound(round);
    }

    public Map<String, DflTeamPredictedScores> getForRoundWithKey(int round) {
        List<DflTeamPredictedScores> predictedScores = repository.findByRound(round);
        Map<String, DflTeamPredictedScores> predictedScoresMap = new HashMap<>();
        for (DflTeamPredictedScores scores : predictedScores) {
            predictedScoresMap.put(scores.getTeamCode(), scores);
        }
        return predictedScoresMap;
    }

    public List<DflTeamPredictedScores> getAllForRound(int round) {
        return repository.findByRound(round);
    }

    public void replaceTeamForRound(int round, String teamCode, DflTeamPredictedScores predictedScore) {
        repository.deleteByRoundAndTeamCode(round, teamCode);
        repository.flush();
        repository.save(predictedScore);
    }

    public void replaceAllForRound(int round, List<DflTeamPredictedScores> predictedScores) {
        repository.deleteByRound(round);
        repository.flush();
        repository.saveAll(predictedScores);
    }
}
