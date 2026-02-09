package net.dflmngr.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import net.dflmngr.model.entity.DflPlayerPredictedScores;
import net.dflmngr.model.entity.keys.DflPlayerPredictedScoresPK;

public interface DflPlayerPredictedScoresRepository extends JpaRepository<DflPlayerPredictedScores, DflPlayerPredictedScoresPK> {}
