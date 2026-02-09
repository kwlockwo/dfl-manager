package net.dflmngr.model.service.impl.spring;

import net.dflmngr.model.entity.DflPlayerScores;
import net.dflmngr.model.entity.keys.DflPlayerScoresPK;
import net.dflmngr.model.service.DflPlayerScoresService;
import net.dflmngr.repositories.DflPlayerScoresRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class DflPlayerScoresServiceImpl implements DflPlayerScoresService {
    
    private final DflPlayerScoresRepository repository;
    
    public DflPlayerScoresServiceImpl(DflPlayerScoresRepository repository) {
        this.repository = repository;
    }
    
    @Override
    public DflPlayerScores get(DflPlayerScoresPK id) {
        return repository.findById(id).orElse(null);
    }
    
    @Override
    public List<DflPlayerScores> findAll() {
        return repository.findAll();
    }
    
    @Override
    public void insert(DflPlayerScores entity) {
        repository.save(entity);
    }
    
    @Override
    public void update(DflPlayerScores entity) {
        repository.save(entity);
    }
    
    @Override
    public void delete(DflPlayerScores entity) {
        repository.delete(entity);
    }
    
    @Override
    public void insertAll(List<DflPlayerScores> entities) {
        repository.saveAll(entities);
    }
    
    @Override
    public void updateAll(List<DflPlayerScores> entities) {
        repository.saveAll(entities);
    }
    
    @Override
    public void replaceAll(List<DflPlayerScores> entities) {
        repository.deleteAll();
        repository.flush();
        repository.saveAll(entities);
    }
    
    @Override
    public void refresh(DflPlayerScores entity) {
        // No-op: Spring manages persistence context
    }
    
    @Override
    public void close() {
        // No-op: Spring manages lifecycle
    }
    
    @Override
    public List<DflPlayerScores> findByRound(int round) {
        return repository.findByRound(round);
    }
    
    @Override
    public DflPlayerScores findByRoundAndPlayer(int round, int playerId) {
        return repository.findByRoundAndPlayerId(round, playerId);
    }
}
