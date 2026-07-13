package net.dflmngr.handlers;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

import net.dflmngr.model.entity.DflPlayer;
import net.dflmngr.model.entity.DflRoundInfo;
import net.dflmngr.model.entity.DflRoundMapping;
import net.dflmngr.model.entity.DflSelectedPlayer;
import net.dflmngr.model.entity.DflTeamPlayer;
import net.dflmngr.model.service.AflFixtureService;
import net.dflmngr.model.service.DflEarlyInsAndOutsService;
import net.dflmngr.model.service.DflPlayerService;
import net.dflmngr.model.service.DflRoundInfoService;
import net.dflmngr.model.service.DflSelectedTeamService;
import net.dflmngr.model.service.DflSelectionIdsService;
import net.dflmngr.model.service.DflTeamPlayerService;
import net.dflmngr.model.service.GlobalsService;
import net.dflmngr.service.ServiceFactory;
import net.dflmngr.validation.Emergency;
import net.dflmngr.validation.SelectedTeamValidation;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class SelectedTeamValidationHandlerTest {

	private static final String TEAM_CODE = "TEST";

	@Mock private ServiceFactory serviceFactory;
	@Mock private DflSelectedTeamService dflSelectedTeamService;
	@Mock private DflTeamPlayerService dflTeamPlayerService;
	@Mock private DflPlayerService dflPlayerService;
	@Mock private GlobalsService globalsService;
	@Mock private DflRoundInfoService dflRoundInfoService;
	@Mock private DflEarlyInsAndOutsService dflEarlyInsAndOutsService;
	@Mock private AflFixtureService aflFixtureService;
	@Mock private DflSelectionIdsService dflSelectionIdsService;

	private SelectedTeamValidationHandler handler;

	@BeforeEach
	void setUp() throws Exception {
		when(serviceFactory.createDflSelectedTeamService()).thenReturn(dflSelectedTeamService);
		when(serviceFactory.createDflTeamPlayerService()).thenReturn(dflTeamPlayerService);
		when(serviceFactory.createDflPlayerService()).thenReturn(dflPlayerService);
		when(serviceFactory.createGlobalsService()).thenReturn(globalsService);
		when(serviceFactory.createDflRoundInfoService()).thenReturn(dflRoundInfoService);
		when(serviceFactory.createDflEarlyInsAndOutsService()).thenReturn(dflEarlyInsAndOutsService);
		when(serviceFactory.createAflFixtureService()).thenReturn(aflFixtureService);
		when(serviceFactory.createDflSelectionIdsService()).thenReturn(dflSelectionIdsService);

		setFactoryInstance(serviceFactory);
		try {
			handler = new SelectedTeamValidationHandler();
		} finally {
			setFactoryInstance(null);
		}

		when(aflFixtureService.getAflRoundComplete(anyInt())).thenReturn(false);
		when(dflSelectionIdsService.selectionIdExists(anyInt(), any(), any())).thenReturn(false);

		DflRoundInfo roundInfo = new DflRoundInfo();
		DflRoundMapping mapping = new DflRoundMapping();
		mapping.setAflRound(1);
		List<DflRoundMapping> mappings = new ArrayList<>();
		mappings.add(mapping);
		roundInfo.setRoundMapping(mappings);
		when(dflRoundInfoService.get(anyInt())).thenReturn(roundInfo);
	}

	@AfterEach
	void tearDown() throws Exception {
		setFactoryInstance(null);
	}

	private static void setFactoryInstance(ServiceFactory factory) throws Exception {
		Field field = ServiceFactory.class.getDeclaredField("instance");
		field.setAccessible(true);
		field.set(null, factory);
	}

	private DflSelectedPlayer selectedPlayer(int teamPlayerId, int playerId, int emergency) {
		DflSelectedPlayer player = new DflSelectedPlayer();
		player.setTeamPlayerId(teamPlayerId);
		player.setPlayerId(playerId);
		player.setRound(1);
		player.setTeamCode(TEAM_CODE);
		player.setEmergency(emergency);
		player.setDnp(false);
		return player;
	}

	private void stubTeamPlayer(int teamPlayerId, int playerId, String position) {
		DflTeamPlayer teamPlayer = new DflTeamPlayer();
		teamPlayer.setTeamCode(TEAM_CODE);
		teamPlayer.setTeamPlayerId(teamPlayerId);
		teamPlayer.setPlayerId(playerId);
		when(dflTeamPlayerService.getTeamPlayerForTeam(TEAM_CODE, teamPlayerId)).thenReturn(teamPlayer);

		DflPlayer player = new DflPlayer();
		player.setPlayerId(playerId);
		player.setPosition(position);
		when(dflPlayerService.get(playerId)).thenReturn(player);
	}

	private Map<String, List<Integer>> insAndOuts(List<Integer> ins, List<Integer> outs) {
		Map<String, List<Integer>> map = new HashMap<>();
		map.put("in", ins);
		map.put("out", outs);
		return map;
	}

	@Test
	void execute_shouldFailTeamPlayerCheck_whenSelectedPlayerDoesNotExist() {
		when(globalsService.getCurrentRound()).thenReturn("1");
		// no team player stubbed for 30 -> lookup returns null

		SelectedTeamValidation result = handler.execute(1, TEAM_CODE,
				insAndOuts(new ArrayList<>(List.of(30)), new ArrayList<>()), new ArrayList<>(), "noid");

		assertNotNull(result);
		assertFalse(result.isValid());
		assertFalse(result.teamPlayerCheckOk);
		assertFalse(result.unknownError);
	}

	@Test
	void execute_shouldFailTeamPlayerCheck_whenSelectedPlayerOutsideRange() {
		when(globalsService.getCurrentRound()).thenReturn("1");

		SelectedTeamValidation result = handler.execute(1, TEAM_CODE,
				insAndOuts(new ArrayList<>(List.of(50)), new ArrayList<>()), new ArrayList<>(), "noid");

		assertNotNull(result);
		assertFalse(result.isValid());
		assertFalse(result.teamPlayerCheckOk);
		assertFalse(result.unknownError);
		verify(dflTeamPlayerService, never()).getTeamPlayerForTeam(anyString(), anyInt());
	}

	@Test
	void execute_shouldAttachDroppedWarnPlayers_whenDroppedPlayerNotSelected() {
		when(globalsService.getCurrentRound()).thenReturn("2");

		List<DflSelectedPlayer> previousTeam = new ArrayList<>();
		previousTeam.add(selectedPlayer(5, 105, 0));
		when(dflSelectedTeamService.getSelectedTeamForRound(1, TEAM_CODE)).thenReturn(previousTeam);

		stubTeamPlayer(5, 105, "mid");
		stubTeamPlayer(9, 109, "fwd");

		SelectedTeamValidation result = handler.execute(2, TEAM_CODE,
				insAndOuts(new ArrayList<>(), new ArrayList<>(List.of(9))), new ArrayList<>(), "noid");

		assertNotNull(result);
		assertTrue(result.droppedWarning);
		assertNotNull(result.droppedWarnPlayers);
		assertEquals(1, result.droppedWarnPlayers.size());
		assertEquals(109, result.droppedWarnPlayers.get(0).getPlayerId());
	}

	@Test
	void execute_shouldNotAddEmergency_whenPlayerAlreadySelected() {
		when(globalsService.getCurrentRound()).thenReturn("2");

		List<DflSelectedPlayer> previousTeam = new ArrayList<>();
		previousTeam.add(selectedPlayer(7, 107, 0));
		when(dflSelectedTeamService.getSelectedTeamForRound(1, TEAM_CODE)).thenReturn(previousTeam);

		stubTeamPlayer(7, 107, "mid");

		SelectedTeamValidation result = handler.execute(2, TEAM_CODE,
				insAndOuts(new ArrayList<>(), new ArrayList<>()),
				new ArrayList<>(List.of(new Emergency(7, 1))), "noid");

		assertNotNull(result);
		// the already-selected emergency must not be added to the team again:
		// one lookup in the emergency pre-check plus one in validateTeam
		verify(dflTeamPlayerService, times(2)).getTeamPlayerForTeam(TEAM_CODE, 7);
	}
}
