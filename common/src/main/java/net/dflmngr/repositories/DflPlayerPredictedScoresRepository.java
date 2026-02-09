package net.dflmngr.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import net.dflmngr.model.entity.DflPlayerPredictedScores;
import net.dflmngr.model.entity.keys.DflPlayerPredictedScoresPK;

public interface DflPlayerPredictedScoresRepository extends JpaRepository<DflPlayerPredictedScores, DflPlayerPredictedScoresPK> {
    
    /**
     * Find all predicted scores for a specific round
     */
    List<DflPlayerPredictedScores> findByRound(int round);
    
    /**
     * Find predicted score for a specific round and player ID
     */
    @Query("SELECT pps FROM DflPlayerPredictedScores pps WHERE pps.round = :round AND pps.playerId = :playerId")
    DflPlayerPredictedScores findByRoundAndPlayerId(@Param("round") int round, @Param("playerId") int playerId);
    
    /**
     * Find predicted scores for a specific round and team code
     */
    @Query("SELECT pps FROM DflPlayerPredictedScores pps WHERE pps.round = :round AND pps.teamCode = :teamCode")
    List<DflPlayerPredictedScores> findByRoundAndTeamCode(@Param("round") int round, @Param("teamCode") String teamCode);
    
    /**
     * Delete all predicted scores for a specific round
     * Used in replaceAll pattern (delete + insert)
     */
    @Transactional
    @Modifying
    @Query("DELETE FROM DflPlayerPredictedScores pps WHERE pps.round = :round")
    void deleteByRound(@Param("round") int round);
    
    /**
     * Delete predicted scores for a specific round and team code
     */
    @Transactional
    @Modifying
    @Query("DELETE FROM DflPlayerPredictedScores pps WHERE pps.round = :round AND pps.teamCode = :teamCode")
    void deleteByRoundAndTeamCode(@Param("round") int round, @Param("teamCode") String teamCode);
}
