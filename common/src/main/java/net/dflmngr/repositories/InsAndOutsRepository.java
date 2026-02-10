package net.dflmngr.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import net.dflmngr.model.entity.InsAndOuts;

@Repository
public interface InsAndOutsRepository extends JpaRepository<InsAndOuts, Integer> {
    
    /**
     * Find all ins and outs for a specific round
     */
    List<InsAndOuts> findByRound(int round);
    
    /**
     * Find ins and outs for a specific round and team code
     */
    List<InsAndOuts> findByRoundAndTeamCode(int round, String teamCode);
    
    /**
     * Find ins (in_out = 'I') for a specific round and team code
     */
    @Query("SELECT io FROM InsAndOuts io WHERE io.round = :round AND io.teamCode = :teamCode AND io.inOut = 'I'")
    List<InsAndOuts> findInsByRoundAndTeamCode(@Param("round") int round, @Param("teamCode") String teamCode);
    
    /**
     * Find outs (in_out = 'O') for a specific round and team code
     */
    @Query("SELECT io FROM InsAndOuts io WHERE io.round = :round AND io.teamCode = :teamCode AND io.inOut = 'O'")
    List<InsAndOuts> findOutsByRoundAndTeamCode(@Param("round") int round, @Param("teamCode") String teamCode);
    
    /**
     * Delete all ins and outs for a specific round
     */
    @Transactional
    @Modifying
    @Query("DELETE FROM InsAndOuts io WHERE io.round = :round")
    void deleteByRound(@Param("round") int round);
    
    /**
     * Delete ins and outs for a specific round and team code
     */
    @Transactional
    @Modifying
    @Query("DELETE FROM InsAndOuts io WHERE io.round = :round AND io.teamCode = :teamCode")
    void deleteByRoundAndTeamCode(@Param("round") int round, @Param("teamCode") String teamCode);
    
    /**
     * Count ins and outs for a specific round
     */
    long countByRound(int round);

    /**
     * Find a specific ins and outs entry by round, team code, and player ID
     */
    InsAndOuts findByRoundAndTeamCodeAndPlayerId(int round, String teamCode, int playerId);
}
