package net.dflmngr.model.service.impl.spring;

import net.dflmngr.model.entity.DflPlayerPredictedScores;
import net.dflmngr.model.entity.keys.DflPlayerPredictedScoresPK;
import net.dflmngr.model.service.DflPlayerPredictedScoresService;
import net.dflmngr.repositories.DflPlayerPredictedScoresRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class DflPlayerPredictedScoresServiceImpl implements DflPlayerPredictedScoresService {
    
    private final DflPlayerPredictedScoresRepository repository;
    
    public DflPlayerPredictedScoresServiceImpl(DflPlayerPredictedScoresRepository repository) {
        this.repository = repository;
    }
    
    @Override
    public DflPlayerPredictedScores get(DflPlayerPredictedScoresPK id) {
        return repository.findById(id).orElse(null);
    }
    
    @Override
    public List<DflPlayerPredictedScores> findAll() {
        return repository.findAll();
    }
    
    @Override
    public void insert(DflPlayerPredictedScores entity) {
        repository.save(entity);
    }
    
    @Override
    public void update(DflPlayerPredictedScores entity) {
        repository.save(entity);
    }
    
    @Override
    public void delete(DflPlayerPredictedScores entity) {
        repository.delete(entity);
    }
    
    @Override
    public void insertAll(List<DflPlayerPredictedScores> entities) {
        repository.saveAll(entities);
    }
    
    @Override
    public void updateAll(List<DflPlayerPredictedScores> entities) {
        repository.saveAll(entities);
    }
    
    @Override
    public void replaceAll(List<DflPlayerPredictedScores> entities) {
        repository.deleteAll();
        repository.flush();
        repository.saveAll(entities);
    }
    
    @Override
    public void refresh(DflPlayerPredictedScores entity) {
        // No-op: Spring manages persistence context
    }
    
    @Override
    public void close() {
        // No-op: Spring manages lifecycle
    }
    
    @Override
    public List<DflPlayerPredictedScores> findByRound(int round) {
        return repository.findByRound(round);
    }
    
    @Override
    public DflPlayerPredictedScores findByRoundAndPlayer(int round, int playerId) {
        return repository.findByRoundAndPlayerId(round, playerId);
    }
}
