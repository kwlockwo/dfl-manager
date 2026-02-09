package net.dflmngr.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import net.dflmngr.model.entity.DflRoundInfo;

@Repository
public interface DflRoundInfoRepository extends JpaRepository<DflRoundInfo, Integer> {

	@Query("SELECT DISTINCT ri FROM DflRoundInfo ri JOIN FETCH ri.roundMapping rm WHERE rm.aflRound IN :aflRounds")
	List<DflRoundInfo> findRoundsByAflRounds(@Param("aflRounds") List<Integer> aflRounds);

	/**
	 * Find round info by round number (primary key lookup)
	 */
	DflRoundInfo findByRound(int round);
}
