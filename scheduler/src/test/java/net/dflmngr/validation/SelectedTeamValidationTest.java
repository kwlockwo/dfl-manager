package net.dflmngr.validation;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.dflmngr.model.entity.DflPlayer;

class SelectedTeamValidationTest {

	private SelectedTeamValidation validation;

	@BeforeEach
	void setUp() {
		validation = new SelectedTeamValidation();
	}

	// Constructor tests

	@Test
	void constructor_shouldInitializeWithDefaultValues() {
		SelectedTeamValidation v = new SelectedTeamValidation();

		// Should be false by default
		assertFalse(v.earlyGames);
		assertFalse(v.playedSelections);
		assertFalse(v.ffCheckOk);
		assertFalse(v.fwdCheckOk);
		assertFalse(v.rckCheckOk);
		assertFalse(v.midCheckOk);
		assertFalse(v.fbCheckOk);
		assertFalse(v.defCheckOk);
		assertFalse(v.benchCheckOk);
		assertFalse(v.teamPlayerCheckOk);
		assertFalse(v.emergencyFfWarning);
		assertFalse(v.emergencyFwdWarning);
		assertFalse(v.emergencyRckWarning);
		assertFalse(v.emergencyMidWarning);
		assertFalse(v.emergencyFbWarning);
		assertFalse(v.emergencyDefWarning);
		assertFalse(v.duplicateSubmissionId);
		assertFalse(v.unknownError);

		// Should be true by default
		assertTrue(v.selectionFileMissing);
		assertTrue(v.lockedOut);
		assertTrue(v.roundCompleted);
	}

	// isValid() tests - Early games scenario

	@Test
	void isValid_shouldReturnTrue_whenEarlyGamesAndNoPlayedSelections() {
		validation.earlyGames = true;
		validation.playedSelections = false;

		assertTrue(validation.isValid());
	}

	@Test
	void isValid_shouldReturnFalse_whenEarlyGamesAndPlayedSelections() {
		validation.earlyGames = true;
		validation.playedSelections = true;

		assertFalse(validation.isValid());
	}

	// isValid() tests - Normal scenario (all checks must pass)

	@Test
	void isValid_shouldReturnTrue_whenAllChecksPass() {
		validation.earlyGames = false;
		validation.selectionFileMissing = false;
		validation.ffCheckOk = true;
		validation.fwdCheckOk = true;
		validation.rckCheckOk = true;
		validation.midCheckOk = true;
		validation.fbCheckOk = true;
		validation.defCheckOk = true;
		validation.benchCheckOk = true;
		validation.teamPlayerCheckOk = true;
		validation.unknownError = false;
		validation.lockedOut = false;
		validation.roundCompleted = false;
		validation.duplicateSubmissionId = false;

		assertTrue(validation.isValid());
	}

	@Test
	void isValid_shouldReturnFalse_whenSelectionFileMissing() {
		setAllValidationChecksToPass();
		validation.selectionFileMissing = true;

		assertFalse(validation.isValid());
	}

	@Test
	void isValid_shouldReturnFalse_whenFfCheckFails() {
		setAllValidationChecksToPass();
		validation.ffCheckOk = false;

		assertFalse(validation.isValid());
	}

	@Test
	void isValid_shouldReturnFalse_whenFwdCheckFails() {
		setAllValidationChecksToPass();
		validation.fwdCheckOk = false;

		assertFalse(validation.isValid());
	}

	@Test
	void isValid_shouldReturnFalse_whenRckCheckFails() {
		setAllValidationChecksToPass();
		validation.rckCheckOk = false;

		assertFalse(validation.isValid());
	}

	@Test
	void isValid_shouldReturnFalse_whenMidCheckFails() {
		setAllValidationChecksToPass();
		validation.midCheckOk = false;

		assertFalse(validation.isValid());
	}

	@Test
	void isValid_shouldReturnFalse_whenFbCheckFails() {
		setAllValidationChecksToPass();
		validation.fbCheckOk = false;

		assertFalse(validation.isValid());
	}

	@Test
	void isValid_shouldReturnFalse_whenDefCheckFails() {
		setAllValidationChecksToPass();
		validation.defCheckOk = false;

		assertFalse(validation.isValid());
	}

	@Test
	void isValid_shouldReturnFalse_whenBenchCheckFails() {
		setAllValidationChecksToPass();
		validation.benchCheckOk = false;

		assertFalse(validation.isValid());
	}

	@Test
	void isValid_shouldReturnFalse_whenTeamPlayerCheckFails() {
		setAllValidationChecksToPass();
		validation.teamPlayerCheckOk = false;

		assertFalse(validation.isValid());
	}

	@Test
	void isValid_shouldReturnFalse_whenUnknownError() {
		setAllValidationChecksToPass();
		validation.unknownError = true;

		assertFalse(validation.isValid());
	}

	@Test
	void isValid_shouldReturnFalse_whenLockedOut() {
		setAllValidationChecksToPass();
		validation.lockedOut = true;

		assertFalse(validation.isValid());
	}

	@Test
	void isValid_shouldReturnFalse_whenRoundCompleted() {
		setAllValidationChecksToPass();
		validation.roundCompleted = true;

		assertFalse(validation.isValid());
	}

	@Test
	void isValid_shouldReturnFalse_whenDuplicateSubmissionId() {
		setAllValidationChecksToPass();
		validation.duplicateSubmissionId = true;

		assertFalse(validation.isValid());
	}

	// areWarnings() tests

	@Test
	void areWarnings_shouldReturnFalse_whenNoWarnings() {
		validation.emergencyFfWarning = false;
		validation.emergencyFwdWarning = false;
		validation.emergencyRckWarning = false;
		validation.emergencyMidWarning = false;
		validation.emergencyFbWarning = false;
		validation.emergencyDefWarning = false;
		validation.selectedWarning = false;
		validation.droppedWarning = false;
		validation.duplicateIns = false;
		validation.duplicateOuts = false;
		validation.duplicateEmgs = false;

		assertFalse(validation.areWarnings());
	}

	@Test
	void areWarnings_shouldReturnTrue_whenEmergencyFfWarning() {
		validation.emergencyFfWarning = true;

		assertTrue(validation.areWarnings());
	}

	@Test
	void areWarnings_shouldReturnTrue_whenEmergencyFwdWarning() {
		validation.emergencyFwdWarning = true;

		assertTrue(validation.areWarnings());
	}

	@Test
	void areWarnings_shouldReturnTrue_whenEmergencyRckWarning() {
		validation.emergencyRckWarning = true;

		assertTrue(validation.areWarnings());
	}

	@Test
	void areWarnings_shouldReturnTrue_whenEmergencyMidWarning() {
		validation.emergencyMidWarning = true;

		assertTrue(validation.areWarnings());
	}

	@Test
	void areWarnings_shouldReturnTrue_whenEmergencyFbWarning() {
		validation.emergencyFbWarning = true;

		assertTrue(validation.areWarnings());
	}

	@Test
	void areWarnings_shouldReturnTrue_whenEmergencyDefWarning() {
		validation.emergencyDefWarning = true;

		assertTrue(validation.areWarnings());
	}

	@Test
	void areWarnings_shouldReturnTrue_whenSelectedWarning() {
		validation.selectedWarning = true;

		assertTrue(validation.areWarnings());
	}

	@Test
	void areWarnings_shouldReturnTrue_whenDroppedWarning() {
		validation.droppedWarning = true;

		assertTrue(validation.areWarnings());
	}

	@Test
	void areWarnings_shouldReturnTrue_whenDuplicateIns() {
		validation.duplicateIns = true;

		assertTrue(validation.areWarnings());
	}

	@Test
	void areWarnings_shouldReturnTrue_whenDuplicateOuts() {
		validation.duplicateOuts = true;

		assertTrue(validation.areWarnings());
	}

	@Test
	void areWarnings_shouldReturnTrue_whenDuplicateEmgs() {
		validation.duplicateEmgs = true;

		assertTrue(validation.areWarnings());
	}

	@Test
	void areWarnings_shouldReturnTrue_whenMultipleWarnings() {
		validation.emergencyFfWarning = true;
		validation.selectedWarning = true;
		validation.duplicateIns = true;

		assertTrue(validation.areWarnings());
	}

	// Getter/Setter tests

	@Test
	void roundGetterSetter_shouldWorkCorrectly() {
		validation.setRound(5);

		assertEquals(5, validation.getRound());
	}

	@Test
	void teamCodeGetterSetter_shouldWorkCorrectly() {
		validation.setTeamCode("ABC");

		assertEquals("ABC", validation.getTeamCode());
	}

	@Test
	void insAndOutsGetterSetter_shouldWorkCorrectly() {
		Map<String, List<Integer>> insAndOuts = new HashMap<>();
		List<Integer> ins = new ArrayList<>();
		ins.add(1);
		ins.add(2);
		insAndOuts.put("ins", ins);

		validation.setInsAndOuts(insAndOuts);

		assertEquals(insAndOuts, validation.getInsAndOuts());
		assertEquals(2, validation.getInsAndOuts().get("ins").size());
	}

	@Test
	void emergenciesGetterSetter_shouldWorkCorrectly() {
		List<Double> emergencies = new ArrayList<>();
		emergencies.add(1.0);
		emergencies.add(2.0);

		validation.setEmergencies(emergencies);

		assertEquals(emergencies, validation.getEmergencies());
		assertEquals(2, validation.getEmergencies().size());
	}

	@Test
	void fromGetterSetter_shouldWorkCorrectly() {
		validation.setFrom("email@example.com");

		assertEquals("email@example.com", validation.getFrom());
	}

	// Edge case tests

	@Test
	void isValid_shouldHandleMultipleFailures() {
		// Multiple checks failing
		validation.earlyGames = false;
		validation.selectionFileMissing = false;
		validation.ffCheckOk = false;  // Fails
		validation.fwdCheckOk = false; // Fails
		validation.rckCheckOk = true;
		validation.midCheckOk = true;
		validation.fbCheckOk = true;
		validation.defCheckOk = true;
		validation.benchCheckOk = true;
		validation.teamPlayerCheckOk = true;
		validation.unknownError = false;
		validation.lockedOut = false;
		validation.roundCompleted = false;
		validation.duplicateSubmissionId = false;

		assertFalse(validation.isValid());
	}

	@Test
	void isValid_earlyGamesTakesPrecedence() {
		// Even with all normal checks passing, early games path is taken
		setAllValidationChecksToPass();
		validation.earlyGames = true;
		validation.playedSelections = false;

		assertTrue(validation.isValid());
	}

	@Test
	void areWarnings_shouldHandleAllWarnings() {
		// Set all warnings
		validation.emergencyFfWarning = true;
		validation.emergencyFwdWarning = true;
		validation.emergencyRckWarning = true;
		validation.emergencyMidWarning = true;
		validation.emergencyFbWarning = true;
		validation.emergencyDefWarning = true;
		validation.selectedWarning = true;
		validation.droppedWarning = true;
		validation.duplicateIns = true;
		validation.duplicateOuts = true;
		validation.duplicateEmgs = true;

		assertTrue(validation.areWarnings());
	}

	@Test
	void playerLists_shouldBeAccessible() {
		// Verify all player list fields are accessible
		validation.ffPlayers = new ArrayList<>();
		validation.fwdPlayers = new ArrayList<>();
		validation.midPlayers = new ArrayList<>();
		validation.defPlayers = new ArrayList<>();
		validation.fbPlayers = new ArrayList<>();
		validation.rckPlayers = new ArrayList<>();
		validation.benchPlayers = new ArrayList<>();
		validation.emgFfPlayers = new ArrayList<>();
		validation.emgFwdPlayers = new ArrayList<>();
		validation.emgMidPlayers = new ArrayList<>();
		validation.emgDefPlayers = new ArrayList<>();
		validation.emgFbPlayers = new ArrayList<>();
		validation.emgRckPlayers = new ArrayList<>();
		validation.selectedWarnPlayers = new ArrayList<>();
		validation.droppedWarnPlayers = new ArrayList<>();
		validation.dupInPlayers = new ArrayList<>();
		validation.dupOutPlayers = new ArrayList<>();
		validation.dupEmgPlayers = new ArrayList<>();

		// All should be non-null empty lists
		assertNotNull(validation.ffPlayers);
		assertNotNull(validation.fwdPlayers);
		assertNotNull(validation.midPlayers);
		assertNotNull(validation.defPlayers);
		assertNotNull(validation.fbPlayers);
		assertNotNull(validation.rckPlayers);
		assertNotNull(validation.benchPlayers);
		assertNotNull(validation.emgFfPlayers);
		assertNotNull(validation.emgFwdPlayers);
		assertNotNull(validation.emgMidPlayers);
		assertNotNull(validation.emgDefPlayers);
		assertNotNull(validation.emgFbPlayers);
		assertNotNull(validation.emgRckPlayers);
		assertNotNull(validation.selectedWarnPlayers);
		assertNotNull(validation.droppedWarnPlayers);
		assertNotNull(validation.dupInPlayers);
		assertNotNull(validation.dupOutPlayers);
		assertNotNull(validation.dupEmgPlayers);
	}

	// Helper method

	private void setAllValidationChecksToPass() {
		validation.earlyGames = false;
		validation.selectionFileMissing = false;
		validation.ffCheckOk = true;
		validation.fwdCheckOk = true;
		validation.rckCheckOk = true;
		validation.midCheckOk = true;
		validation.fbCheckOk = true;
		validation.defCheckOk = true;
		validation.benchCheckOk = true;
		validation.teamPlayerCheckOk = true;
		validation.unknownError = false;
		validation.lockedOut = false;
		validation.roundCompleted = false;
		validation.duplicateSubmissionId = false;
	}
}
