package net.dflmngr.services;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import net.dflmngr.model.entity.DflEarlyInsAndOuts;
import net.dflmngr.repositories.DflEarlyInsAndOutsRepository;

@Service
public class DflEarlyInsAndOutsService {

    private final DflEarlyInsAndOutsRepository repository;

    public DflEarlyInsAndOutsService(DflEarlyInsAndOutsRepository repository) {
        this.repository = repository;
    }

    public List<DflEarlyInsAndOuts> getByTeamAndRound(int round, String teamCode) {
        return repository.findByRoundAndTeamCode(round, teamCode);
    }

    @Transactional
    public void saveTeamInsAndOuts(List<DflEarlyInsAndOuts> earlyInsAndOuts) {
        int round = earlyInsAndOuts.get(0).getRound();
        String teamCode = earlyInsAndOuts.get(0).getTeamCode();

        List<DflEarlyInsAndOuts> existingInsAndOuts = repository.findByRoundAndTeamCode(round, teamCode);

        if(existingInsAndOuts.size() > 0) {
            for(DflEarlyInsAndOuts delete : existingInsAndOuts) {
                repository.delete(delete);
            }
        }

        repository.flush();

        for(DflEarlyInsAndOuts insert : earlyInsAndOuts) {
            repository.save(insert);
        }
    }

    // Generic repository methods
    public DflEarlyInsAndOuts get(Integer id) {
        return repository.findById(id).orElse(null);
    }

    public List<DflEarlyInsAndOuts> findAll() {
        return repository.findAll();
    }

    @Transactional
    public void insert(DflEarlyInsAndOuts entity) {
        repository.save(entity);
    }

    @Transactional
    public void update(DflEarlyInsAndOuts entity) {
        repository.save(entity);
    }

    @Transactional
    public void insertAll(List<DflEarlyInsAndOuts> entities) {
        repository.saveAll(entities);
    }

    @Transactional
    public void updateAll(List<DflEarlyInsAndOuts> entities) {
        repository.saveAll(entities);
    }

    @Transactional
    public void delete(DflEarlyInsAndOuts entity) {
        repository.delete(entity);
    }

    @Transactional
    public void replaceAll(List<DflEarlyInsAndOuts> entities) {
        repository.deleteAll();
        repository.flush();
        repository.saveAll(entities);
    }
}
