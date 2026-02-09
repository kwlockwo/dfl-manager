package net.dflmngr.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import net.dflmngr.model.entity.DflPlayer;

public interface DflPlayerRepository extends JpaRepository<DflPlayer, Integer> {
    
    /**
     * Find players by a list of player IDs
     */
    List<DflPlayer> findByPlayerIdIn(List<Integer> playerIds);
    
    /**
     * Find DFL player by AFL player ID
     */
    @Query("SELECT p FROM DflPlayer p WHERE p.aflPlayerId = :aflPlayerId")
    DflPlayer findByAflPlayerId(@Param("aflPlayerId") String aflPlayerId);
    
    /**
     * Find all first-year players (Adam Goodes eligible)
     */
    @Query("SELECT p FROM DflPlayer p WHERE p.isFirstYear = true")
    List<DflPlayer> findAdamGoodesEligible();
    
    /**
     * Find all players by AFL club/team
     */
    @Query("SELECT p FROM DflPlayer p WHERE p.aflClub = :team")
    List<DflPlayer> findByAflClub(@Param("team") String team);
    
    /**
     * Find all players with non-null AFL player ID
     */
    @Query("SELECT p FROM DflPlayer p WHERE p.aflPlayerId IS NOT NULL")
    List<DflPlayer> findWithAflPlayerId();
    
    /**
     * Find players by position
     */
    @Query("SELECT p FROM DflPlayer p WHERE p.position = :position")
    List<DflPlayer> findByPosition(@Param("position") String position);
}
