package net.dflmngr.services;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import net.dflmngr.model.entity.DflFixture;
import net.dflmngr.model.entity.DflTeam;
import net.dflmngr.model.entity.DflTeamScores;
import net.dflmngr.model.web.GameFixture;
import net.dflmngr.model.web.RoundFixtures;
import net.dflmngr.repositories.DflFixtureRepository;
import net.dflmngr.repositories.DflSelectedPlayerRepository;
import net.dflmngr.repositories.DflTeamRepository;
import net.dflmngr.repositories.DflTeamScoresRepository;

@Service
public class FixtureService {
	
	private static final Logger logger = LoggerFactory.getLogger(FixtureService.class);
	
	private final DflFixtureRepository dflFixtureRepository;
	private final DflTeamScoresRepository dflTeamScoresRepository;
	private final DflTeamRepository dflTeamRepository;
	private final DflSelectedPlayerRepository dflSelectedPlayerRepository;

	public FixtureService(DflFixtureRepository dflFixtureRepository, DflTeamScoresRepository dflTeamScoresRepository, DflTeamRepository dflTeamRepository, DflSelectedPlayerRepository dflSelectedPlayerRepository) {
		this.dflFixtureRepository = dflFixtureRepository;
		this.dflTeamScoresRepository = dflTeamScoresRepository;
		this.dflTeamRepository = dflTeamRepository;
		this.dflSelectedPlayerRepository = dflSelectedPlayerRepository;
	}
	
	public List<RoundFixtures> getFixtures() {
		
		List<RoundFixtures> fixtures = new ArrayList<>();
		
		List<DflFixture> dflFixtures = dflFixtureRepository.findAll();
				
		List<DflTeamScores> dflTeamScoresList = dflTeamScoresRepository.findAll();
		List<DflTeam> dflTeamsList = dflTeamRepository.findAll();
		
		Map<String, DflTeamScores> dflTeamScores = dflTeamScoresList.stream().collect(Collectors.toMap(
												  teamScore -> teamScore.getTeamCode() + ":" + teamScore.getRound(), teamScore -> teamScore));
		Map<String, DflTeam> dflTeams = dflTeamsList.stream().collect(Collectors.toMap(DflTeam::getTeamCode, team -> team));
		
		Comparator<DflFixture> dflFixtureComparator = Comparator.comparingInt(DflFixture::getRound).thenComparingInt(DflFixture::getGame);
		dflFixtures.sort(dflFixtureComparator);
		
		int currentRound = 1;
		RoundFixtures roundFixtures = new RoundFixtures();
		List<GameFixture> games = new ArrayList<>();
		
		Comparator<GameFixture> gamesComparator = Comparator.comparingInt(GameFixture::getGame);
		
		logger.debug("DFL Fixtures: {}", dflFixtures);
		logger.debug("DFL Teams: {}", dflTeams);
		
		for(DflFixture dflFixture : dflFixtures) {			
			GameFixture game = new GameFixture();
			
			logger.debug("Round={}, Game={}, Home={}, Away={}", dflFixture.getRound(), dflFixture.getGame(), dflFixture.getHomeTeam(), dflFixture.getAwayTeam());
			
			game.setGame(dflFixture.getGame());
			game.setHomeTeam(dflFixture.getHomeTeam());
			game.setAwayTeam(dflFixture.getAwayTeam());
			
			String homeHashKey = dflFixture.getHomeTeam() + ":" + dflFixture.getRound();
			String awayHashKey = dflFixture.getAwayTeam() + ":" + dflFixture.getRound();
			
			if(dflTeamScores.containsKey(homeHashKey) && dflTeamScores.containsKey(awayHashKey)) {
				game.setHomeTeamScore(dflTeamScores.get(homeHashKey).getScore());
				game.setAwayTeamScore(dflTeamScores.get(awayHashKey).getScore());
			}
			
			boolean homeTeamSelectionsExist = dflSelectedPlayerRepository.selectedTeamExists(dflFixture.getRound(), dflFixture.getHomeTeam());
			boolean awayTeamSelectionsExist = dflSelectedPlayerRepository.selectedTeamExists(dflFixture.getRound(), dflFixture.getAwayTeam());
			
			if(homeTeamSelectionsExist && awayTeamSelectionsExist) {
				String resultsUri = "/results/" + dflFixture.getRound() + "/" + dflFixture.getGame();
				game.setResultsUri(resultsUri);
			}
			
			logger.debug("Home team details={}", dflTeams.get(dflFixture.getHomeTeam()));
			game.setHomeTeamDisplayName(dflTeams.get(dflFixture.getHomeTeam()).getShortName());
			
			logger.debug("Away team details={}", dflTeams.get(dflFixture.getAwayTeam()));
			game.setAwayTeamDisplayName(dflTeams.get(dflFixture.getAwayTeam()).getShortName());
			
			if(currentRound == dflFixture.getRound()) {
				games.add(game);
			} else {
				games.sort(gamesComparator);
				
				roundFixtures.setRound(currentRound);
				roundFixtures.setGames(games);
				fixtures.add(roundFixtures);
				
				roundFixtures = new RoundFixtures();
				games = new ArrayList<>();
				games.add(game);
				
				currentRound = dflFixture.getRound();				
			}
		}
		
		if(!games.isEmpty()) {
			games.sort(gamesComparator);
			
			roundFixtures.setRound(currentRound);
			roundFixtures.setGames(games);
			fixtures.add(roundFixtures);
		}
		
		Comparator<RoundFixtures> roundsComparator = Comparator.comparingInt(RoundFixtures::getRound);
		fixtures.sort(roundsComparator);
		
		return fixtures;
	}
}
