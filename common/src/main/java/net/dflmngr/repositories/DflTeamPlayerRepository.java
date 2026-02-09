package net.dflmngr.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import net.dflmngr.model.entity.DflTeamPlayer;

@Repository
public interface DflTeamPlayerRepository extends JpaRepository<DflTeamPlayer, Integer> {
	DflTeamPlayer findByTeamCodeAndTeamPlayerId(String teamCode, int teamPlayerId);
}
