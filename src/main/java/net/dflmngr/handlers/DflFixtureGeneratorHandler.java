package net.dflmngr.handlers;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import net.dflmngr.model.entity.DflFixture;
import net.dflmngr.model.service.DflFixtureService;
import net.dflmngr.model.service.GlobalsService;

public class DflFixtureGeneratorHandler extends BaseHandler {

	private GlobalsService globalsService;
	private DflFixtureService dflFixtureService;

	public DflFixtureGeneratorHandler() {
		super("DflFixtureGeneratorHandler");
		globalsService = serviceFactory.createGlobalsService();
		dflFixtureService = serviceFactory.createDflFixtureService();
	}

	public void execute() {

		try{
			ensureLoggingConfigured();
			
			loggerUtils.log("info", "DflFixtureGeneratorHandler executing");
			
			loggerUtils.log("info", "Generating DFL Fixture");
			generateFixture();
						
			loggerUtils.log("info", "DflFixtureGeneratorHandler completed");
			
		} catch (Exception ex) {
			loggerUtils.logException("Error in DflFixtureGeneratorHandler.execute()", ex);
		}
	}
	
	private void generateFixture() {
		
		loggerUtils.log("info", "Fetching Global Data");
		Map<Integer, Map<Integer, String[]>> dflFixtureTemplate = globalsService.getDflFixuteTemplate();
		Map<String, String> dlfFixtureOrder = globalsService.getDflFixtureOrder();
		
		List<DflFixture> dflFixture = new ArrayList<>();
		
		for (Map.Entry<Integer,  Map<Integer, String[]>> roundEntry : dflFixtureTemplate.entrySet()) {
		    int round = roundEntry.getKey();
		    Map<Integer, String[]> roundTemplate = roundEntry.getValue();
		    
		    for (Map.Entry<Integer, String[]> gameEntry : roundTemplate.entrySet()) {
			    int game = gameEntry.getKey();
			    String[] teams = gameEntry.getValue();
			    
			    String homeTeamIndex = teams[0];
			    String awayTeamIndex = teams[1];
			    
			    String homeTeam = dlfFixtureOrder.get(homeTeamIndex);
			    String awayTeam = dlfFixtureOrder.get(awayTeamIndex);
			    
			    DflFixture dflGame = new DflFixture();
			    dflGame.setRound(round);
			    dflGame.setGame(game);
			    dflGame.setHomeTeam(homeTeam);
			    dflGame.setAwayTeam(awayTeam);
			    
			    loggerUtils.log("info", "Creating DFL fixture game: {}", dflGame);
			    dflFixture.add(dflGame);
		    }
		}
		
		loggerUtils.log("info", "Saving fixutres in db...");
		dflFixtureService.replaceAll(dflFixture);
		
		loggerUtils.log("info", "DFl fixture generated");
	}
	
	
	public static void main(String[] args) {		
		try {
			
			DflFixtureGeneratorHandler fixuteGenerator = new DflFixtureGeneratorHandler();
			fixuteGenerator.configureLogging("batch.name", "batch-logger", "DflFixtureGenerator");
			fixuteGenerator.execute();

		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

}
