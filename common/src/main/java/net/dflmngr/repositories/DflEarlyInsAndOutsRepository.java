package net.dflmngr.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import net.dflmngr.model.entity.DflEarlyInsAndOuts;

@Repository
public interface DflEarlyInsAndOutsRepository extends JpaRepository<DflEarlyInsAndOuts, Integer> {
	
	List<DflEarlyInsAndOuts> findByRoundAndTeamCode(int round, String teamCode);
}
