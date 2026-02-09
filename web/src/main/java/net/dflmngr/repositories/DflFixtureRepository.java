package net.dflmngr.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import net.dflmngr.model.entity.DflFixture;
import net.dflmngr.model.entity.keys.DflFixturePK;

public interface DflFixtureRepository extends JpaRepository<DflFixture, DflFixturePK> {}
