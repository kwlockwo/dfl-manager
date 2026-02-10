package net.dflmngr.model.service.impl.spring;

import net.dflmngr.model.entity.DflLadder;
import net.dflmngr.model.entity.keys.DflLadderPK;
import net.dflmngr.model.service.DflLadderService;
import net.dflmngr.repositories.DflLadderRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Transactional
public class DflLadderServiceImpl implements DflLadderService {
    
    private final DflLadderRepository repository;
    
    public DflLadderServiceImpl(DflLadderRepository repository) {
        this.repository = repository;
    }
    
    public DflLadder get(DflLadderPK id) {
        return repository.findById(id).orElse(null);
    }
    
    public List<DflLadder> findAll() {
        return repository.findAll();
    }
    
    public void insert(DflLadder entity) {
        repository.save(entity);
    }
    
    public void update(DflLadder entity) {
        repository.save(entity);
    }
    
    public void delete(DflLadder entity) {
        repository.delete(entity);
    }
    
    public void insertAll(List<DflLadder> entities) {
        repository.saveAll(entities);
    }
    
    public void updateAll(List<DflLadder> entities) {
        repository.saveAll(entities);
    }
    
    public void replaceAll(List<DflLadder> entities) {
        repository.deleteAll();
        repository.flush();
        repository.saveAll(entities);
    }
    
    public void refresh(DflLadder entity) {
        // No-op: Spring manages persistence context
    }
    
    public void close() {
        // No-op: Spring manages lifecycle
    }
    
    public DflLadder findByTeamCode(String teamCode) {
        throw new UnsupportedOperationException("findByTeamCode not yet implemented");
    }

    public void replaceAllForRound(int round, List<DflLadder> ladder) {
        // Delete all ladder entries for the specified round, then save the new ones
        repository.deleteByRound(round);
        repository.flush();
        repository.saveAll(ladder);
    }

    public void insertAll(List<DflLadder> entities, boolean inTx) {
        // inTx parameter ignored - Spring manages transactions via @Transactional
        repository.saveAll(entities);
    }

    public void updateAll(List<DflLadder> entities, boolean inTx) {
        // inTx parameter ignored - Spring manages transactions via @Transactional
        repository.saveAll(entities);
    }

    public List<DflLadder> getLadderForRound(int round) {
        return repository.findByRound(round);
    }

    public List<DflLadder> getPreviousRoundLadder(int round) {
        return repository.findByRound(round - 1);
    }

    public Map<String, DflLadder> getForRoundWithKey(int round) {
        List<DflLadder> ladders = repository.findByRound(round);
        Map<String, DflLadder> ladderMap = new HashMap<>();
        for (DflLadder ladder : ladders) {
            ladderMap.put(ladder.getTeamCode(), ladder);
        }
        return ladderMap;
    }

    public Map<String, DflLadder> getForPeviousRoundWithKey(int round) {
        List<DflLadder> ladders = repository.findByRound(round - 1);
        Map<String, DflLadder> ladderMap = new HashMap<>();
        for (DflLadder ladder : ladders) {
            ladderMap.put(ladder.getTeamCode(), ladder);
        }
        return ladderMap;
    }
}
