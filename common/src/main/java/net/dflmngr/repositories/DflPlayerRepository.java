package net.dflmngr.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import net.dflmngr.model.entity.DflPlayer;

public interface DflPlayerRepository extends JpaRepository<DflPlayer, Integer> {
	List<DflPlayer> findByPlayerIdIn(List<Integer> playerIds);
}