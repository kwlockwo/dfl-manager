package net.dflmngr.validation;

/**
 * An emergency selection: the team player number (1-45) and the emergency
 * rank (1 = first emergency, 2 = any subsequent emergency).
 *
 * Replaces the old encoding where 21.1/21.2 meant "player 21, emergency 1/2"
 * packed into a Double and decoded from its decimal digits.
 */
public record Emergency(int playerNo, int rank) {
}
