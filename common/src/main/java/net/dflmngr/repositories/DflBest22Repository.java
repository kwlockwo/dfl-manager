package net.dflmngr.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import net.dflmngr.model.entity.DflBest22;

@Repository
public interface DflBest22Repository extends JpaRepository<DflBest22, Integer> {
    
    /**
     * Find all Best 22 players for a specific round
     */
    List<DflBest22> findByRound(int round);

    /**
     * Delete all Best 22 entries for a specific round
     */
    @Transactional
    @Modifying
    @Query("DELETE FROM DflBest22 b WHERE b.round = :round")
    void deleteByRound(@Param("round") int round);

    /**
     * Count Best 22 entries for a specific round
     */
    long countByRound(int round);

    /**
     * Find Best 22 entry for a specific round and player ID
     */
    DflBest22 findByRoundAndPlayerId(int round, int playerId);
}
