package net.dflmngr.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import net.dflmngr.model.entity.DflTeam;

public interface DflTeamRepository extends JpaRepository<DflTeam, String> {}
