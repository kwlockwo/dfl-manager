package net.dflmngr.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import net.dflmngr.model.entity.StatsRoundPlayerStats;
import net.dflmngr.model.entity.keys.StatsRoundPlayerStatsPK;

@Repository
public interface StatsRoundPlayerStatsRepository extends JpaRepository<StatsRoundPlayerStats, StatsRoundPlayerStatsPK> {
	
	List<StatsRoundPlayerStats> findByRound(int round);
	
	@Modifying
	@Query("DELETE FROM StatsRoundPlayerStats s WHERE s.round = :round AND s.team = :team")
	void deleteByRoundAndTeam(@Param("round") int round, @Param("team") String team);

	List<StatsRoundPlayerStats> findByRoundAndTeam(int round, String team);

	/**
	 * Find stats for a specific round and player name
	 * Note: StatsRoundPlayerStats doesn't have a playerId field - it uses name, team, and jumperNo as identifiers
	 */
	List<StatsRoundPlayerStats> findByRoundAndName(int round, String name);
}
