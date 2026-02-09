package net.dflmngr.controllers;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;

import net.dflmngr.model.web.Ladder;
import net.dflmngr.services.LadderService;

@Controller
public class LadderController {

	private static final String LADDER_VIEW = "ladder";

	private final LadderService ladderService;

	public LadderController(LadderService ladderService) {
		this.ladderService = ladderService;
	}

    @ModelAttribute("module")
    public String module() {
        return LADDER_VIEW;
    }

    @GetMapping(value = "/", produces = "text/html")
    public String ladder(Model model) {
    	List<Ladder> ladder = ladderService.getLadder();

		int round = 1;

		if(!ladder.isEmpty()) {
			model.addAttribute(LADDER_VIEW, ladder);
			round = ladder.get(0).getRound();
		}

    	model.addAttribute("currentRound", round);

    	List<Ladder> liveLadder = ladderService.getLiveLadder();
    	model.addAttribute("liveLadder", liveLadder);

    	return LADDER_VIEW;
    }
}
