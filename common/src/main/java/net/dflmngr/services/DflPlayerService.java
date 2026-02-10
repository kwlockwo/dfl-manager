package net.dflmngr.services;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import net.dflmngr.model.entity.DflPlayer;
import net.dflmngr.repositories.DflPlayerRepository;

@Service
public class DflPlayerService {

    private final DflPlayerRepository repository;

    public DflPlayerService(DflPlayerRepository repository) {
        this.repository = repository;
    }

    public DflPlayer getByAflPlayerId(String aflPlayerId) {
        return repository.findByAflPlayerId(aflPlayerId);
    }

    public List<DflPlayer> getAdamGoodesEligible() {
        return repository.findAdamGoodesEligible();
    }

    public Map<String, DflPlayer> getCrossRefPlayers() {
        Map<String, DflPlayer> crossRefPlayers = new HashMap<>();
        List<DflPlayer> allPlayers = repository.findAll();

        for(DflPlayer player : allPlayers) {
            String crossRefId = ((player.getFirstName() + player.getLastName()).replaceAll("[^a-zA-Z]", "") + "-" + player.getAflClub()).toLowerCase();
            crossRefPlayers.put(crossRefId, player);
        }

        return crossRefPlayers;
    }

    @Transactional
    public void bulkUpdateAflPlayerId(Map<String, DflPlayer> entities) {
        for(Map.Entry<String, DflPlayer> entry : entities.entrySet()) {
            String aflPlayerId = entry.getKey();
            DflPlayer player = entry.getValue();
            player.setAflPlayerId(aflPlayerId);
        }
        // Changes are automatically persisted at end of transaction
    }

    public List<DflPlayer> getByTeam(String team) {
        return repository.findByAflClub(team);
    }

    // Generic repository methods
    public DflPlayer get(Integer id) {
        return repository.findById(id).orElse(null);
    }

    public List<DflPlayer> findAll() {
        return repository.findAll();
    }

    @Transactional
    public void insert(DflPlayer entity) {
        repository.save(entity);
    }

    @Transactional
    public void update(DflPlayer entity) {
        repository.save(entity);
    }

    @Transactional
    public void insertAll(List<DflPlayer> entities) {
        repository.saveAll(entities);
    }

    @Transactional
    public void updateAll(List<DflPlayer> entities) {
        repository.saveAll(entities);
    }

    @Transactional
    public void delete(DflPlayer entity) {
        repository.delete(entity);
    }

    @Transactional
    public void replaceAll(List<DflPlayer> entities) {
        repository.deleteAll();
        repository.flush();
        repository.saveAll(entities);
    }
}
