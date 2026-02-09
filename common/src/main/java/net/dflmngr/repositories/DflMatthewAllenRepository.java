package net.dflmngr.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import net.dflmngr.model.entity.DflMatthewAllen;

@Repository
public interface DflMatthewAllenRepository extends JpaRepository<DflMatthewAllen, Integer> {
	
	List<DflMatthewAllen> findByRound(int round);
	
	@Query("SELECT ma FROM DflMatthewAllen ma WHERE ma.playerId = :playerId ORDER BY ma.round DESC LIMIT 1")
	DflMatthewAllen findLastVotesByPlayerId(@Param("playerId") int playerId);
	
	@Modifying
	@Query("DELETE FROM DflMatthewAllen ma WHERE ma.round = :round")
	void deleteByRound(@Param("round") int round);
}
