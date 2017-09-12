package pl.psnc.indigo.cli.parser;

import java.util.LinkedList;
import java.util.List;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.OptionBuilder;
import org.apache.commons.cli.PosixParser;
import pl.psnc.indigo.cli.commands.AbstractCommand;
import pl.psnc.indigo.cli.commands.ListApplicationsCommand;
import pl.psnc.indigo.cli.commands.HelpCommand;

/**
 * This class is responsible for parsing command line arguments.
 *
 */
public final class ICParser {

  private String[] appArgs = null;
  private String fgURLString = null;
  private String tokenString = null;
  private String description = null;
  private List<String> arguments = null;
  private List<String> inputFiles = null;
  private List<String> outputFiles = null;
  private String appName = null;

  /**
   * Prevents from mistaken creation of class.
   */
  private ICParser() {
  }

  /**
   * By default, we want to parse all the arguments.
   *
   * @param args List of all arguments.
   */
  public ICParser(final String[] args) {
    appArgs = args;
  }

  /**
   * After parsing all the parameters, parse method returns all the commands
   * that should/could be executed based on command line parametrs.
   *
   * Now, this is a question, how to proceed here. Either we allow just one
   * command at a time, or we can call multiple commands. E.g. to call status
   * check for multiple jobs
   *
   * @return Returns list of all commands that were found during parsing
   * @throws Exception In case of parsing related issues, this function will
   * throw exception. Message contains information what went wrong.
   */
  public LinkedList<AbstractCommand> parse() throws Exception {

    LinkedList<AbstractCommand> retVal = new LinkedList<AbstractCommand>();

    if (appArgs == null || appArgs.length == 0) {
      throw new Exception(
              "Application arguments are empty. You have to specify at least -help.");
    }

    Options options = new Options();
    CommandLine cmdLine = null;

    options.addOption("help", false, "show help");
    options.addOption("token", true, "access token (string)");
    options.addOption("url", true, "Future Gateway API URL");
    options.addOption("listApps", false, "Lists all available applications");
    options.addOption(OptionBuilder.withLongOpt("inputs")
            .withDescription("Path to inputs")
            .hasArgs()
            .withArgName("PATHS")
            .create());
    options.addOption(OptionBuilder.withLongOpt("outputs")
            .withDescription("Path to outputs")
            .hasArgs()
            .withArgName("PATHS")
            .create());
    options.addOption("description", false, "Description of the task");
    options.addOption("args", false, "Arguments of the task");
    options.addOption("appName",
            false, "Name of the application to be created");
    options.addOption("createTask", false, "Creates tasks");
    options.addOption("verbose", false, "Be verbose");

    CommandLineParser parser = new PosixParser();

    try {
      cmdLine = parser.parse(options, appArgs);
    } catch (Exception ex) {
      throw new Exception(
              "There were problems while parsing command line arguments", ex);
    }

    if (cmdLine.hasOption("help")) {
      retVal.add(HelpParser.getInstance().parse(cmdLine, options));
    } else if (cmdLine.hasOption("listApps")) {
      retVal.add(ListApplicationsParser.getInstance().parse(cmdLine, options));
    } else if (cmdLine.hasOption("createTask")) {

    }

    return retVal;

  }
}
