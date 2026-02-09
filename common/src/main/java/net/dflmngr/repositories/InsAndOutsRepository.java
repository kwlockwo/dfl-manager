package net.dflmngr.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import net.dflmngr.model.entity.InsAndOuts;

@Repository
public interface InsAndOutsRepository extends JpaRepository<InsAndOuts, Integer> {
	
	List<InsAndOuts> findByRoundAndTeamCode(int round, String teamCode);
	
	List<InsAndOuts> findByRound(int round);
}
