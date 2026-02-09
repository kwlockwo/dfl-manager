package net.dflmngr.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import net.dflmngr.model.entity.Globals;
import net.dflmngr.model.entity.keys.GlobalsPK;

@Repository
public interface GlobalsRepository extends JpaRepository<Globals, GlobalsPK> {
	List<Globals> findByGroupCode(String groupCode);
}
