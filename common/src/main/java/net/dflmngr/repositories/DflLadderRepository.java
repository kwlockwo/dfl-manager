package net.dflmngr.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import net.dflmngr.model.entity.DflLadder;
import net.dflmngr.model.entity.keys.DflLadderPK;

@Repository
public interface DflLadderRepository extends JpaRepository<DflLadder, DflLadderPK> {
	
	List<DflLadder> findByRound(int round);
	
	@Query("SELECT l FROM DflLadder l WHERE l.round = (SELECT MAX(l2.round) FROM DflLadder l2 WHERE l2.live = false)")
	List<DflLadder> findCurrentDflLadder();
	
	@Query("SELECT l FROM DflLadder l WHERE l.round = (SELECT MAX(l2.round) FROM DflLadder l2 WHERE l2.live = true)")
	List<DflLadder> findLiveDflLadder();
}
