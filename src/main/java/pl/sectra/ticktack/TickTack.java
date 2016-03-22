package pl.sectra.ticktack;

import com.beust.jcommander.JCommander;
import com.google.common.base.Charsets;
import com.google.common.io.CharStreams;
import com.google.gson.Gson;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.log4j.Logger;
import pl.sectra.ticktack.model.json.Punch;
import pl.sectra.ticktack.view.commandline.CommandLineArgs;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.Locale;
import java.util.Properties;
import java.util.concurrent.TimeUnit;

/**
 * Created with IntelliJ IDEA.
 * User: kmatysiak
 * Date: 22.03.2016
 * Time: 20:57
 */
public class TickTack {

	private static final Logger LOG = Logger.getLogger(TickTack.class);

	private static final Properties TICK_TACK_PROPERTIES = new Properties();

	private static final int DAY_WORK_HOURS = 8;

	static {
		try {
			TICK_TACK_PROPERTIES.load(TickTack.class.getClassLoader().getResourceAsStream("ticktack.properties"));
		} catch (IOException e) {
			LOG.error(e);
		}
	}

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
			do {
				LocalDate dayToCheck = today.withDayOfMonth(dayOfMonth);
				String punchDateParam = dayToCheck.format(DateTimeFormatter.ofPattern("yyyyMMdd"));

				HttpClient httpClient = new DefaultHttpClient();
				HttpGet request = new HttpGet(
						TICK_TACK_PROPERTIES.getProperty("service.url")
								+ "/"
								+ TICK_TACK_PROPERTIES.getProperty("user.id")
								+ "/"
								+ punchDateParam
				);
				HttpResponse response = httpClient.execute(request);
				InputStream inputStream = response.getEntity().getContent();
				String json = CharStreams.toString(new InputStreamReader(inputStream, Charsets.UTF_8));

				Punch[] punches = new Gson().fromJson(json, Punch[].class);

				LOG.debug(Arrays.toString(punches));

				Long timeOfIn = punches[0].timeOfRegistration;
				Long timeOfOut = punches[1].timeOfRegistration;

				DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
						.withLocale(Locale.UK)
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

			} while (lastDayOfMonth.isBefore(lastDayOfMonth));

			long monthlyHoursAtWork = TimeUnit.MILLISECONDS.toHours(monthlyWorkingTime);
			long monthlyMinutesAtWork = (TimeUnit.MILLISECONDS.toMinutes(monthlyWorkingTime) -
					TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(monthlyWorkingTime)));

			LOG.info("Monthly in work for:\t" + monthlyHoursAtWork + "h" + monthlyMinutesAtWork + "min\n");

		} catch (IOException e) {
			LOG.error(e);
		}
	}
}
