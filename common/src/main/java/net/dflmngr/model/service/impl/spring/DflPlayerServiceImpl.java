package net.dflmngr.model.service.impl.spring;

import net.dflmngr.model.entity.DflPlayer;
import net.dflmngr.model.service.DflPlayerService;
import net.dflmngr.repositories.DflPlayerRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Transactional
public class DflPlayerServiceImpl implements DflPlayerService {
    
    private final DflPlayerRepository repository;
    
    public DflPlayerServiceImpl(DflPlayerRepository repository) {
        this.repository = repository;
    }
    
    public DflPlayer get(Integer id) {
        return repository.findById(id).orElse(null);
    }
    
    public List<DflPlayer> findAll() {
        return repository.findAll();
    }
    
    public void insert(DflPlayer entity) {
        repository.save(entity);
    }
    
    public void update(DflPlayer entity) {
        repository.save(entity);
    }
    
    public void delete(DflPlayer entity) {
        repository.delete(entity);
    }
    
    public void insertAll(List<DflPlayer> entities) {
        repository.saveAll(entities);
    }
    
    public void updateAll(List<DflPlayer> entities) {
        repository.saveAll(entities);
    }
    
    public void replaceAll(List<DflPlayer> entities) {
        repository.deleteAll();
        repository.flush();
        repository.saveAll(entities);
    }
    
    public void refresh(DflPlayer entity) {
        // No-op: Spring manages persistence context
    }
    
    public void close() {
        // No-op: Spring manages lifecycle
    }
    
    public DflPlayer findByPlayerId(int playerId) {
        return repository.findById(playerId).orElse(null);
    }

    public List<DflPlayer> getByTeam(String team) {
        // Find by AFL club/team
        return repository.findByAflClub(team);
    }

    public void insertAll(List<DflPlayer> entities, boolean inTx) {
        // inTx parameter ignored - Spring manages transactions via @Transactional
        repository.saveAll(entities);
    }

    public void updateAll(List<DflPlayer> entities, boolean inTx) {
        // inTx parameter ignored - Spring manages transactions via @Transactional
        repository.saveAll(entities);
    }

    public DflPlayer getByAflPlayerId(String aflPlayerId) {
        return repository.findByAflPlayerId(aflPlayerId);
    }

    public List<DflPlayer> getAdamGoodesEligible() {
        return repository.findAdamGoodesEligible();
    }

    public Map<String, DflPlayer> getCrossRefPlayers() {
        Map<String, DflPlayer> crossRefPlayers = new HashMap<>();
        List<DflPlayer> players = repository.findWithAflPlayerId();
        
        for (DflPlayer player : players) {
            crossRefPlayers.put(player.getAflPlayerId(), player);
        }
        
        return crossRefPlayers;
    }

    public void bulkUpdateAflPlayerId(Map<String, DflPlayer> players) {
        for (Map.Entry<String, DflPlayer> entry : players.entrySet()) {
            String aflPlayerId = entry.getKey();
            DflPlayer player = entry.getValue();
            player.setAflPlayerId(aflPlayerId);
        }
        repository.saveAll(players.values());
    }
}
