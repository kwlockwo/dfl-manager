package net.dflmngr.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import net.dflmngr.model.entity.DflPlayerScores;
import net.dflmngr.model.entity.keys.DflPlayerScoresPK;

public interface DflPlayerScoresRepository extends JpaRepository<DflPlayerScores, DflPlayerScoresPK> {}
