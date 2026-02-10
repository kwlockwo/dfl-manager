package net.dflmngr.model.service.impl.spring;

import net.dflmngr.model.entity.DflTeam;
import net.dflmngr.model.service.DflTeamService;
import net.dflmngr.repositories.DflTeamRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class DflTeamServiceImpl implements DflTeamService {
    
    private final DflTeamRepository repository;
    
    public DflTeamServiceImpl(DflTeamRepository repository) {
        this.repository = repository;
    }
    
    public DflTeam get(String id) {
        return repository.findById(id).orElse(null);
    }
    
    public List<DflTeam> findAll() {
        return repository.findAll();
    }
    
    public void insert(DflTeam entity) {
        repository.save(entity);
    }
    
    public void update(DflTeam entity) {
        repository.save(entity);
    }
    
    public void delete(DflTeam entity) {
        repository.delete(entity);
    }
    
    public void insertAll(List<DflTeam> entities) {
        repository.saveAll(entities);
    }
    
    public void updateAll(List<DflTeam> entities) {
        repository.saveAll(entities);
    }
    
    public void replaceAll(List<DflTeam> entities) {
        repository.deleteAll();
        repository.flush();
        repository.saveAll(entities);
    }
    
    public void refresh(DflTeam entity) {
        // No-op: Spring manages persistence context
    }
    
    public void close() {
        // No-op: Spring manages lifecycle
    }
    
    public DflTeam findByTeamCode(String teamCode) {
        return repository.findByTeamCode(teamCode);
    }

    public void insertAll(List<DflTeam> entities, boolean inTx) {
        // inTx parameter ignored - Spring manages transactions via @Transactional
        repository.saveAll(entities);
    }

    public void updateAll(List<DflTeam> entities, boolean inTx) {
        // inTx parameter ignored - Spring manages transactions via @Transactional
        repository.saveAll(entities);
    }
}
