package net.dflmngr.model.service.impl.spring;

import net.dflmngr.model.entity.DflTeamScores;
import net.dflmngr.model.entity.keys.DflTeamScoresPK;
import net.dflmngr.model.service.DflTeamScoresService;
import net.dflmngr.repositories.DflTeamScoresRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Transactional
public class DflTeamScoresServiceImpl implements DflTeamScoresService {
    
    private final DflTeamScoresRepository repository;
    
    public DflTeamScoresServiceImpl(DflTeamScoresRepository repository) {
        this.repository = repository;
    }
    
    public DflTeamScores get(DflTeamScoresPK id) {
        return repository.findById(id).orElse(null);
    }
    
    public List<DflTeamScores> findAll() {
        return repository.findAll();
    }
    
    public void insert(DflTeamScores entity) {
        repository.save(entity);
    }
    
    public void update(DflTeamScores entity) {
        repository.save(entity);
    }
    
    public void delete(DflTeamScores entity) {
        repository.delete(entity);
    }
    
    public void insertAll(List<DflTeamScores> entities) {
        repository.saveAll(entities);
    }
    
    public void updateAll(List<DflTeamScores> entities) {
        repository.saveAll(entities);
    }
    
    public void replaceAll(List<DflTeamScores> entities) {
        repository.deleteAll();
        repository.flush();
        repository.saveAll(entities);
    }
    
    public void refresh(DflTeamScores entity) {
        // No-op: Spring manages persistence context
    }
    
    public void close() {
        // No-op: Spring manages lifecycle
    }
    
    public List<DflTeamScores> findByRound(int round) {
        return repository.findByRound(round);
    }
    
    public DflTeamScores findByRoundAndTeam(int round, String teamCode) {
        return repository.findByRoundAndTeamCode(round, teamCode);
    }

    public void insertAll(List<DflTeamScores> entities, boolean inTx) {
        // inTx parameter ignored - Spring manages transactions via @Transactional
        repository.saveAll(entities);
    }

    public void updateAll(List<DflTeamScores> entities, boolean inTx) {
        // inTx parameter ignored - Spring manages transactions via @Transactional
        repository.saveAll(entities);
    }

    public List<DflTeamScores> getForRound(int round) {
        return repository.findByRound(round);
    }

    public Map<String, DflTeamScores> getForRoundWithKey(int round) {
        List<DflTeamScores> teamScores = repository.findByRound(round);
        Map<String, DflTeamScores> teamScoresMap = new HashMap<>();
        for (DflTeamScores scores : teamScores) {
            teamScoresMap.put(scores.getTeamCode(), scores);
        }
        return teamScoresMap;
    }

    public void replaceAllForRound(int round, List<DflTeamScores> teamScores) {
        repository.deleteByRound(round);
        repository.flush();
        repository.saveAll(teamScores);
    }
}
