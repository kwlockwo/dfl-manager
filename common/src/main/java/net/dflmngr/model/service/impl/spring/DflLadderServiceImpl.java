package net.dflmngr.model.service.impl.spring;

import net.dflmngr.model.entity.DflLadder;
import net.dflmngr.model.service.DflLadderService;
import net.dflmngr.repositories.DflLadderRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class DflLadderServiceImpl implements DflLadderService {
    
    private final DflLadderRepository repository;
    
    public DflLadderServiceImpl(DflLadderRepository repository) {
        this.repository = repository;
    }
    
    @Override
    public DflLadder get(String id) {
        return repository.findById(id).orElse(null);
    }
    
    @Override
    public List<DflLadder> findAll() {
        return repository.findAll();
    }
    
    @Override
    public void insert(DflLadder entity) {
        repository.save(entity);
    }
    
    @Override
    public void update(DflLadder entity) {
        repository.save(entity);
    }
    
    @Override
    public void delete(DflLadder entity) {
        repository.delete(entity);
    }
    
    @Override
    public void insertAll(List<DflLadder> entities) {
        repository.saveAll(entities);
    }
    
    @Override
    public void updateAll(List<DflLadder> entities) {
        repository.saveAll(entities);
    }
    
    @Override
    public void replaceAll(List<DflLadder> entities) {
        repository.deleteAll();
        repository.flush();
        repository.saveAll(entities);
    }
    
    @Override
    public void refresh(DflLadder entity) {
        // No-op: Spring manages persistence context
    }
    
    @Override
    public void close() {
        // No-op: Spring manages lifecycle
    }
    
    @Override
    public DflLadder findByTeamCode(String teamCode) {
        return repository.findByTeamCode(teamCode);
    }
}
