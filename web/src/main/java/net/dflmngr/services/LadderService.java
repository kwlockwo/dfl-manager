package net.dflmngr.services;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import net.dflmngr.model.entity.DflLadder;
import net.dflmngr.model.entity.DflTeam;
import net.dflmngr.model.web.Ladder;
import net.dflmngr.repositories.DflLadderRepository;
import net.dflmngr.repositories.DflTeamRepository;

@Service
public class LadderService {
	
	private final DflLadderRepository dflLadderRepository;
	private final DflTeamRepository dflTeamRepository;
	
	public LadderService(DflLadderRepository dflLadderRepository, DflTeamRepository dflTeamRepository) {
		this.dflLadderRepository = dflLadderRepository;
		this.dflTeamRepository = dflTeamRepository;
	}
	
	public List<Ladder> getLadder() {
		List<DflLadder> dflLadder = dflLadderRepository.findCurrentDflLadder();
		List<DflTeam> dflTeamsList = dflTeamRepository.findAll();
		
		Map<String, DflTeam> dflTeams = dflTeamsList.stream().collect(Collectors.toMap(DflTeam::getTeamCode, item -> item));
		
		return createLadder(dflLadder, dflTeams);
	}
	
	public List<Ladder> getLiveLadder() {
		List<DflLadder> dflLadder = dflLadderRepository.findLiveDflLadder();
		List<DflTeam> dflTeamsList = dflTeamRepository.findAll();
		
		Map<String, DflTeam> dflTeams = dflTeamsList.stream().collect(Collectors.toMap(DflTeam::getTeamCode, item -> item));
		
		return createLadder(dflLadder, dflTeams);
	}
	
	private List<Ladder> createLadder(List<DflLadder> dflLadder, Map<String, DflTeam> dflTeams) {
		List<Ladder> ladder = new ArrayList<>();
		
		for(DflLadder team : dflLadder) {
			Ladder l = new Ladder();
			
			l.setRound(team.getRound());
			l.setTeamCode(team.getTeamCode());
			l.setWins(team.getWins());
			l.setLosses(team.getLosses());
			l.setDraws(team.getDraws());
			l.setPointsFor(team.getPointsFor());
			l.setPointsAgainst(team.getPointsAgainst());
			l.setAverageFor(team.getAverageFor());
			l.setAverageAgainst(team.getAverageAgainst());
			l.setPts(team.getPts());
			l.setPercentage(team.getPercentage());
			
			String hashKey = team.getTeamCode();
			l.setDisplayName(dflTeams.get(hashKey).getName());
			
			String teamUri =  "/teams/" + team.getTeamCode().toLowerCase();
			l.setTeamUri(teamUri);
			
			ladder.add(l);
		}
		
		Comparator<Ladder> comparator = Comparator.comparingInt(Ladder::getPts).reversed();
		comparator = comparator.thenComparingDouble(Ladder::getPercentage).reversed();
		comparator = comparator.thenComparingInt(Ladder::getPointsFor).reversed();
		
		ladder.sort(comparator);
		
		return ladder;
	}
}
