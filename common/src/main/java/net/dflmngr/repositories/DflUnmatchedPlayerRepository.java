package net.dflmngr.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import net.dflmngr.model.entity.DflUnmatchedPlayer;

@Repository
public interface DflUnmatchedPlayerRepository extends JpaRepository<DflUnmatchedPlayer, Integer> {
}
