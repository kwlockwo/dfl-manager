package net.dflmngr.repositories;

import java.time.ZonedDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import net.dflmngr.model.entity.AflFixture;
import net.dflmngr.model.entity.keys.AflFixturePK;

public interface AflFixtureRepository extends JpaRepository<AflFixture, AflFixturePK> {
    
    /**
     * Find all fixtures for a specific round
     */
    List<AflFixture> findByRound(int round);
    
    /**
     * Find fixture for a specific round and team (either home or away)
     */
    @Query("SELECT f FROM AflFixture f WHERE f.round = :round AND (f.homeTeam = :team OR f.awayTeam = :team)")
    AflFixture findByRoundAndTeam(@Param("round") int round, @Param("team") String team);
    
    /**
     * Find incomplete fixtures (endTime is NULL and startTime < provided time)
     */
    @Query("SELECT f FROM AflFixture f WHERE f.startTime < :time AND f.endTime IS NULL")
    List<AflFixture> findIncompleteFixtures(@Param("time") ZonedDateTime time);
    
    /**
     * Find fixtures to scrape (startTime < provided time and statsDownloaded is NULL or FALSE)
     */
    @Query("SELECT f FROM AflFixture f WHERE f.startTime < :time AND (f.statsDownloaded IS NULL OR f.statsDownloaded = false)")
    List<AflFixture> findFixturesToScrape(@Param("time") ZonedDateTime time);
    
    /**
     * Find incomplete fixtures for a specific round (endTime is NULL)
     */
    @Query("SELECT f FROM AflFixture f WHERE f.round = :round AND f.endTime IS NULL")
    List<AflFixture> findIncompleteFixturesForRound(@Param("round") int round);
    
    /**
     * Find the maximum round number where startTime is not NULL
     */
    @Query("SELECT MAX(f.round) FROM AflFixture f WHERE f.startTime IS NOT NULL")
    Integer findMaxRound();
    
    /**
     * Complex subquery: Find the minimum round where exactly 9 fixtures have statsDownloaded = false
     * This is used to determine where to start refreshing fixture data
     */
    @Query("SELECT MIN(f.round) FROM AflFixture f WHERE f.round IN " +
           "(SELECT f2.round FROM AflFixture f2 WHERE f2.statsDownloaded = false " +
           "GROUP BY f2.round HAVING COUNT(f2.round) = 9)")
    Integer findRefreshFixtureStart();
}
