package net.dflmngr.model.service.impl.spring;

import net.dflmngr.model.entity.AflFixture;
import net.dflmngr.model.entity.keys.AflFixturePK;
import net.dflmngr.model.service.AflFixtureService;
import net.dflmngr.repositories.AflFixtureRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@Transactional
public class AflFixtureServiceImpl implements AflFixtureService {
    
    private final AflFixtureRepository repository;
    
    public AflFixtureServiceImpl(AflFixtureRepository repository) {
        this.repository = repository;
    }
    
    public AflFixture get(AflFixturePK id) {
        return repository.findById(id).orElse(null);
    }
    
    public List<AflFixture> findAll() {
        return repository.findAll();
    }
    
    public void insert(AflFixture entity) {
        repository.save(entity);
    }
    
    public void update(AflFixture entity) {
        repository.save(entity);
    }
    
    public void delete(AflFixture entity) {
        repository.delete(entity);
    }
    
    public void insertAll(List<AflFixture> entities) {
        repository.saveAll(entities);
    }
    
    public void updateAll(List<AflFixture> entities) {
        repository.saveAll(entities);
    }
    
    public void replaceAll(List<AflFixture> entities) {
        repository.deleteAll();
        repository.flush();
        repository.saveAll(entities);
    }
    
    public void refresh(AflFixture entity) {
        // No-op: Spring manages persistence context
    }
    
    public void close() {
        // No-op: Spring manages lifecycle
    }
    
    public List<AflFixture> findByRound(int round) {
        return repository.findByRound(round);
    }
    
    public AflFixture findByRoundAndGame(int round, int game) {
        return repository.findByRoundAndGame(round, game);
    }

    public void insertAll(List<AflFixture> entities, boolean inTx) {
        // inTx parameter ignored - Spring manages transactions via @Transactional
        repository.saveAll(entities);
    }

    public void updateAll(List<AflFixture> entities, boolean inTx) {
        // inTx parameter ignored - Spring manages transactions via @Transactional
        repository.saveAll(entities);
    }

    public List<AflFixture> getAflFixturesForRound(int round) {
        return repository.findByRound(round);
    }

    public AflFixture getAflFixtureForRoundAndTeam(int round, String team) {
        return repository.findByRoundAndTeam(round, team);
    }

    public Map<Integer, List<AflFixture>> getAflFixturneRoundBlocks() {
        List<AflFixture> allFixtures = repository.findAll();
        Map<Integer, List<AflFixture>> fixtureBlocks = new HashMap<>();
        for (AflFixture fixture : allFixtures) {
            int round = fixture.getRound();
            fixtureBlocks.computeIfAbsent(round, k -> new ArrayList<>()).add(fixture);
        }
        return fixtureBlocks;
    }

    public List<AflFixture> getAflFixturesPlayedForRound(int round) {
        List<AflFixture> fixtures = repository.findByRound(round);
        return fixtures.stream()
                .filter(f -> f.getEndTime() != null)
                .collect(Collectors.toList());
    }

    public AflFixture getPlayedGame(int round, int game) {
        AflFixture fixture = repository.findByRoundAndGame(round, game);
        if (fixture != null && fixture.getEndTime() != null) {
            return fixture;
        }
        return null;
    }

    public List<String> getAflTeamsPlayedForRound(int round) {
        List<AflFixture> fixtures = getAflFixturesPlayedForRound(round);
        List<String> teams = new ArrayList<>();
        for (AflFixture fixture : fixtures) {
            teams.add(fixture.getHomeTeam());
            teams.add(fixture.getAwayTeam());
        }
        return teams;
    }

    public List<AflFixture> getIncompleteFixtures() {
        return repository.findIncompleteFixtures(ZonedDateTime.now());
    }

    public List<AflFixture> getFixturesToScrape() {
        return repository.findFixturesToScrape(ZonedDateTime.now());
    }

    public List<Integer> getAflRoundsToScrape() {
        List<AflFixture> fixtures = getFixturesToScrape();
        return fixtures.stream()
                .map(AflFixture::getRound)
                .distinct()
                .collect(Collectors.toList());
    }

    public boolean getAflRoundComplete(int round) {
        List<AflFixture> incompleteFixtures = repository.findIncompleteFixturesForRound(round);
        return incompleteFixtures.isEmpty();
    }

    public void updateLoadedFixtures(List<AflFixture> updatedFixtures) {
        repository.saveAll(updatedFixtures);
    }

    public int getMaxAflRound() {
        Integer maxRound = repository.findMaxRound();
        return maxRound != null ? maxRound : 0;
    }

    public int getRefreshFixtureStart() {
        Integer refreshStart = repository.findRefreshFixtureStart();
        return refreshStart != null ? refreshStart : 0;
    }
}
