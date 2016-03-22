package pl.sectra.ticktack;

import com.beust.jcommander.JCommander;
import org.apache.log4j.Logger;
import pl.sectra.ticktack.view.commandline.CommandLineArgs;

import java.util.Arrays;

/**
 * Created with IntelliJ IDEA.
 * User: kmatysiak
 * Date: 22.03.2016
 * Time: 20:57
 */
public class TickTack {

	private static Logger LOG = Logger.getLogger(TickTack.class);

	public static void main(String[] args) {

		LOG.debug("Input parameters: " + Arrays.toString(args));

		CommandLineArgs commandLineArgs = new CommandLineArgs();
		new JCommander(commandLineArgs, args);


	}
}
