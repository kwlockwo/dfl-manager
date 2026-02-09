package net.dflmngr.model.service.impl.spring;

import net.dflmngr.model.entity.DflTeamPredictedScores;
import net.dflmngr.model.entity.keys.DflTeamPredictedScoresPK;
import net.dflmngr.model.service.DflTeamPredictedScoresService;
import net.dflmngr.repositories.DflTeamPredictedScoresRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class DflTeamPredictedScoresServiceImpl implements DflTeamPredictedScoresService {
    
    private final DflTeamPredictedScoresRepository repository;
    
    public DflTeamPredictedScoresServiceImpl(DflTeamPredictedScoresRepository repository) {
        this.repository = repository;
    }
    
    @Override
    public DflTeamPredictedScores get(DflTeamPredictedScoresPK id) {
        return repository.findById(id).orElse(null);
    }
    
    @Override
    public List<DflTeamPredictedScores> findAll() {
        return repository.findAll();
    }
    
    @Override
    public void insert(DflTeamPredictedScores entity) {
        repository.save(entity);
    }
    
    @Override
    public void update(DflTeamPredictedScores entity) {
        repository.save(entity);
    }
    
    @Override
    public void delete(DflTeamPredictedScores entity) {
        repository.delete(entity);
    }
    
    @Override
    public void insertAll(List<DflTeamPredictedScores> entities) {
        repository.saveAll(entities);
    }
    
    @Override
    public void updateAll(List<DflTeamPredictedScores> entities) {
        repository.saveAll(entities);
    }
    
    @Override
    public void replaceAll(List<DflTeamPredictedScores> entities) {
        repository.deleteAll();
        repository.flush();
        repository.saveAll(entities);
    }
    
    @Override
    public void refresh(DflTeamPredictedScores entity) {
        // No-op: Spring manages persistence context
    }
    
    @Override
    public void close() {
        // No-op: Spring manages lifecycle
    }
    
    @Override
    public List<DflTeamPredictedScores> findByRound(int round) {
        return repository.findByRound(round);
    }
    
    @Override
    public DflTeamPredictedScores findByRoundAndTeam(int round, String teamCode) {
        return repository.findByRoundAndTeamCode(round, teamCode);
    }
}
