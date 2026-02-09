package net.dflmngr.model.service.impl.spring;

import net.dflmngr.model.entity.DflEarlyInsAndOuts;
import net.dflmngr.model.service.DflEarlyInsAndOutsService;
import net.dflmngr.repositories.DflEarlyInsAndOutsRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class DflEarlyInsAndOutsServiceImpl implements DflEarlyInsAndOutsService {
    
    private final DflEarlyInsAndOutsRepository repository;
    
    public DflEarlyInsAndOutsServiceImpl(DflEarlyInsAndOutsRepository repository) {
        this.repository = repository;
    }
    
    public DflEarlyInsAndOuts get(Integer id) {
        return repository.findById(id).orElse(null);
    }
    
    public List<DflEarlyInsAndOuts> findAll() {
        return repository.findAll();
    }
    
    public void insert(DflEarlyInsAndOuts entity) {
        repository.save(entity);
    }
    
    public void update(DflEarlyInsAndOuts entity) {
        repository.save(entity);
    }
    
    public void delete(DflEarlyInsAndOuts entity) {
        repository.delete(entity);
    }
    
    public void insertAll(List<DflEarlyInsAndOuts> entities) {
        repository.saveAll(entities);
    }
    
    public void updateAll(List<DflEarlyInsAndOuts> entities) {
        repository.saveAll(entities);
    }
    
    public void replaceAll(List<DflEarlyInsAndOuts> entities) {
        repository.deleteAll();
        repository.flush();
        repository.saveAll(entities);
    }
    
    public void refresh(DflEarlyInsAndOuts entity) {
        // No-op: Spring manages persistence context
    }
    
    public void close() {
        // No-op: Spring manages lifecycle
    }
    
    public List<DflEarlyInsAndOuts> findByRound(int round) {
        return repository.findByRound(round);
    }
    
    public List<DflEarlyInsAndOuts> findByRoundAndTeam(int round, String teamCode) {
        return repository.findByRoundAndTeamCode(round, teamCode);
    }
    
    public DflEarlyInsAndOuts findByRoundAndTeamAndPlayer(int round, String teamCode, int playerId) {
        return repository.findByRoundAndTeamCodeAndPlayerId(round, teamCode, playerId);
    }

    public void insertAll(List<DflEarlyInsAndOuts> entities, boolean inTx) {
        // inTx parameter ignored - Spring manages transactions via @Transactional
        repository.saveAll(entities);
    }

    public void updateAll(List<DflEarlyInsAndOuts> entities, boolean inTx) {
        // inTx parameter ignored - Spring manages transactions via @Transactional
        repository.saveAll(entities);
    }

    public List<DflEarlyInsAndOuts> getByTeamAndRound(int round, String teamCode) {
        return repository.findByRoundAndTeamCode(round, teamCode);
    }

    public void saveTeamInsAndOuts(List<DflEarlyInsAndOuts> insAndOuts) {
        repository.saveAll(insAndOuts);
    }
}
