package net.dflmngr.handlers;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import net.dflmngr.exceptions.UnknownPositionException;

class PositionCountsTest {

	private PositionCounts counts;

	@BeforeEach
	void setUp() {
		counts = new PositionCounts();
	}

	@Test
	void limitFor_shouldReturnPositionLimits() {
		assertEquals(2, PositionCounts.limitFor("ff"));
		assertEquals(6, PositionCounts.limitFor("fwd"));
		assertEquals(2, PositionCounts.limitFor("rck"));
		assertEquals(6, PositionCounts.limitFor("mid"));
		assertEquals(6, PositionCounts.limitFor("def"));
		assertEquals(2, PositionCounts.limitFor("fb"));
	}

	@Test
	void unknownPosition_shouldThrow() {
		assertThrows(UnknownPositionException.class, () -> PositionCounts.limitFor("goalie"));
		assertThrows(UnknownPositionException.class, () -> counts.increment("goalie"));
		assertThrows(UnknownPositionException.class, () -> PositionCounts.limitFor(null));
	}

	@Test
	void positions_shouldBeCaseInsensitive() {
		counts.increment("FWD");
		assertEquals(1, counts.count("fwd"));
		assertEquals(2, PositionCounts.limitFor("FF"));
	}

	@Test
	void incrementAndDecrement_shouldTrackCounts() {
		assertEquals(0, counts.count("mid"));
		counts.increment("mid");
		counts.increment("mid");
		assertEquals(2, counts.count("mid"));
		counts.decrement("mid");
		assertEquals(1, counts.count("mid"));
	}

	@Test
	void hasRoomAndIsFull_shouldReflectLimit() {
		assertTrue(counts.hasRoom("ff"));
		assertFalse(counts.isFull("ff"));

		counts.increment("ff");
		counts.increment("ff");

		assertFalse(counts.hasRoom("ff"));
		assertTrue(counts.isFull("ff"));
		assertFalse(counts.overLimit("ff"));

		counts.increment("ff");
		assertTrue(counts.overLimit("ff"));
		assertTrue(counts.anyOverLimit());
	}

	@Test
	void benchPositions_shouldListPositionsExactlyAtLimit() {
		counts.increment("ff");
		counts.increment("ff");
		for (int i = 0; i < 6; i++) {
			counts.increment("mid");
		}
		counts.increment("fwd");

		assertEquals(List.of("ff", "mid"), counts.benchPositions());
		assertEquals(2, counts.benchCount());

		// over the limit is no longer "exactly at limit"
		counts.increment("ff");
		assertEquals(List.of("mid"), counts.benchPositions());
	}

	@Test
	void anyOverLimit_shouldBeFalseWhenAllWithinLimits() {
		counts.increment("fwd");
		counts.increment("def");
		assertFalse(counts.anyOverLimit());
	}
}
