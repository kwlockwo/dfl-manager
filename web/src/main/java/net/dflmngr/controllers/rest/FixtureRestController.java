package net.dflmngr.controllers.rest;

import java.util.List;

import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RestController;

import net.dflmngr.model.web.RoundFixtures;
import net.dflmngr.services.FixtureService;

@RestController
public class FixtureRestController {
	
	private final FixtureService fixtureService;

	public FixtureRestController(FixtureService fixtureRespository) {
		this.fixtureService = fixtureRespository;
	}
	
    @ModelAttribute("module")
    public String module() {
        return "fixtures";
    }
	
	@GetMapping(value = "/fixtures", produces = "application/json")
	public List<RoundFixtures> fixtures(Model model) {		
		return fixtureService.getFixtures();		
	}
}
