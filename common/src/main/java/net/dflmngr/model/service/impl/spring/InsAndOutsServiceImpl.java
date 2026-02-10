package net.dflmngr.model.service.impl.spring;

import net.dflmngr.model.entity.InsAndOuts;
import net.dflmngr.model.service.InsAndOutsService;
import net.dflmngr.repositories.InsAndOutsRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class InsAndOutsServiceImpl implements InsAndOutsService {
    
    private final InsAndOutsRepository repository;
    
    public InsAndOutsServiceImpl(InsAndOutsRepository repository) {
        this.repository = repository;
    }
    
    public InsAndOuts get(Integer id) {
        return repository.findById(id).orElse(null);
    }
    
    public List<InsAndOuts> findAll() {
        return repository.findAll();
    }
    
    public void insert(InsAndOuts entity) {
        repository.save(entity);
    }
    
    public void update(InsAndOuts entity) {
        repository.save(entity);
    }
    
    public void delete(InsAndOuts entity) {
        repository.delete(entity);
    }
    
    public void insertAll(List<InsAndOuts> entities) {
        repository.saveAll(entities);
    }
    
    public void updateAll(List<InsAndOuts> entities) {
        repository.saveAll(entities);
    }
    
    public void replaceAll(List<InsAndOuts> entities) {
        repository.deleteAll();
        repository.flush();
        repository.saveAll(entities);
    }
    
    public void refresh(InsAndOuts entity) {
        // No-op: Spring manages persistence context
    }
    
    public void close() {
        // No-op: Spring manages lifecycle
    }
    
    public List<InsAndOuts> findByRound(int round) {
        return repository.findByRound(round);
    }
    
    public List<InsAndOuts> findByRoundAndTeam(int round, String teamCode) {
        return repository.findByRoundAndTeamCode(round, teamCode);
    }
    
    public InsAndOuts findByRoundAndTeamAndPlayer(int round, String teamCode, int playerId) {
        return repository.findByRoundAndTeamCodeAndPlayerId(round, teamCode, playerId);
    }

    public void saveTeamInsAndOuts(List<InsAndOuts> insAndOuts) {
        repository.saveAll(insAndOuts);
    }

    public List<InsAndOuts> getByTeamAndRound(int round, String teamCode) {
        return repository.findByRoundAndTeamCode(round, teamCode);
    }

    public void removeForRound(int round) {
        repository.deleteByRound(round);
    }

    public void insertAll(List<InsAndOuts> entities, boolean inTx) {
        // inTx parameter ignored - Spring manages transactions via @Transactional
        repository.saveAll(entities);
    }

    public void updateAll(List<InsAndOuts> entities, boolean inTx) {
        // inTx parameter ignored - Spring manages transactions via @Transactional
        repository.saveAll(entities);
    }
}
