package net.dflmngr.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import net.dflmngr.model.entity.AflPlayer;

public interface AflPlayerRepository extends JpaRepository<AflPlayer, String> {
    
    /**
     * Find AFL player by DFL player ID
     */
    AflPlayer findByDflPlayerId(Integer playerId);
    
    /**
     * Find AFL players by team
     */
    @Query("SELECT p FROM AflPlayer p WHERE p.team = :team")
    List<AflPlayer> findByTeam(@Param("team") String team);
    
    /**
     * Find AFL players with mapped DFL player ID
     */
    @Query("SELECT p FROM AflPlayer p WHERE p.dflPlayerId IS NOT NULL")
    List<AflPlayer> findWithDflPlayerId();
    
    /**
     * Find AFL players without mapped DFL player ID
     */
    @Query("SELECT p FROM AflPlayer p WHERE p.dflPlayerId IS NULL")
    List<AflPlayer> findUnmapped();
}
