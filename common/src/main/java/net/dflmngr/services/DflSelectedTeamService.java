package net.dflmngr.services;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import net.dflmngr.model.entity.DflSelectedPlayer;
import net.dflmngr.model.entity.keys.DflSelectedPlayerPK;
import net.dflmngr.repositories.DflSelectedPlayerRepository;

@Service
public class DflSelectedTeamService {

    private final DflSelectedPlayerRepository repository;

    public DflSelectedTeamService(DflSelectedPlayerRepository repository) {
        this.repository = repository;
    }

    public List<DflSelectedPlayer> getAllForRound(int round) {
        return repository.findByRound(round);
    }

    public List<DflSelectedPlayer> getSelectedTeamForRound(int round, String teamCode) {
        return repository.findByRoundAndTeamCode(round, teamCode);
    }

    @Transactional
    public void replaceAllForRound(int round, List<DflSelectedPlayer> selectedTeam) {
        List<DflSelectedPlayer> existingTeam = getAllForRound(round);

        for(DflSelectedPlayer stats : existingTeam) {
            repository.delete(stats);
        }

        repository.flush();
        repository.saveAll(selectedTeam);
    }

    @Transactional
    public void replaceTeamForRound(int round, String teamCode, List<DflSelectedPlayer> selectedTeam) {
        List<DflSelectedPlayer> existingTeam = getSelectedTeamForRound(round, teamCode);

        for(DflSelectedPlayer stats : existingTeam) {
            repository.delete(stats);
        }

        repository.flush();
        repository.saveAll(selectedTeam);
    }

    public Map<Integer, DflSelectedPlayer> getForRoundWithKey(int round) {
        Map<Integer, DflSelectedPlayer> selectedPlayersWithKey = new HashMap<>();
        List<DflSelectedPlayer> selectedPlayers = getAllForRound(round);

        for(DflSelectedPlayer player : selectedPlayers) {
            selectedPlayersWithKey.put(player.getPlayerId(), player);
        }

        return selectedPlayersWithKey;
    }

    // Generic repository methods
    public DflSelectedPlayer get(DflSelectedPlayerPK id) {
        return repository.findById(id).orElse(null);
    }

    public List<DflSelectedPlayer> findAll() {
        return repository.findAll();
    }

    @Transactional
    public void insert(DflSelectedPlayer entity) {
        repository.save(entity);
    }

    @Transactional
    public void update(DflSelectedPlayer entity) {
        repository.save(entity);
    }

    @Transactional
    public void insertAll(List<DflSelectedPlayer> entities) {
        repository.saveAll(entities);
    }

    @Transactional
    public void updateAll(List<DflSelectedPlayer> entities) {
        repository.saveAll(entities);
    }

    @Transactional
    public void delete(DflSelectedPlayer entity) {
        repository.delete(entity);
    }

    @Transactional
    public void replaceAll(List<DflSelectedPlayer> entities) {
        repository.deleteAll();
        repository.flush();
        repository.saveAll(entities);
    }
}
