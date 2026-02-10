package net.dflmngr.services;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import net.dflmngr.model.entity.DflTeamPlayer;
import net.dflmngr.repositories.DflTeamPlayerRepository;

@Service
public class DflTeamPlayerService {

    private final DflTeamPlayerRepository repository;

    public DflTeamPlayerService(DflTeamPlayerRepository repository) {
        this.repository = repository;
    }

    public DflTeamPlayer getTeamPlayerForTeam(String teamCode, int teamPlayerId) {
        return repository.findByTeamCodeAndTeamPlayerId(teamCode, teamPlayerId);
    }

    // Generic repository methods
    public DflTeamPlayer get(Integer id) {
        return repository.findById(id).orElse(null);
    }

    public List<DflTeamPlayer> findAll() {
        return repository.findAll();
    }

    @Transactional
    public void insert(DflTeamPlayer entity) {
        repository.save(entity);
    }

    @Transactional
    public void update(DflTeamPlayer entity) {
        repository.save(entity);
    }

    @Transactional
    public void insertAll(List<DflTeamPlayer> entities) {
        repository.saveAll(entities);
    }

    @Transactional
    public void updateAll(List<DflTeamPlayer> entities) {
        repository.saveAll(entities);
    }

    @Transactional
    public void delete(DflTeamPlayer entity) {
        repository.delete(entity);
    }

    @Transactional
    public void replaceAll(List<DflTeamPlayer> entities) {
        repository.deleteAll();
        repository.flush();
        repository.saveAll(entities);
    }
}
