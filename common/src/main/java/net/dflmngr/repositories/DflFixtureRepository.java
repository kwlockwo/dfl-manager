package net.dflmngr.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import net.dflmngr.model.entity.DflFixture;
import net.dflmngr.model.entity.keys.DflFixturePK;

public interface DflFixtureRepository extends JpaRepository<DflFixture, DflFixturePK> {
    
    /**
     * Find all DFL fixtures for a specific round
     */
    List<DflFixture> findByRound(int round);
}
