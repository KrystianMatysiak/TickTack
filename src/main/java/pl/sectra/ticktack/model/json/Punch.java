package pl.sectra.ticktack.model.json;

import pl.sectra.ticktack.model.Direction;

/**
 * Created with IntelliJ IDEA.
 * User: kmatysiak
 * Date: 22.03.2016
 * Time: 22:15
 */
public class Punch {

	public Long id;

	public Direction direction;

	public Long cardNumber;

//	public TimeZone timeZone;

	public Long timeOfRegistration;

	@Override
	public String toString() {
		return "Punch{" +
				"id=" + id +
				", direction=" + direction +
				", cardNumber=" + cardNumber +
				", timeOfRegistration=" + timeOfRegistration +
				'}';
	}
}
