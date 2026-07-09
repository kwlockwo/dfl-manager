package net.dflmngr.handlers;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import net.dflmngr.exceptions.UnknownPositionException;

/**
 * Tracks how many players a team is fielding at each position and applies
 * the per-position limits (ff 2, fwd 6, rck 2, mid 6, def 6, fb 2).
 * A position at its limit means its bench slot is filled.
 */
public class PositionCounts {

	private static final Map<String, Integer> LIMITS = createLimits();

	private final Map<String, Integer> counts = new LinkedHashMap<>();

	private static Map<String, Integer> createLimits() {
		Map<String, Integer> limits = new LinkedHashMap<>();
		limits.put("ff", 2);
		limits.put("fwd", 6);
		limits.put("rck", 2);
		limits.put("mid", 6);
		limits.put("def", 6);
		limits.put("fb", 2);
		return limits;
	}

	public static int limitFor(String position) {
		return LIMITS.get(checkKnown(position));
	}

	public void increment(String position) {
		counts.merge(checkKnown(position), 1, Integer::sum);
	}

	public void decrement(String position) {
		counts.merge(checkKnown(position), -1, Integer::sum);
	}

	public int count(String position) {
		return counts.getOrDefault(checkKnown(position), 0);
	}

	public boolean hasRoom(String position) {
		return count(position) < limitFor(position);
	}

	public boolean isFull(String position) {
		return count(position) >= limitFor(position);
	}

	public boolean overLimit(String position) {
		return count(position) > limitFor(position);
	}

	public boolean anyOverLimit() {
		return LIMITS.keySet().stream().anyMatch(this::overLimit);
	}

	public List<String> benchPositions() {
		List<String> bench = new ArrayList<>();
		for(String position : LIMITS.keySet()) {
			if(count(position) == limitFor(position)) {
				bench.add(position);
			}
		}
		return bench;
	}

	public int benchCount() {
		return benchPositions().size();
	}

	private static String checkKnown(String position) {
		String normalised = position == null ? null : position.toLowerCase();
		if(normalised == null || !LIMITS.containsKey(normalised)) {
			throw new UnknownPositionException(position);
		}
		return normalised;
	}

	@Override
	public String toString() {
		return counts.toString();
	}
}
