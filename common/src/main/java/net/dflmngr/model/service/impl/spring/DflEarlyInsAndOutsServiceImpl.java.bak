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
    
    @Override
    public DflEarlyInsAndOuts get(Integer id) {
        return repository.findById(id).orElse(null);
    }
    
    @Override
    public List<DflEarlyInsAndOuts> findAll() {
        return repository.findAll();
    }
    
    @Override
    public void insert(DflEarlyInsAndOuts entity) {
        repository.save(entity);
    }
    
    @Override
    public void update(DflEarlyInsAndOuts entity) {
        repository.save(entity);
    }
    
    @Override
    public void delete(DflEarlyInsAndOuts entity) {
        repository.delete(entity);
    }
    
    @Override
    public void insertAll(List<DflEarlyInsAndOuts> entities) {
        repository.saveAll(entities);
    }
    
    @Override
    public void updateAll(List<DflEarlyInsAndOuts> entities) {
        repository.saveAll(entities);
    }
    
    @Override
    public void replaceAll(List<DflEarlyInsAndOuts> entities) {
        repository.deleteAll();
        repository.flush();
        repository.saveAll(entities);
    }
    
    @Override
    public void refresh(DflEarlyInsAndOuts entity) {
        // No-op: Spring manages persistence context
    }
    
    @Override
    public void close() {
        // No-op: Spring manages lifecycle
    }
    
    @Override
    public List<DflEarlyInsAndOuts> findByRound(int round) {
        return repository.findByRound(round);
    }
    
    @Override
    public List<DflEarlyInsAndOuts> findByRoundAndTeam(int round, String teamCode) {
        return repository.findByRoundAndTeamCode(round, teamCode);
    }
    
    @Override
    public DflEarlyInsAndOuts findByRoundAndTeamAndPlayer(int round, String teamCode, int playerId) {
        return repository.findByRoundAndTeamCodeAndPlayerId(round, teamCode, playerId);
    }
}
