package net.dflmngr.controllers;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;

import net.dflmngr.model.web.Results;
import net.dflmngr.model.web.RoundMenu;
import net.dflmngr.model.web.TeamResults;
import net.dflmngr.services.ResultService;

@Controller
public class ResultController {
	
	private static final String RESULTS = "results";
	private static final String HOME_EMG_MESSAGE = "homeEmgMessage";
	private static final String AWAY_EMG_MESSAGE = "awayEmgMessage";
	private static final String RESULTS_EMG_STAR = "results.emg.star";
	private static final String RESULTS_EMG_DBLSTAR = "results.emg.doublestar";
	private static final String RESULTS_EMG_TRIPSTAR = "results.emg.tripplestar";

	private final ResultService resultService;

	public ResultController(ResultService resultService) {
		this.resultService = resultService;
	}
	
    @ModelAttribute("module")
    public String module() {
        return RESULTS;
    }

	@GetMapping(value = "/results/{round}/{game}", produces = "text/html")
	public String results(@PathVariable int round, @PathVariable int game, Model model) {
		Results results = resultService.getResults(round, game);
		model.addAttribute(RESULTS, results);
		
		if(results.getHomeTeam() != null || results.getAwayTeam() != null) {
			TeamResults team = results.getHomeTeam();
			setEmgIndicators(team, true, model);
			
			team = results.getAwayTeam();
			setEmgIndicators(team, false, model);
		}

		List<RoundMenu> roundsMenu = resultService.getMenu(round, game);
		model.addAttribute("menu", roundsMenu);
		
		return RESULTS;
	}
	
	@GetMapping(value = "/results", produces = "text/html")
	public String currentResults(Model model) {
		Results results = resultService.getCurrentResults();
		model.addAttribute(RESULTS, results);
		
		if(results.getHomeTeam() != null || results.getAwayTeam() != null) {
			TeamResults team = results.getHomeTeam();
			setEmgIndicators(team, true, model);
			
			team = results.getAwayTeam();
			setEmgIndicators(team, false, model);
		}
		
		List<RoundMenu> roundsMenu = resultService.getMenu(results.getRound(), results.getGame());
		model.addAttribute("menu", roundsMenu);
		
		return RESULTS;
	}

	private void setEmgIndicators(TeamResults team, boolean homeOrAway, Model model) {
		if(team != null && team.getEmgInd() != null) {
			if(team.getEmgInd().equals("*")) {
				model.addAttribute(homeOrAway ? HOME_EMG_MESSAGE : AWAY_EMG_MESSAGE, RESULTS_EMG_STAR);
			} else if(team.getEmgInd().equals("**")) {
				model.addAttribute(homeOrAway ? HOME_EMG_MESSAGE : AWAY_EMG_MESSAGE, RESULTS_EMG_DBLSTAR);
			} else if(team.getEmgInd().equals("*/**")) {
				model.addAttribute(homeOrAway ? HOME_EMG_MESSAGE : AWAY_EMG_MESSAGE, RESULTS_EMG_TRIPSTAR);
			}
		}
	}

}
