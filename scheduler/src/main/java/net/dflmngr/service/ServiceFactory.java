package net.dflmngr.service;

import net.dflmngr.model.service.*;
import net.dflmngr.model.service.impl.*;

/**
 * Factory for creating service instances.
 * Provides a centralized way to create services instead of using 'new ServiceImpl()' everywhere.
 * This makes the code easier to test and maintain.
 */
public class ServiceFactory {

	private static ServiceFactory instance;

	private ServiceFactory() {
	}

	public static synchronized ServiceFactory getInstance() {
		if (instance == null) {
			instance = new ServiceFactory();
		}
		return instance;
	}

	// Team Services
	public DflTeamService createDflTeamService() {
		return new DflTeamServiceImpl();
	}

	public AflTeamService createAflTeamService() {
		return new AflTeamServiceImpl();
	}

	// Player Services
	public DflPlayerService createDflPlayerService() {
		return new DflPlayerServiceImpl();
	}

	public AflPlayerService createAflPlayerService() {
		return new AflPlayerServiceImpl();
	}

	public DflTeamPlayerService createDflTeamPlayerService() {
		return new DflTeamPlayerServiceImpl();
	}

	// Fixture Services
	public DflFixtureService createDflFixtureService() {
		return new DflFixtureServiceImpl();
	}

	public AflFixtureService createAflFixtureService() {
		return new AflFixtureServiceImpl();
	}

	// Score Services
	public DflPlayerScoresService createDflPlayerScoresService() {
		return new DflPlayerScoresServiceImpl();
	}

	public DflTeamScoresService createDflTeamScoresService() {
		return new DflTeamScoresServiceImpl();
	}

	public DflTeamPredictedScoresService createDflTeamPredictedScoresService() {
		return new DflTeamPredictedScoresServiceImpl();
	}

	public DflPlayerPredictedScoresService createDflPlayerPredictedScoresService() {
		return new DflPlayerPredictedScoresServiceImpl();
	}

	public DflPreseasonScoresService createDflPreseasonScoresService() {
		return new DflPreseasonScoresServiceImpl();
	}

	// Round/Info Services
	public DflRoundInfoService createDflRoundInfoService() {
		return new DflRoundInfoServiceImpl();
	}

	public GlobalsService createGlobalsService() {
		return new GlobalsServiceImpl();
	}

	// Selection Services
	public DflSelectedTeamService createDflSelectedTeamService() {
		return new DflSelectedTeamServiceImpl();
	}

	public DflSelectionIdsService createDflSelectionIdsService() {
		return new DflSelectionIdsServiceImpl();
	}

	// Stats Services
	public RawPlayerStatsService createRawPlayerStatsService() {
		return new RawPlayerStatsServiceImpl();
	}

	public StatsRoundPlayerStatsService createStatsRoundPlayerStatsService() {
		return new StatsRoundPlayerStatsServiceImpl();
	}

	// Other Services
	public InsAndOutsService createInsAndOutsService() {
		return new InsAndOutsServiceImpl();
	}

	public DflEarlyInsAndOutsService createDflEarlyInsAndOutsService() {
		return new DflEarlyInsAndOutsServiceImpl();
	}

	public DflLadderService createDflLadderService() {
		return new DflLadderServiceImpl();
	}

	public DflMatthewAllenService createDflMatthewAllenService() {
		return new DflMatthewAllenServiceImpl();
	}

	public DflBest22Service createDflBest22Service() {
		return new DflBest22ServiceImpl();
	}

	public DflUnmatchedPlayerService createDflUnmatchedPlayerService() {
		return new DflUnmatchedPlayerServiceImpl();
	}
}
