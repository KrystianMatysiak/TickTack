package pl.sectra.ticktack.view.commandline;

import com.beust.jcommander.Parameter;

/**
 * Created with IntelliJ IDEA.
 * User: kmatysiak
 * Date: 22.03.2016
 * Time: 21:07
 */
public class CommandLineArgs {

	@Parameter(names = {"--help", "-h"}, help = true)
	public boolean help;
}
