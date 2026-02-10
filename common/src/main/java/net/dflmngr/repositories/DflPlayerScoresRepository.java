package net.dflmngr.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import net.dflmngr.model.entity.DflPlayerScores;
import net.dflmngr.model.entity.keys.DflPlayerScoresPK;

public interface DflPlayerScoresRepository extends JpaRepository<DflPlayerScores, DflPlayerScoresPK> {
    
    /**
     * Find all player scores for a specific round
     */
    List<DflPlayerScores> findByRound(int round);
    
    /**
     * Find player scores for a specific round and player ID
     */
    @Query("SELECT ps FROM DflPlayerScores ps WHERE ps.round = :round AND ps.playerId = :playerId")
    DflPlayerScores findByRoundAndPlayerId(@Param("round") int round, @Param("playerId") int playerId);
    
    /**
     * Find player scores for a specific round and team code
     */
    @Query("SELECT ps FROM DflPlayerScores ps WHERE ps.round = :round AND ps.teamCode = :teamCode")
    List<DflPlayerScores> findByRoundAndTeamCode(@Param("round") int round, @Param("teamCode") String teamCode);
    
    /**
     * Calculate total score for a team in a round
     */
    @Query("SELECT SUM(ps.score) FROM DflPlayerScores ps WHERE ps.round = :round AND ps.teamCode = :teamCode")
    Integer sumScoreByRoundAndTeamCode(@Param("round") int round, @Param("teamCode") String teamCode);

    /**
     * Delete all player scores for a specific round
     */
    @Transactional
    @Modifying
    @Query("DELETE FROM DflPlayerScores ps WHERE ps.round = :round")
    void deleteByRound(@Param("round") int round);
}
