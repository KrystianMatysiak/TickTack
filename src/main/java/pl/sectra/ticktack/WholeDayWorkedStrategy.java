package pl.sectra.ticktack;

import java.sql.Timestamp;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;

/**
 * The type Whole day worked strategy.
 */
public class WholeDayWorkedStrategy implements MissingPunchOutStrategy {
	
	private static final int DAY_WORK_HOURS = 8;
	
	@Override
	public long punchOutTime(long punchIn) {
		return Timestamp.valueOf(
			LocalDateTime.ofInstant(Instant.ofEpochMilli(punchIn), ZoneId.systemDefault())
				.plusHours(DAY_WORK_HOURS))
			.getTime();
	}
}
