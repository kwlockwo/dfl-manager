package net.dflmngr.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import net.dflmngr.model.entity.DflEarlyInsAndOuts;

@Repository
public interface DflEarlyInsAndOutsRepository extends JpaRepository<DflEarlyInsAndOuts, Integer> {

	List<DflEarlyInsAndOuts> findByRoundAndTeamCode(int round, String teamCode);

	/**
	 * Find all early ins and outs for a specific round
	 */
	List<DflEarlyInsAndOuts> findByRound(int round);

	/**
	 * Find a specific early ins and outs entry by round, team code, and player ID
	 */
	DflEarlyInsAndOuts findByRoundAndTeamCodeAndPlayerId(int round, String teamCode, int playerId);
}
