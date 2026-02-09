package net.dflmngr.model.service.impl.spring;

import net.dflmngr.model.entity.DflTeamPlayer;
import net.dflmngr.model.service.DflTeamPlayerService;
import net.dflmngr.repositories.DflTeamPlayerRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class DflTeamPlayerServiceImpl implements DflTeamPlayerService {
    
    private final DflTeamPlayerRepository repository;
    
    public DflTeamPlayerServiceImpl(DflTeamPlayerRepository repository) {
        this.repository = repository;
    }
    
    public DflTeamPlayer get(Integer id) {
        return repository.findById(id).orElse(null);
    }
    
    public List<DflTeamPlayer> findAll() {
        return repository.findAll();
    }
    
    public void insert(DflTeamPlayer entity) {
        repository.save(entity);
    }
    
    public void update(DflTeamPlayer entity) {
        repository.save(entity);
    }
    
    public void delete(DflTeamPlayer entity) {
        repository.delete(entity);
    }
    
    public void insertAll(List<DflTeamPlayer> entities) {
        repository.saveAll(entities);
    }
    
    public void updateAll(List<DflTeamPlayer> entities) {
        repository.saveAll(entities);
    }
    
    public void replaceAll(List<DflTeamPlayer> entities) {
        repository.deleteAll();
        repository.flush();
        repository.saveAll(entities);
    }
    
    public void refresh(DflTeamPlayer entity) {
        // No-op: Spring manages persistence context
    }
    
    public void close() {
        // No-op: Spring manages lifecycle
    }
    
    public List<DflTeamPlayer> findForTeam(String teamCode) {
        return repository.findByTeamCode(teamCode);
    }
    
    public DflTeamPlayer findForTeamAndPlayer(String teamCode, int playerId) {
        return repository.findByTeamCodeAndPlayerId(teamCode, playerId);
    }

    public void insertAll(List<DflTeamPlayer> entities, boolean inTx) {
        // inTx parameter ignored - Spring manages transactions via @Transactional
        repository.saveAll(entities);
    }

    public void updateAll(List<DflTeamPlayer> entities, boolean inTx) {
        // inTx parameter ignored - Spring manages transactions via @Transactional
        repository.saveAll(entities);
    }

    public DflTeamPlayer getTeamPlayerForTeam(String teamCode, int teamPlayerId) {
        return repository.findByTeamCodeAndTeamPlayerId(teamCode, teamPlayerId);
    }
}
