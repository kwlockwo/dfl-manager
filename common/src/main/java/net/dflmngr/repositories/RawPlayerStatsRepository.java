package net.dflmngr.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import net.dflmngr.model.entity.RawPlayerStats;
import net.dflmngr.model.entity.keys.RawPlayerStatsPK;

public interface RawPlayerStatsRepository extends JpaRepository<RawPlayerStats, RawPlayerStatsPK> {
    
    /**
     * Find raw player stats by round, team and jumper number
     */
    RawPlayerStats findByRoundAndTeamAndJumperNo(int round, String team, int jumperNo);
    
    /**
     * Find all raw player stats for a specific round
     */
    List<RawPlayerStats> findByRound(int round);
    
    /**
     * Find raw player stats for a specific round and team
     */
    @Query("SELECT rps FROM RawPlayerStats rps WHERE rps.round = :round AND rps.team = :team")
    List<RawPlayerStats> findByRoundAndTeam(@Param("round") int round, @Param("team") String team);
    
    /**
     * Delete stats for a specific round and team
     * Used when refreshing team stats
     */
    @Transactional
    @Modifying
    @Query("DELETE FROM RawPlayerStats rps WHERE rps.round = :round AND rps.team = :team")
    void deleteByRoundAndTeam(@Param("round") int round, @Param("team") String team);
    
    /**
     * Delete all stats for a specific round
     */
    @Transactional
    @Modifying
    @Query("DELETE FROM RawPlayerStats rps WHERE rps.round = :round")
    void deleteByRound(@Param("round") int round);
    
    /**
     * Count stats for a specific round
     */
    long countByRound(int round);
    
    /**
     * Find stats by scraping status
     */
    @Query("SELECT rps FROM RawPlayerStats rps WHERE rps.round = :round AND rps.scrapingStatus = :status")
    List<RawPlayerStats> findByRoundAndScrapingStatus(@Param("round") int round, @Param("status") String status);

    /**
     * Find raw player stats by round and player name
     * Note: RawPlayerStats doesn't have a playerId field - it uses name, team, and jumperNo as identifiers
     */
    List<RawPlayerStats> findByRoundAndName(int round, String name);
}
