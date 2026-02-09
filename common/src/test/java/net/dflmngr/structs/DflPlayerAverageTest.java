package net.dflmngr.structs;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

class DflPlayerAverageTest {

	@Test
	void constructor_shouldCreateInstanceWithDefaultValues() {
		DflPlayerAverage player = new DflPlayerAverage();

		assertEquals(0, player.getPlayerId());
		assertEquals(0, player.getTeamPlayerId());
		assertNull(player.getTeamCode());
		assertEquals(0, player.getAverage());
	}

	@Test
	void setters_shouldSetValues() {
		DflPlayerAverage player = new DflPlayerAverage();

		player.setPlayerId(123);
		player.setTeamPlayerId(456);
		player.setTeamCode("TEST");
		player.setAverage(85);

		assertEquals(123, player.getPlayerId());
		assertEquals(456, player.getTeamPlayerId());
		assertEquals("TEST", player.getTeamCode());
		assertEquals(85, player.getAverage());
	}

	@Test
	void equals_shouldReturnTrueForSameObject() {
		DflPlayerAverage player = new DflPlayerAverage();

		assertTrue(player.equals(player));
	}

	@Test
	void equals_shouldReturnFalseForNull() {
		DflPlayerAverage player = new DflPlayerAverage();

		assertFalse(player.equals(null));
	}

	@Test
	void equals_shouldReturnFalseForDifferentClass() {
		DflPlayerAverage player = new DflPlayerAverage();

		assertFalse(player.equals("not a player"));
	}

	@Test
	void equals_shouldReturnTrueForEqualObjects() {
		DflPlayerAverage player1 = new DflPlayerAverage();
		player1.setPlayerId(123);
		player1.setTeamPlayerId(456);
		player1.setTeamCode("TEST");
		player1.setAverage(85);

		DflPlayerAverage player2 = new DflPlayerAverage();
		player2.setPlayerId(123);
		player2.setTeamPlayerId(456);
		player2.setTeamCode("TEST");
		player2.setAverage(85);

		assertTrue(player1.equals(player2));
		assertTrue(player2.equals(player1));
	}

	@Test
	void equals_shouldReturnFalseForDifferentPlayerId() {
		DflPlayerAverage player1 = new DflPlayerAverage();
		player1.setPlayerId(123);

		DflPlayerAverage player2 = new DflPlayerAverage();
		player2.setPlayerId(456);

		assertFalse(player1.equals(player2));
	}

	@Test
	void equals_shouldReturnFalseForDifferentTeamPlayerId() {
		DflPlayerAverage player1 = new DflPlayerAverage();
		player1.setTeamPlayerId(123);

		DflPlayerAverage player2 = new DflPlayerAverage();
		player2.setTeamPlayerId(456);

		assertFalse(player1.equals(player2));
	}

	@Test
	void equals_shouldReturnFalseForDifferentTeamCode() {
		DflPlayerAverage player1 = new DflPlayerAverage();
		player1.setTeamCode("TEST1");

		DflPlayerAverage player2 = new DflPlayerAverage();
		player2.setTeamCode("TEST2");

		assertFalse(player1.equals(player2));
	}

	@Test
	void equals_shouldReturnFalseWhenOneTeamCodeIsNull() {
		DflPlayerAverage player1 = new DflPlayerAverage();
		player1.setTeamCode("TEST");

		DflPlayerAverage player2 = new DflPlayerAverage();
		player2.setTeamCode(null);

		assertFalse(player1.equals(player2));
	}

	@Test
	void equals_shouldReturnFalseForDifferentAverage() {
		DflPlayerAverage player1 = new DflPlayerAverage();
		player1.setAverage(85);

		DflPlayerAverage player2 = new DflPlayerAverage();
		player2.setAverage(90);

		assertFalse(player1.equals(player2));
	}

	@Test
	void hashCode_shouldBeConsistentWithEquals() {
		DflPlayerAverage player1 = new DflPlayerAverage();
		player1.setPlayerId(123);
		player1.setTeamPlayerId(456);
		player1.setTeamCode("TEST");
		player1.setAverage(85);

		DflPlayerAverage player2 = new DflPlayerAverage();
		player2.setPlayerId(123);
		player2.setTeamPlayerId(456);
		player2.setTeamCode("TEST");
		player2.setAverage(85);

		assertEquals(player1.hashCode(), player2.hashCode());
	}

	@Test
	void compareTo_shouldReturnNegativeWhenLowerAverage() {
		DflPlayerAverage player1 = new DflPlayerAverage();
		player1.setAverage(80);

		DflPlayerAverage player2 = new DflPlayerAverage();
		player2.setAverage(90);

		assertTrue(player1.compareTo(player2) < 0);
	}

	@Test
	void compareTo_shouldReturnPositiveWhenHigherAverage() {
		DflPlayerAverage player1 = new DflPlayerAverage();
		player1.setAverage(90);

		DflPlayerAverage player2 = new DflPlayerAverage();
		player2.setAverage(80);

		assertTrue(player1.compareTo(player2) > 0);
	}

	@Test
	void compareTo_shouldReturnZeroWhenEqualAverage() {
		DflPlayerAverage player1 = new DflPlayerAverage();
		player1.setAverage(85);

		DflPlayerAverage player2 = new DflPlayerAverage();
		player2.setAverage(85);

		assertEquals(0, player1.compareTo(player2));
	}

	@Test
	void compare_shouldReturnNegativeWhenFirstLowerAverage() {
		DflPlayerAverage comparator = new DflPlayerAverage();

		DflPlayerAverage player1 = new DflPlayerAverage();
		player1.setAverage(80);

		DflPlayerAverage player2 = new DflPlayerAverage();
		player2.setAverage(90);

		assertTrue(comparator.compare(player1, player2) < 0);
	}

	@Test
	void compare_shouldReturnPositiveWhenFirstHigherAverage() {
		DflPlayerAverage comparator = new DflPlayerAverage();

		DflPlayerAverage player1 = new DflPlayerAverage();
		player1.setAverage(90);

		DflPlayerAverage player2 = new DflPlayerAverage();
		player2.setAverage(80);

		assertTrue(comparator.compare(player1, player2) > 0);
	}

	@Test
	void compare_shouldReturnZeroWhenEqualAverage() {
		DflPlayerAverage comparator = new DflPlayerAverage();

		DflPlayerAverage player1 = new DflPlayerAverage();
		player1.setAverage(85);

		DflPlayerAverage player2 = new DflPlayerAverage();
		player2.setAverage(85);

		assertEquals(0, comparator.compare(player1, player2));
	}

	@Test
	void toString_shouldContainAllFields() {
		DflPlayerAverage player = new DflPlayerAverage();
		player.setPlayerId(123);
		player.setTeamPlayerId(456);
		player.setTeamCode("TEST");
		player.setAverage(85);

		String result = player.toString();

		assertTrue(result.contains("playerId=123"));
		assertTrue(result.contains("teamPlayerId=456"));
		assertTrue(result.contains("teamCode=TEST"));
		assertTrue(result.contains("average=85"));
	}
}
