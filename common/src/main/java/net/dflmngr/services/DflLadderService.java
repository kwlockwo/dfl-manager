package net.dflmngr.services;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import net.dflmngr.model.entity.DflLadder;
import net.dflmngr.model.entity.keys.DflLadderPK;
import net.dflmngr.repositories.DflLadderRepository;

@Service
public class DflLadderService {

    private final DflLadderRepository repository;

    public DflLadderService(DflLadderRepository repository) {
        this.repository = repository;
    }

    public List<DflLadder> getLadderForRound(int round) {
        return repository.findByRound(round);
    }

    public List<DflLadder> getPreviousRoundLadder(int round) {
        return getLadderForRound(round - 1);
    }

    public Map<String, DflLadder> getForRoundWithKey(int round) {
        Map<String, DflLadder> ladderMap = new HashMap<>();

        List<DflLadder> ladder = getLadderForRound(round);

        for(DflLadder team : ladder) {
            ladderMap.put(team.getTeamCode(), team);
        }

        return ladderMap;
    }

    public Map<String, DflLadder> getForPeviousRoundWithKey(int round) {
        Map<String, DflLadder> ladderMap = new HashMap<>();

        List<DflLadder> ladder = getPreviousRoundLadder(round);

        for(DflLadder team : ladder) {
            ladderMap.put(team.getTeamCode(), team);
        }

        return ladderMap;
    }

    @Transactional
    public void replaceAllForRound(int round, List<DflLadder> ladder) {
        List<DflLadder> existingLadder = getLadderForRound(round);
        for(DflLadder team : existingLadder) {
            repository.delete(team);
        }

        repository.flush();
        repository.saveAll(ladder);
    }

    // Generic repository methods
    public DflLadder get(DflLadderPK id) {
        return repository.findById(id).orElse(null);
    }

    public List<DflLadder> findAll() {
        return repository.findAll();
    }

    @Transactional
    public void insert(DflLadder entity) {
        repository.save(entity);
    }

    @Transactional
    public void update(DflLadder entity) {
        repository.save(entity);
    }

    @Transactional
    public void insertAll(List<DflLadder> entities) {
        repository.saveAll(entities);
    }

    @Transactional
    public void updateAll(List<DflLadder> entities) {
        repository.saveAll(entities);
    }

    @Transactional
    public void delete(DflLadder entity) {
        repository.delete(entity);
    }

    @Transactional
    public void replaceAll(List<DflLadder> entities) {
        repository.deleteAll();
        repository.flush();
        repository.saveAll(entities);
    }
}
