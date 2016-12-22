package pl.sectra.ticktack;

import com.beust.jcommander.JCommander;
import org.apache.log4j.Logger;
import pl.sectra.ticktack.model.json.Punch;
import pl.sectra.ticktack.view.commandline.CommandLineArgs;

import java.io.IOException;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.Properties;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * Created with IntelliJ IDEA.
 * User: kmatysiak
 * Date: 22.03.2016
 * Time: 20:57
 */
public class TickTackLauncher {

	private static final Logger LOG = Logger.getLogger(TickTackLauncher.class);

	private static final String TICK_TACK_SERVICE_URL_PARAM_NAME = "service.url";
	private static final String TICK_TACK_USER_ID_PARAM_NAME = "user.id";

	private static final Properties TICK_TACK_PROPERTIES = new Properties();
	static {
		try {
			TICK_TACK_PROPERTIES.load(TickTackLauncher.class.getClassLoader().getResourceAsStream("ticktack.properties"));
		} catch (IOException e) {
			LOG.error(e);
		}
	}

	private static final String TICK_TACK_URL = TICK_TACK_PROPERTIES.getProperty(TICK_TACK_SERVICE_URL_PARAM_NAME);

	private static final int DAY_WORK_HOURS = 8;

	public static void main(String[] args) {

		LOG.debug("Input parameters: " + Arrays.toString(args));

		CommandLineArgs commandLineArgs = new CommandLineArgs();
		JCommander jCommander = new JCommander(commandLineArgs, args);
		jCommander.setProgramName("TickTack");

//		if (commandLineArgs.help) {
//			jCommander.usage();
//		}

		try {
//			String json = Resources.toString(Resources.getResourceAsStreamsource("20151008.json"), Charsets.UTF_8);

			ZoneId zoneId = ZoneId.systemDefault();
			LocalDate today = LocalDate.now(zoneId);

			LocalDate lastDayOfMonth = today.withDayOfMonth(today.lengthOfMonth());

			long monthlyWorkingTime = 0;

			int dayOfMonth = 1;

			LocalDate dayToCheck;
			do {
				dayToCheck = today.withDayOfMonth(dayOfMonth);

				String userId = TICK_TACK_PROPERTIES.getProperty(TICK_TACK_USER_ID_PARAM_NAME);

				List<Punch> punches = new TickTackRequester(TICK_TACK_URL)
						.doRequest(userId, dayToCheck);

				LOG.debug(punches.stream().map(Object::toString).collect(Collectors.joining(", ")));

				Long timeOfIn = punches.get(0).timeOfRegistration;
				Long timeOfOut = punches.get(1).timeOfRegistration;

				DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
						.withLocale(Locale.GERMAN)
						.withZone(ZoneId.systemDefault());

				String timeOfInString = dateTimeFormatter.format(Instant.ofEpochMilli(timeOfIn));
				String timeOfOutString = dateTimeFormatter.format(Instant.ofEpochMilli(timeOfOut));

				long workingTime = timeOfOut - timeOfIn;
				long hoursAtWork = TimeUnit.MILLISECONDS.toHours(workingTime);
				long minutesAtWork = (TimeUnit.MILLISECONDS.toMinutes(workingTime) -
						TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(workingTime)));

				LOG.info("In:\t" + timeOfInString);
				LOG.info("Out:\t" + timeOfOutString);
				LOG.info("In work for:\t" + hoursAtWork + "h" + minutesAtWork + "min\n");

				monthlyWorkingTime += workingTime;

				dayOfMonth++;
				
			} while (dayToCheck.isBefore(lastDayOfMonth));

			long monthlyHoursAtWork = TimeUnit.MILLISECONDS.toHours(monthlyWorkingTime);
			long monthlyMinutesAtWork = (TimeUnit.MILLISECONDS.toMinutes(monthlyWorkingTime) -
					TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(monthlyWorkingTime)));

			LOG.info("Monthly in work for:\t" + monthlyHoursAtWork + "h" + monthlyMinutesAtWork + "min\n");

		} catch (IOException e) {
			LOG.error(e);
		}
	}
}
