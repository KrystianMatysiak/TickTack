package pl.sectra.ticktack.view.commandline;

import com.beust.jcommander.Parameter;

/**
 * Created with IntelliJ IDEA.
 * User: kmatysiak
 * Date: 22.03.2016
 * Time: 21:07
 */
public class CommandLineArgs {

	@Parameter(names = {"-log", "-verbose"}, description = "Level of verbosity")
	public Integer verbose = 1;

	@Parameter(names = "-groups", description = "Comma-separated list of group names to be run")
	public String groups;

	@Parameter(names = "-debug", description = "Debug mode")
	public boolean debug = false;

	@Parameter(names = {"--help", "-h"}, help = true)
	public boolean help;
}
