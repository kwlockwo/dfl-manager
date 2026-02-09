package net.dflmngr.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import net.dflmngr.model.entity.DflSelectedPlayer;
import net.dflmngr.model.entity.keys.DflSelectedPlayerPK;

public interface DflSelectedPlayerRepository extends JpaRepository<DflSelectedPlayer, DflSelectedPlayerPK> {
    
    /**
     * Find all selected players for a specific round
     */
    List<DflSelectedPlayer> findByRound(int round);
    
    /**
     * Find selected players for a specific round and team code
     */
    List<DflSelectedPlayer> findByRoundAndTeamCode(int round, String teamCode);
    
    /**
     * Find selected player for a specific round and player ID
     */
    @Query("SELECT sp FROM DflSelectedPlayer sp WHERE sp.round = :round AND sp.playerId = :playerId")
    DflSelectedPlayer findByRoundAndPlayerId(@Param("round") int round, @Param("playerId") int playerId);
    
    /**
     * Check if a selected team exists for a round and team code
     */
    @Query("SELECT CASE WHEN COUNT(sp) > 0 THEN true ELSE false END FROM DflSelectedPlayer sp " +
           "WHERE sp.round = :round AND sp.teamCode = :teamCode")
    boolean selectedTeamExists(@Param("round") int round, @Param("teamCode") String teamCode);
    
    /**
     * Count selected players for a round and team
     */
    long countByRoundAndTeamCode(int round, String teamCode);
    
    /**
     * Find non-emergency players for a round and team
     */
    @Query("SELECT sp FROM DflSelectedPlayer sp WHERE sp.round = :round AND sp.teamCode = :teamCode AND sp.isEmergency = 0")
    List<DflSelectedPlayer> findNonEmergencyByRoundAndTeamCode(@Param("round") int round, @Param("teamCode") String teamCode);
    
    /**
     * Find emergency players for a round and team
     */
    @Query("SELECT sp FROM DflSelectedPlayer sp WHERE sp.round = :round AND sp.teamCode = :teamCode AND sp.isEmergency > 0")
    List<DflSelectedPlayer> findEmergencyByRoundAndTeamCode(@Param("round") int round, @Param("teamCode") String teamCode);
    
    /**
     * Find players marked as DNP (did not play) for a round and team
     */
    @Query("SELECT sp FROM DflSelectedPlayer sp WHERE sp.round = :round AND sp.teamCode = :teamCode AND sp.isDnp = true")
    List<DflSelectedPlayer> findDnpByRoundAndTeamCode(@Param("round") int round, @Param("teamCode") String teamCode);
    
    /**
     * Delete all selected players for a specific round
     */
    @Transactional
    @Modifying
    @Query("DELETE FROM DflSelectedPlayer sp WHERE sp.round = :round")
    void deleteByRound(@Param("round") int round);
    
    /**
     * Delete selected players for a specific round and team code
     */
    @Transactional
    @Modifying
    @Query("DELETE FROM DflSelectedPlayer sp WHERE sp.round = :round AND sp.teamCode = :teamCode")
    void deleteByRoundAndTeamCode(@Param("round") int round, @Param("teamCode") String teamCode);
}
