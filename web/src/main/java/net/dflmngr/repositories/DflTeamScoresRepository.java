package net.dflmngr.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import net.dflmngr.model.entity.DflTeamScores;
import net.dflmngr.model.entity.keys.DflTeamScoresPK;

public interface DflTeamScoresRepository extends JpaRepository<DflTeamScores, DflTeamScoresPK> {
    long countByRound(int round);
}
