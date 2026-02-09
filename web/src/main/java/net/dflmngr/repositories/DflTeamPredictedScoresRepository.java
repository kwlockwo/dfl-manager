package net.dflmngr.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import net.dflmngr.model.entity.DflTeamPredictedScores;
import net.dflmngr.model.entity.keys.DflTeamPredictedScoresPK;

public interface DflTeamPredictedScoresRepository extends JpaRepository<DflTeamPredictedScores, DflTeamPredictedScoresPK> {}
