package net.dflmngr.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import net.dflmngr.model.entity.AflPlayer;

public interface AflPlayerRepository extends JpaRepository<AflPlayer, String>{
	AflPlayer findByDflPlayerId(Integer playerId);
}
