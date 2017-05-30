package pl.sectra.ticktack;

/**
 * The interface Missing punch out strategy.
 */
public interface MissingPunchOutStrategy {
	/**
	 * Punch out time long.
	 *
	 * @param punchIn the punch in
	 * @return the long
	 */
	long punchOutTime(long punchIn);
}
