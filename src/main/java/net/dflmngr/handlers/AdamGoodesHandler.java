package net.dflmngr.handlers;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;

import net.dflmngr.model.entity.DflAdamGoodes;
import net.dflmngr.model.entity.DflPlayer;
import net.dflmngr.model.entity.DflPlayerScores;
import net.dflmngr.model.entity.DflSelectedPlayer;
import net.dflmngr.model.service.DflPlayerScoresService;
import net.dflmngr.model.service.DflPlayerService;
import net.dflmngr.model.service.DflSelectedTeamService;

public class AdamGoodesHandler extends BaseHandler {

	String emailOverride;

	List<DflAdamGoodes> medalStandings;
	List<DflAdamGoodes> topFirstYears;

	DflPlayerService dflPlayerService;
	DflPlayerScoresService dflPlayerScoresService;
	DflSelectedTeamService dflSelectedTeamService;

	public AdamGoodesHandler() {
		super("AdamGoodesHandler");
		medalStandings = new ArrayList<>();
		topFirstYears = new ArrayList<>();

		dflPlayerService = serviceFactory.createDflPlayerService();
		dflPlayerScoresService = serviceFactory.createDflPlayerScoresService();
		dflSelectedTeamService = serviceFactory.createDflSelectedTeamService();
	}

	public void execute(int round) {

		try{
			ensureLoggingConfigured();
			
			List<DflPlayer> adamGoodesEligible = dflPlayerService.getAdamGoodesEligible();
			
			List<Map<Integer, DflPlayerScores>> playerScores = new ArrayList<>();
			
			for(int i = 1; i <= round; i++) {
				playerScores.add(dflPlayerScoresService.getForRoundWithKey(i));
			}
			
			calculateStandings(round, adamGoodesEligible, playerScores);
			
			dflPlayerService.close();
			dflPlayerScoresService.close();
			dflSelectedTeamService.close();
	
		} catch (Exception ex) {
			loggerUtils.logException("Error in AdamGoodesHandler.execute(), round=" + round, ex);
		}
	}
	
	public List<DflAdamGoodes> getMedalStandings() {
		return medalStandings;
	}
	
	public List<DflAdamGoodes> getTopFirstYears() {
		return topFirstYears;
	}
	
	private void calculateStandings(int round, List<DflPlayer> adamGoodesEligible, List<Map<Integer, DflPlayerScores>> playerScores) {
		
		for(DflPlayer player : adamGoodesEligible) {
			int adamGoodesTotal = 0;
			int scoreTotal = 0;
			String teamCode = "";
			int teamPlayerId = 0;
			for(Map<Integer, DflPlayerScores> roundScores : playerScores) {
				if(roundScores.containsKey(player.getPlayerId())) {
					DflPlayerScores playerScore = roundScores.get(player.getPlayerId());
					Map<Integer, DflSelectedPlayer> dflSelectedPlayers = dflSelectedTeamService.getForRoundWithKey(playerScore.getRound());
					
					if(dflSelectedPlayers.containsKey(player.getPlayerId())) {
						adamGoodesTotal = adamGoodesTotal + playerScore.getScore();
					}
					
					scoreTotal = scoreTotal + playerScore.getScore();
					teamCode = playerScore.getTeamCode();
					teamPlayerId = playerScore.getTeamPlayerId();
				}
			}
			
			if(scoreTotal > 0) {
				DflAdamGoodes topFirstYearsTotal = new DflAdamGoodes();
				topFirstYearsTotal.setRound(round);
				topFirstYearsTotal.setPlayerId(player.getPlayerId());
				topFirstYearsTotal.setTeamCode(teamCode);
				topFirstYearsTotal.setTeamPlayerId(teamPlayerId);
				topFirstYearsTotal.setTotalScore(scoreTotal);
				
				topFirstYears.add(topFirstYearsTotal);
			}
				
			if(adamGoodesTotal > 0) {
				DflAdamGoodes adamGoodes = new DflAdamGoodes();
				adamGoodes.setRound(round);
				adamGoodes.setPlayerId(player.getPlayerId());
				adamGoodes.setTeamCode(teamCode);
				adamGoodes.setTeamPlayerId(teamPlayerId);
				adamGoodes.setTotalScore(adamGoodesTotal);
				
				medalStandings.add(adamGoodes);
			}
		}
		
		Collections.sort(medalStandings, Collections.reverseOrder());
		Collections.sort(topFirstYears, Collections.reverseOrder());
	}

	private void report() {		
		loggerUtils.log("info", "Adam Goodes top 5");
		for(int i = 0; i < 5; i++) {
			if(i < medalStandings.size()) {
				DflAdamGoodes standing = medalStandings.get(i);
				loggerUtils.log("info", "{}. {}, {}, {} - {}",
								i+1, standing.getPlayerId(), standing.getTeamCode(),  standing.getTeamPlayerId(), standing.getTotalScore());
			} else {
				break;
			}
		}
		
		loggerUtils.log("info", "Top 5 first year players");
		for(int i = 0; i < 5; i++) {
			if(i < topFirstYears.size()) {
				DflAdamGoodes topFirstYear = topFirstYears.get(i);
				loggerUtils.log("info", "{}. {}, {}, {} - {}",
								i+1, topFirstYear.getPlayerId(), topFirstYear.getTeamCode(),  topFirstYear.getTeamPlayerId(), topFirstYear.getTotalScore());
			} else {
				break;
			}
		}
	}
	
	public static void main(String[] args) {
		
		Options options = new Options();
		
		Option roundOpt  = Option.builder("r").argName("round").hasArg().desc("round to run to").type(Number.class).required().build();
		
		options.addOption(roundOpt);
		
		try {
			int round = 0;
						
			CommandLineParser parser = new DefaultParser();
			CommandLine cli = parser.parse(options, args);
			
			round = ((Number)cli.getParsedOptionValue("r")).intValue();
				
			AdamGoodesHandler adamGoodesHandler = new AdamGoodesHandler();
			adamGoodesHandler.configureLogging("batch.name", "batch-logger", ("AdamGoodesHandler_R" + round));
			adamGoodesHandler.execute(round);
						
			adamGoodesHandler.report();
			
		} catch (ParseException ex) {
			HelpFormatter formatter = new HelpFormatter();
			formatter.printHelp( "adamGoodesHandler", options);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}
}
