package net.dflmngr.model.service.impl.spring;

import net.dflmngr.model.entity.AflTeam;
import net.dflmngr.model.service.AflTeamService;
import net.dflmngr.repositories.AflTeamRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class AflTeamServiceImpl implements AflTeamService {
    
    private final AflTeamRepository repository;
    
    public AflTeamServiceImpl(AflTeamRepository repository) {
        this.repository = repository;
    }
    
    public AflTeam get(String id) {
        return repository.findById(id).orElse(null);
    }
    
    public List<AflTeam> findAll() {
        return repository.findAll();
    }
    
    public void insert(AflTeam entity) {
        repository.save(entity);
    }
    
    public void update(AflTeam entity) {
        repository.save(entity);
    }
    
    public void delete(AflTeam entity) {
        repository.delete(entity);
    }
    
    public void insertAll(List<AflTeam> entities) {
        repository.saveAll(entities);
    }
    
    public void updateAll(List<AflTeam> entities) {
        repository.saveAll(entities);
    }
    
    public void replaceAll(List<AflTeam> entities) {
        repository.deleteAll();
        repository.flush();
        repository.saveAll(entities);
    }
    
    public void refresh(AflTeam entity) {
        // No-op: Spring manages persistence context
    }
    
    public void close() {
        // No-op: Spring manages lifecycle
    }
    
    public AflTeam findByTeamId(String teamId) {
        return repository.findByName(teamId).orElse(null);
    }

    public AflTeam getAflTeamByName(String name) {
        return repository.findByName(name).orElse(null);
    }

    public void insertAll(List<AflTeam> entities, boolean inTx) {
        // inTx parameter ignored - Spring manages transactions via @Transactional
        repository.saveAll(entities);
    }

    public void updateAll(List<AflTeam> entities, boolean inTx) {
        // inTx parameter ignored - Spring manages transactions via @Transactional
        repository.saveAll(entities);
    }
}
