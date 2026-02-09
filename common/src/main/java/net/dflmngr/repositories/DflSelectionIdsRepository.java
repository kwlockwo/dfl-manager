package net.dflmngr.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import net.dflmngr.model.entity.DflSelectionIds;

@Repository
public interface DflSelectionIdsRepository extends JpaRepository<DflSelectionIds, Integer> {
	
	List<DflSelectionIds> findByRoundAndTeamCodeAndSelectionId(int round, String teamCode, String selectionId);
}
