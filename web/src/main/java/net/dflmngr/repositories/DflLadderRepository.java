package net.dflmngr.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import net.dflmngr.model.entity.DflLadder;
import net.dflmngr.model.entity.keys.DflLadderPK;

public interface DflLadderRepository extends JpaRepository<DflLadder, DflLadderPK> {
	
	@Query("select l from DflLadder l where l.round = (select max(round) from DflLadder where live = false)")
	public List<DflLadder> findCurrentDflLadder();
	
	@Query("select l from DflLadder l where l.round = (select max(round) from DflLadder where live = true)")
	public List<DflLadder> findLiveDflLadder();
}
