package net.dflmngr.services;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import net.dflmngr.model.entity.AflFixture;
import net.dflmngr.model.entity.keys.AflFixturePK;
import net.dflmngr.repositories.AflFixtureRepository;
import net.dflmngr.utils.DflmngrUtils;

@Service
public class AflFixtureService {

    private final AflFixtureRepository repository;

    public AflFixtureService(AflFixtureRepository repository) {
        this.repository = repository;
    }

    public List<AflFixture> getAflFixturesForRound(int round) {
        return repository.findByRound(round);
    }

    public Map<Integer, List<AflFixture>> getAflFixturneRoundBlocks() {
        Map<Integer, List<AflFixture>> fxitureRoundBlocks = new HashMap<>();

        List<AflFixture> fullFixture = repository.findAll();

        for(AflFixture fixture : fullFixture) {
            int roundKey = fixture.getRound();
            List<AflFixture> fixtureBlock = fxitureRoundBlocks.getOrDefault(roundKey, new ArrayList<>());
            fixtureBlock.add(fixture);
            fxitureRoundBlocks.put(roundKey, fixtureBlock);
        }

        return fxitureRoundBlocks;
    }

    public AflFixture getAflFixtureForRoundAndTeam(int round, String team) {
        return repository.findByRoundAndTeam(round, team);
    }

    public List<AflFixture> getAflFixturesPlayedForRound(int round) {
        List<AflFixture> playedFixtures = new ArrayList<>();
        List<AflFixture> aflFixtures = repository.findByRound(round);

        for(AflFixture fixture : aflFixtures) {
            if(fixture.getEndTime() != null) {
                playedFixtures.add(fixture);
            }
        }

        return playedFixtures;
    }

    public AflFixture getPlayedGame(int round, int game) {
        AflFixture playedFixture = null;

        AflFixturePK aflFixturePK = new AflFixturePK();
        aflFixturePK.setRound(round);
        aflFixturePK.setGame(game);

        AflFixture fixture = repository.findById(aflFixturePK).orElse(null);

        if(fixture != null && fixture.getEndTime() != null) {
            playedFixture = fixture;
        }

        return playedFixture;
    }

    public List<String> getAflTeamsPlayedForRound(int round) {
        List<String> playedTeams = new ArrayList<>();

        List<AflFixture> playedFixtures = getAflFixturesPlayedForRound(round);

        for(AflFixture fixture : playedFixtures) {
            playedTeams.add(fixture.getHomeTeam());
            playedTeams.add(fixture.getAwayTeam());
        }

        return playedTeams;
    }

    public List<AflFixture> getIncompleteFixtures() {
        ZonedDateTime now = ZonedDateTime.now(ZoneId.of(DflmngrUtils.defaultTimezone));
        return repository.findIncompleteFixtures(now);
    }

    public List<AflFixture> getFixturesToScrape() {
        ZonedDateTime now = ZonedDateTime.now(ZoneId.of(DflmngrUtils.defaultTimezone));
        return repository.findFixturesToScrape(now);
    }

    public List<Integer> getAflRoundsToScrape() {
        List<AflFixture> fixtures = getFixturesToScrape();

        List<Integer> rounds = new ArrayList<>();
        for(AflFixture fixture : fixtures) {
            if(!rounds.contains(fixture.getRound())) {
                rounds.add(fixture.getRound());
            }
        }

        return rounds;
    }

    public boolean getAflRoundComplete(int round) {
        List<AflFixture> incomleteFixtures = repository.findIncompleteFixturesForRound(round);
        return incomleteFixtures == null || incomleteFixtures.isEmpty();
    }

    @Transactional
    public void updateLoadedFixtures(List<AflFixture> updatedFixtures) {
        List<AflFixture> saveFixtures = new ArrayList<>();

        for(AflFixture updatedFixture : updatedFixtures) {
            AflFixturePK aflFixturePK = new AflFixturePK();
            aflFixturePK.setRound(updatedFixture.getRound());
            aflFixturePK.setGame(updatedFixture.getGame());

            AflFixture fixture = repository.findById(aflFixturePK).orElse(null);
            if(fixture == null) {
                fixture = new AflFixture();
                fixture.setRound(updatedFixture.getRound());
                fixture.setGame(updatedFixture.getGame());
            }

            fixture.setHomeTeam(updatedFixture.getHomeTeam());
            fixture.setAwayTeam(updatedFixture.getAwayTeam());
            fixture.setGround(updatedFixture.getGround());
            fixture.setStartTime(updatedFixture.getStartTime());
            fixture.setTimezone(updatedFixture.getTimezone());

            saveFixtures.add(fixture);
        }

        repository.saveAll(saveFixtures);
    }

    public int getMaxAflRound() {
        Integer maxRound = repository.findMaxRound();
        return maxRound != null ? maxRound : 0;
    }

    public int getRefreshFixtureStart() {
        Integer refreshStart = repository.findRefreshFixtureStart();
        return refreshStart != null ? refreshStart : 0;
    }

    // Generic repository methods
    public AflFixture get(AflFixturePK id) {
        return repository.findById(id).orElse(null);
    }

    public List<AflFixture> findAll() {
        return repository.findAll();
    }

    @Transactional
    public void insert(AflFixture entity) {
        repository.save(entity);
    }

    @Transactional
    public void update(AflFixture entity) {
        repository.save(entity);
    }

    @Transactional
    public void insertAll(List<AflFixture> entities) {
        repository.saveAll(entities);
    }

    @Transactional
    public void updateAll(List<AflFixture> entities) {
        repository.saveAll(entities);
    }

    @Transactional
    public void delete(AflFixture entity) {
        repository.delete(entity);
    }

    @Transactional
    public void replaceAll(List<AflFixture> entities) {
        repository.deleteAll();
        repository.flush();
        repository.saveAll(entities);
    }
}
