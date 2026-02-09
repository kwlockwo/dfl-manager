package net.dflmngr.controllers.rest;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import net.dflmngr.model.web.Ladder;
import net.dflmngr.services.LadderService;

@RestController
public class LadderRestController {

	private final LadderService ladderService;
	
	public LadderRestController(LadderService ladderService) {
		this.ladderService = ladderService;
	}
	
	@GetMapping(value = "/ladder", produces = "application/json")
	public List<Ladder> ladder() {
		return ladderService.getLadder();
	}
}
