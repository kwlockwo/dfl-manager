package net.dflmngr.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import net.dflmngr.model.entity.RawPlayerStats;
import net.dflmngr.model.entity.keys.RawPlayerStatsPK;

public interface RawPlayerStatsRepository extends JpaRepository<RawPlayerStats, RawPlayerStatsPK> {
	RawPlayerStats findByRoundAndTeamAndJumperNo(int round, String team, int jumperNo);
}
