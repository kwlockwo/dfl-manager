package net.dflmngr.controllers;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;

import net.dflmngr.model.web.RoundFixtures;
import net.dflmngr.services.FixtureService;

@Controller
public class FixtureController {
	
	private static final String FIXTURES_VIEW = "fixtures";
	
	private final FixtureService fixtureService;

	public FixtureController(FixtureService fixtureRespository) {
		this.fixtureService = fixtureRespository;
	}
	
    @ModelAttribute("module")
    public String module() {
        return FIXTURES_VIEW;
    }
	
	@GetMapping(value = "/fixtures", produces = "text/html")
	public String fixtures(Model model) {		
		List<RoundFixtures> fixtures = fixtureService.getFixtures();
		model.addAttribute(FIXTURES_VIEW, fixtures);
		
		return FIXTURES_VIEW;
	}
}
