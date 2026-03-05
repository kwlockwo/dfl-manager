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

import net.dflmngr.model.entity.DflCallumChambers;
import net.dflmngr.model.entity.DflPlayer;
import net.dflmngr.model.entity.DflPlayerScores;
import net.dflmngr.model.entity.DflTeamPlayer;
import net.dflmngr.model.service.DflPlayerScoresService;
import net.dflmngr.model.service.DflPlayerService;
import net.dflmngr.model.service.DflTeamPlayerService;
import net.dflmngr.model.service.GlobalsService;

public class CallumChambersHandler extends BaseHandler {

	String emailOverride;

	List<DflCallumChambers> medalStandings;

	DflPlayerService dflPlayerService;
	DflPlayerScoresService dflPlayerScoresService;
	DflTeamPlayerService dflTeamPlayerService;
	GlobalsService globalsService;

	public CallumChambersHandler() {
		super("CallumChambersHandler");
		medalStandings = new ArrayList<>();

		dflPlayerService = serviceFactory.createDflPlayerService();
		dflPlayerScoresService = serviceFactory.createDflPlayerScoresService();
		dflTeamPlayerService = serviceFactory.createDflTeamPlayerService();
		globalsService = serviceFactory.createGlobalsService();
	}

	public void execute(int round) {

		try{
			ensureLoggingConfigured();

			List<DflPlayer> players = dflPlayerService.findAll();
			List<Map<Integer, DflPlayerScores>> playerScores = new ArrayList<>();

			for(int i = 1; i <= round; i++) {
				playerScores.add(dflPlayerScoresService.getForRoundWithKey(i));
			}

			calculateStandings(round, players, playerScores);

			dflPlayerService.close();
			dflPlayerScoresService.close();
			dflTeamPlayerService.close();
			globalsService.close();

		} catch (Exception ex) {
			loggerUtils.logException("Error in CallumChambersHandler.execute(), round=" + round, ex);
		}
	}

	public List<DflCallumChambers> getMedalStandings() {
		return medalStandings;
	}

	private void calculateStandings(int round, List<DflPlayer> players, List<Map<Integer, DflPlayerScores>> playerScores) {

		Map<String, String> draftOrder = globalsService.getDraftOrder();

		for(DflPlayer player : players) {
			int score = 0;

			DflTeamPlayer teamPlayer = dflTeamPlayerService.get(player.getPlayerId());

			if(teamPlayer != null) {
				String teamCode = teamPlayer.getTeamCode();
				int teamPlayerId = teamPlayer.getTeamPlayerId();

				for(Map<Integer, DflPlayerScores> roundScores : playerScores) {
					if(roundScores.containsKey(player.getPlayerId())) {
						score = score + roundScores.get(player.getPlayerId()).getScore();
					}
				}

				DflCallumChambers callumChambersTotal = new DflCallumChambers();
				callumChambersTotal.setRound(round);
				callumChambersTotal.setPlayerId(player.getPlayerId());
				callumChambersTotal.setTeamCode(teamCode);
				String draftOrderValue = draftOrder.get(teamCode);
				if (draftOrderValue == null) {
					loggerUtils.log("warn", "No draft order found for teamCode: {}", teamCode);
					continue;
				}
				callumChambersTotal.setDraftOrder(Integer.parseInt(draftOrderValue));
				callumChambersTotal.setTeamPlayerId(teamPlayerId);
				callumChambersTotal.setTotalScore(score);

				medalStandings.add(callumChambersTotal);
			}
		}

		Collections.sort(medalStandings, Collections.reverseOrder());
	}

	public void report() {
		loggerUtils.log("info", "Callum Chambers top 5 for round");
		for(int i = 0; i < Math.min(5, medalStandings.size()); i++) {
			DflCallumChambers standing = medalStandings.get(i);
			loggerUtils.log("info", "{}. {}, {}, {}, {} - {}",
							i+1, standing.getPlayerId(), standing.getTeamCode(), standing.getDraftOrder(),
							standing.getTeamPlayerId(), standing.getTotalScore());
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

			CallumChambersHandler callumChambersHandler = new CallumChambersHandler();
			callumChambersHandler.configureLogging("batch.name", "batch-logger", ("CallumChambersHandler_R" + round));
			callumChambersHandler.execute(round);

			callumChambersHandler.report();

		} catch (ParseException ex) {
			HelpFormatter formatter = new HelpFormatter();
			formatter.printHelp( "adamGoodesHandler", options);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}
}
