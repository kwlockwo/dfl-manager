package net.dflmngr.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import net.dflmngr.model.entity.DflLadder;
import net.dflmngr.model.entity.keys.DflLadderPK;

@Repository
public interface DflLadderRepository extends JpaRepository<DflLadder, DflLadderPK> {
    
    /**
     * Find ladder for a specific round
     */
    List<DflLadder> findByRound(int round);
    
    /**
     * Find current DFL ladder (max round where live = false)
     */
    @Query("SELECT l FROM DflLadder l WHERE l.round = (SELECT MAX(l2.round) FROM DflLadder l2 WHERE l2.live = false) ORDER BY l.ladderPos")
    List<DflLadder> findCurrentDflLadder();
    
    /**
     * Find live DFL ladder (max round where live = true)
     */
    @Query("SELECT l FROM DflLadder l WHERE l.round = (SELECT MAX(l2.round) FROM DflLadder l2 WHERE l2.live = true) ORDER BY l.ladderPos")
    List<DflLadder> findLiveDflLadder();
    
    /**
     * Find ladder entry for a specific round and team code
     */
    @Query("SELECT l FROM DflLadder l WHERE l.round = :round AND l.teamCode = :teamCode")
    DflLadder findByRoundAndTeamCode(@Param("round") int round, @Param("teamCode") String teamCode);
    
    /**
     * Find maximum round in ladder
     */
    @Query("SELECT MAX(l.round) FROM DflLadder l")
    Integer findMaxRound();
    
    /**
     * Delete all ladder entries for a specific round
     */
    @Transactional
    @Modifying
    @Query("DELETE FROM DflLadder l WHERE l.round = :round")
    void deleteByRound(@Param("round") int round);
    
    /**
     * Check if ladder exists for a round
     */
    boolean existsByRound(int round);
}
