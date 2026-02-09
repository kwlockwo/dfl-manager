package net.dflmngr.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import net.dflmngr.model.entity.DflPreseasonScores;
import net.dflmngr.model.entity.keys.DflPreseasonScoresPK;

@Repository
public interface DflPreseasonScoresRepository extends JpaRepository<DflPreseasonScores, DflPreseasonScoresPK> {
}
