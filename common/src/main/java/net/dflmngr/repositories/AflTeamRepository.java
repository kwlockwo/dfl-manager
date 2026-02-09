package net.dflmngr.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import net.dflmngr.model.entity.AflTeam;

@Repository
public interface AflTeamRepository extends JpaRepository<AflTeam, String> {
	AflTeam findByName(String name);
}
