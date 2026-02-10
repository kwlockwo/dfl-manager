package net.dflmngr.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import net.dflmngr.model.entity.DflTeamPlayer;

@Repository
public interface DflTeamPlayerRepository extends JpaRepository<DflTeamPlayer, Integer> {
	DflTeamPlayer findByTeamCodeAndTeamPlayerId(String teamCode, int teamPlayerId);

	/**
	 * Find all team players for a specific team code
	 */
	List<DflTeamPlayer> findByTeamCode(String teamCode);

	/**
	 * Find a team player by team code and player ID
	 */
	DflTeamPlayer findByTeamCodeAndPlayerId(String teamCode, int playerId);
}
