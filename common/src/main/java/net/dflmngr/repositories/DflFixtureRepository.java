package net.dflmngr.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import net.dflmngr.model.entity.DflFixture;
import net.dflmngr.model.entity.keys.DflFixturePK;

public interface DflFixtureRepository extends JpaRepository<DflFixture, DflFixturePK> {

    /**
     * Find all DFL fixtures for a specific round
     */
    List<DflFixture> findByRound(int round);

    /**
     * Find a specific DFL fixture by round and game number
     */
    @Query("SELECT f FROM DflFixture f WHERE f.round = :round AND f.game = :game")
    DflFixture findByRoundAndGame(@Param("round") int round, @Param("game") int game);
}
