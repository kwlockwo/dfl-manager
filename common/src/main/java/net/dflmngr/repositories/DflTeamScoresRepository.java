package net.dflmngr.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import net.dflmngr.model.entity.DflTeamScores;
import net.dflmngr.model.entity.keys.DflTeamScoresPK;

public interface DflTeamScoresRepository extends JpaRepository<DflTeamScores, DflTeamScoresPK> {
    
    /**
     * Find all team scores for a specific round
     */
    List<DflTeamScores> findByRound(int round);
    
    /**
     * Find team score for a specific round and team code
     */
    @Query("SELECT ts FROM DflTeamScores ts WHERE ts.round = :round AND ts.teamCode = :teamCode")
    DflTeamScores findByRoundAndTeamCode(@Param("round") int round, @Param("teamCode") String teamCode);
    
    /**
     * Count team scores for a specific round
     */
    long countByRound(int round);
    
    /**
     * Delete all team scores for a specific round
     */
    @Transactional
    @Modifying
    @Query("DELETE FROM DflTeamScores ts WHERE ts.round = :round")
    void deleteByRound(@Param("round") int round);
}
