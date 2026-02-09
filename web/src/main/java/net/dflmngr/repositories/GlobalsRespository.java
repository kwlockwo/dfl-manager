package net.dflmngr.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import net.dflmngr.model.entity.Globals;
import net.dflmngr.model.entity.keys.GlobalsPK;

public interface GlobalsRespository extends JpaRepository<Globals, GlobalsPK>  {}
