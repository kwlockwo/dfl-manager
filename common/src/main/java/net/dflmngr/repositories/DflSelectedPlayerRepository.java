package net.dflmngr.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import net.dflmngr.model.entity.DflSelectedPlayer;
import net.dflmngr.model.entity.keys.DflSelectedPlayerPK;

public interface DflSelectedPlayerRepository extends JpaRepository<DflSelectedPlayer, DflSelectedPlayerPK> {
	List<DflSelectedPlayer> findByRoundAndTeamCode(int round, String teamCode);
	
	@Query(value = "select case when count(t) > 0 then true else false end from DflSelectedPlayer t where t.round = :round and t.teamCode = :teamCode")
	boolean selectedTeamExists(@Param("teamCode") String teamCode, @Param("round") int round);
}
