package net.dflmngr.repositories;

import net.dflmngr.model.entity.AflFixture;
import net.dflmngr.model.entity.keys.AflFixturePK;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.ZonedDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Integration tests for AflFixtureRepository.
 *
 * Tests all custom query methods against H2 database with PostgreSQL schema.
 */
class AflFixtureRepositoryIntegrationTest extends AbstractRepositoryIntegrationTest {

    @Autowired
    private AflFixtureRepository repository;

    private ZonedDateTime now;
    private ZonedDateTime past;
    private ZonedDateTime future;

    @BeforeEach
    @Override
    public void setUp() {
        now = ZonedDateTime.now();
        past = now.minusHours(2);
        future = now.plusHours(2);

        // Clear any existing data
        repository.deleteAll();
    }

    @Test
    void shouldSaveAndFindFixture() {
        // Given
        AflFixture fixture = createFixture(1, 1, "RICH", "CARL");

        // When
        AflFixture saved = repository.save(fixture);

        // Then
        assertThat(saved).isNotNull();
        assertThat(saved.getRound()).isEqualTo(1);
        assertThat(saved.getGame()).isEqualTo(1);
        assertThat(saved.getHomeTeam()).isEqualTo("RICH");
        assertThat(saved.getAwayTeam()).isEqualTo("CARL");
    }

    @Test
    void shouldFindByRound() {
        // Given
        repository.save(createFixture(1, 1, "RICH", "CARL"));
        repository.save(createFixture(1, 2, "COLL", "ESS"));
        repository.save(createFixture(2, 1, "GEEL", "HAW"));

        // When
        List<AflFixture> round1Fixtures = repository.findByRound(1);

        // Then
        assertThat(round1Fixtures).hasSize(2);
        assertThat(round1Fixtures)
            .extracting(AflFixture::getRound)
            .containsOnly(1);
    }

    @Test
    void shouldFindByRoundAndTeam() {
        // Given
        repository.save(createFixture(1, 1, "RICH", "CARL"));
        repository.save(createFixture(1, 2, "COLL", "ESS"));

        // When
        AflFixture richFixture = repository.findByRoundAndTeam(1, "RICH");
        AflFixture carlFixture = repository.findByRoundAndTeam(1, "CARL");

        // Then
        assertThat(richFixture).isNotNull();
        assertThat(richFixture.getHomeTeam()).isEqualTo("RICH");
        assertThat(richFixture.getAwayTeam()).isEqualTo("CARL");

        assertThat(carlFixture).isNotNull();
        assertThat(carlFixture.getAwayTeam()).isEqualTo("CARL");
    }

    @Test
    void shouldNotFindByRoundAndTeamWhenTeamNotPlaying() {
        // Given
        repository.save(createFixture(1, 1, "RICH", "CARL"));

        // When
        AflFixture notFound = repository.findByRoundAndTeam(1, "GEEL");

        // Then
        assertThat(notFound).isNull();
    }

    @Test
    void shouldFindIncompleteFixtures() {
        // Given - Fixture started but not finished (endTime is null)
        AflFixture incomplete = createFixture(1, 1, "RICH", "CARL");
        incomplete.setStartTime(past);
        incomplete.setEndTime(null);
        repository.save(incomplete);

        // Fixture finished (has endTime)
        AflFixture complete = createFixture(1, 2, "COLL", "ESS");
        complete.setStartTime(past);
        complete.setEndTime(now);
        repository.save(complete);

        // Fixture not started yet
        AflFixture notStarted = createFixture(1, 3, "GEEL", "HAW");
        notStarted.setStartTime(future);
        notStarted.setEndTime(null);
        repository.save(notStarted);

        // When
        List<AflFixture> incompleteFixtures = repository.findIncompleteFixtures(now);

        // Then
        assertThat(incompleteFixtures).hasSize(1);
        assertThat(incompleteFixtures.get(0).getGame()).isEqualTo(1);
    }

    @Test
    void shouldFindFixturesToScrape() {
        // Given - Fixture with statsDownloaded = false
        AflFixture needsScraping = createFixture(1, 1, "RICH", "CARL");
        needsScraping.setStartTime(past);
        needsScraping.setStatsDownloaded(false);
        repository.save(needsScraping);

        // Fixture with statsDownloaded = false (also needs scraping)
        AflFixture needsScrapingFalse = createFixture(1, 2, "COLL", "ESS");
        needsScrapingFalse.setStartTime(past);
        needsScrapingFalse.setStatsDownloaded(false);
        repository.save(needsScrapingFalse);

        // Fixture already scraped
        AflFixture scraped = createFixture(1, 3, "GEEL", "HAW");
        scraped.setStartTime(past);
        scraped.setStatsDownloaded(true);
        repository.save(scraped);

        // Fixture not started yet
        AflFixture notStarted = createFixture(1, 4, "MELB", "NMFC");
        notStarted.setStartTime(future);
        notStarted.setStatsDownloaded(false);
        repository.save(notStarted);

        // When
        List<AflFixture> toScrape = repository.findFixturesToScrape(now);

        // Then
        assertThat(toScrape).hasSize(2);
        assertThat(toScrape)
            .extracting(AflFixture::getGame)
            .containsExactlyInAnyOrder(1, 2);
    }

    @Test
    void shouldFindIncompleteFixturesForRound() {
        // Given
        AflFixture incomplete1 = createFixture(1, 1, "RICH", "CARL");
        incomplete1.setEndTime(null);
        repository.save(incomplete1);

        AflFixture incomplete2 = createFixture(1, 2, "COLL", "ESS");
        incomplete2.setEndTime(null);
        repository.save(incomplete2);

        AflFixture complete = createFixture(1, 3, "GEEL", "HAW");
        complete.setEndTime(now);
        repository.save(complete);

        // Different round
        AflFixture round2 = createFixture(2, 1, "MELB", "NMFC");
        round2.setEndTime(null);
        repository.save(round2);

        // When
        List<AflFixture> incompleteRound1 = repository.findIncompleteFixturesForRound(1);

        // Then
        assertThat(incompleteRound1).hasSize(2);
        assertThat(incompleteRound1)
            .extracting(AflFixture::getRound)
            .containsOnly(1);
    }

    @Test
    void shouldFindMaxRound() {
        // Given
        repository.save(createFixtureWithStartTime(1, 1, "RICH", "CARL", past));
        repository.save(createFixtureWithStartTime(2, 1, "COLL", "ESS", past));
        repository.save(createFixtureWithStartTime(3, 1, "GEEL", "HAW", past));

        // Fixture with no startTime (shouldn't be counted)
        AflFixture noStartTime = createFixture(4, 1, "MELB", "NMFC");
        noStartTime.setStartTime(null);
        repository.save(noStartTime);

        // When
        Integer maxRound = repository.findMaxRound();

        // Then
        assertThat(maxRound).isEqualTo(3);
    }

    @Test
    void shouldReturnNullWhenNoFixturesHaveStartTime() {
        // Given
        AflFixture fixture = createFixture(1, 1, "RICH", "CARL");
        fixture.setStartTime(null);
        repository.save(fixture);

        // When
        Integer maxRound = repository.findMaxRound();

        // Then
        assertThat(maxRound).isNull();
    }

    @Test
    void shouldFindRefreshFixtureStart() {
        // Given - Round 1 has all 9 fixtures with statsDownloaded = false
        for (int i = 1; i <= 9; i++) {
            AflFixture fixture = createFixture(1, i, "TEAM" + i, "TEAM" + (i + 10));
            fixture.setStatsDownloaded(false);
            repository.save(fixture);
        }

        // Round 2 has only 8 fixtures with statsDownloaded = false
        for (int i = 1; i <= 8; i++) {
            AflFixture fixture = createFixture(2, i, "TEAM" + i, "TEAM" + (i + 10));
            fixture.setStatsDownloaded(false);
            repository.save(fixture);
        }
        AflFixture round2Game9 = createFixture(2, 9, "TEAM9", "TEAM19");
        round2Game9.setStatsDownloaded(true);
        repository.save(round2Game9);

        // Round 3 has all 9 fixtures with statsDownloaded = false
        for (int i = 1; i <= 9; i++) {
            AflFixture fixture = createFixture(3, i, "TEAM" + i, "TEAM" + (i + 10));
            fixture.setStatsDownloaded(false);
            repository.save(fixture);
        }

        // When
        Integer refreshStart = repository.findRefreshFixtureStart();

        // Then - Should return round 1 (minimum round with exactly 9 unscraped fixtures)
        assertThat(refreshStart).isEqualTo(1);
    }

    @Test
    void shouldReturnNullWhenNoRoundHasExactly9UnscrapedFixtures() {
        // Given - All rounds have less than 9 fixtures
        for (int i = 1; i <= 5; i++) {
            AflFixture fixture = createFixture(1, i, "TEAM" + i, "TEAM" + (i + 10));
            fixture.setStatsDownloaded(false);
            repository.save(fixture);
        }

        // When
        Integer refreshStart = repository.findRefreshFixtureStart();

        // Then
        assertThat(refreshStart).isNull();
    }

    @Test
    void shouldFindByRoundAndGame() {
        // Given
        repository.save(createFixture(1, 1, "RICH", "CARL"));
        repository.save(createFixture(1, 2, "COLL", "ESS"));
        repository.save(createFixture(2, 1, "GEEL", "HAW"));

        // When
        AflFixture found = repository.findByRoundAndGame(1, 2);

        // Then
        assertThat(found).isNotNull();
        assertThat(found.getRound()).isEqualTo(1);
        assertThat(found.getGame()).isEqualTo(2);
        assertThat(found.getHomeTeam()).isEqualTo("COLL");
        assertThat(found.getAwayTeam()).isEqualTo("ESS");
    }

    // Helper methods

    private AflFixture createFixture(int round, int game, String homeTeam, String awayTeam) {
        AflFixture fixture = new AflFixture();
        fixture.setRound(round);
        fixture.setGame(game);
        fixture.setHomeTeam(homeTeam);
        fixture.setAwayTeam(awayTeam);
        fixture.setGround("MCG");
        fixture.setStartTime(now);
        fixture.setTimezone("Australia/Melbourne");
        fixture.setStatsDownloaded(false);
        return fixture;
    }

    private AflFixture createFixtureWithStartTime(int round, int game, String homeTeam, String awayTeam, ZonedDateTime startTime) {
        AflFixture fixture = createFixture(round, game, homeTeam, awayTeam);
        fixture.setStartTime(startTime);
        return fixture;
    }
}
