package net.dflmngr.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import net.dflmngr.model.entity.DflBest22;

@Repository
public interface DflBest22Repository extends JpaRepository<DflBest22, Integer> {
	
	List<DflBest22> findByRound(int round);
}
