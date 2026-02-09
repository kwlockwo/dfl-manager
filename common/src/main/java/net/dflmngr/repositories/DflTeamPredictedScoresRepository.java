package net.dflmngr.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import net.dflmngr.model.entity.DflTeamPredictedScores;
import net.dflmngr.model.entity.keys.DflTeamPredictedScoresPK;

public interface DflTeamPredictedScoresRepository extends JpaRepository<DflTeamPredictedScores, DflTeamPredictedScoresPK> {
    
    /**
     * Find all team predicted scores for a specific round
     */
    List<DflTeamPredictedScores> findByRound(int round);
    
    /**
     * Find team predicted score for a specific round and team code
     */
    @Query("SELECT tps FROM DflTeamPredictedScores tps WHERE tps.round = :round AND tps.teamCode = :teamCode")
    DflTeamPredictedScores findByRoundAndTeamCode(@Param("round") int round, @Param("teamCode") String teamCode);
    
    /**
     * Delete all predicted scores for a specific round
     * Used in replaceAll pattern (delete + insert)
     */
    @Transactional
    @Modifying
    @Query("DELETE FROM DflTeamPredictedScores tps WHERE tps.round = :round")
    void deleteByRound(@Param("round") int round);
    
    /**
     * Delete predicted score for a specific round and team code
     */
    @Transactional
    @Modifying
    @Query("DELETE FROM DflTeamPredictedScores tps WHERE tps.round = :round AND tps.teamCode = :teamCode")
    void deleteByRoundAndTeamCode(@Param("round") int round, @Param("teamCode") String teamCode);
    
    /**
     * Calculate sum of predicted scores for a specific round
     */
    @Query("SELECT SUM(tps.predictedScore) FROM DflTeamPredictedScores tps WHERE tps.round = :round")
    Integer sumPredictedScoreByRound(@Param("round") int round);
}
