package net.dflmngr.controllers.rest;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import net.dflmngr.model.web.Results;
import net.dflmngr.services.ResultService;

@RestController
public class ResultsRestController {

	private final ResultService resultService;
	
	public ResultsRestController(ResultService resultService) {
		this.resultService = resultService;
	}
	
	@GetMapping(value = "/results/{round}/{game}", produces = "application/json")
	public Results getResults(@PathVariable int round, @PathVariable int game) {
		return resultService.getResults(round, game);
	}	
}
