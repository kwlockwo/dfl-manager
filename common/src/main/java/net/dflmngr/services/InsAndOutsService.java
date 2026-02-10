package net.dflmngr.services;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import net.dflmngr.model.entity.InsAndOuts;
import net.dflmngr.repositories.InsAndOutsRepository;

@Service
public class InsAndOutsService {

    private final InsAndOutsRepository repository;

    public InsAndOutsService(InsAndOutsRepository repository) {
        this.repository = repository;
    }

    @Transactional
    public void saveTeamInsAndOuts(List<InsAndOuts> insAndOuts) {
        int round = insAndOuts.get(0).getRound();
        String teamCode = insAndOuts.get(0).getTeamCode();

        List<InsAndOuts> existingInsAndOuts = repository.findByRoundAndTeamCode(round, teamCode);

        if(!existingInsAndOuts.isEmpty()) {
            for(InsAndOuts delete : existingInsAndOuts) {
                repository.delete(delete);
            }
        }

        repository.flush();

        for(InsAndOuts insert : insAndOuts) {
            repository.save(insert);
        }
    }

    public List<InsAndOuts> getByTeamAndRound(int round, String teamCode) {
        return repository.findByRoundAndTeamCode(round, teamCode);
    }

    @Transactional
    public void removeForRound(int round) {
        List<InsAndOuts> existingInsAndOuts = repository.findByRound(round);

        if(!existingInsAndOuts.isEmpty()) {
            for(InsAndOuts delete : existingInsAndOuts) {
                repository.delete(delete);
            }
        }
    }

    // Generic repository methods
    public InsAndOuts get(Integer id) {
        return repository.findById(id).orElse(null);
    }

    public List<InsAndOuts> findAll() {
        return repository.findAll();
    }

    @Transactional
    public void insert(InsAndOuts entity) {
        repository.save(entity);
    }

    @Transactional
    public void update(InsAndOuts entity) {
        repository.save(entity);
    }

    @Transactional
    public void insertAll(List<InsAndOuts> entities) {
        repository.saveAll(entities);
    }

    @Transactional
    public void updateAll(List<InsAndOuts> entities) {
        repository.saveAll(entities);
    }

    @Transactional
    public void delete(InsAndOuts entity) {
        repository.delete(entity);
    }

    @Transactional
    public void replaceAll(List<InsAndOuts> entities) {
        repository.deleteAll();
        repository.flush();
        repository.saveAll(entities);
    }
}
