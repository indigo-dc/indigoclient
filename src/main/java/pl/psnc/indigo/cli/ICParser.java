package pl.psnc.indigo.cli;

import java.util.LinkedList;
import org.apache.commons.cli.*;
import pl.psnc.indigo.cli.commands.*;

public class ICParser {

  String[] appArgs = null;
  String fgURLString = null;
  String tokenString = null;

  public ICParser(String[] args) {
    appArgs = args;
  }

  /* After parsing all the parameters, parse method returns all the commands
     that should/could be executed based on command line parametrs.

     Now, this is a question, how to proceed here. Either we allow just one command
     at a time, or we can call multiple commands. E.g. to call status check for multiple jobs
   */
  public LinkedList<AbstractCommand> parse() throws Exception {

    LinkedList<AbstractCommand> retVal = new LinkedList<AbstractCommand>();

    if (appArgs == null || appArgs.length == 0) {
      throw new Exception("Application arguments are empty. You have to specify at least -help.");
    }

    Options options = new Options();
    CommandLine cmdLine = null;

    options.addOption("help", false, "show help");
    options.addOption("token", true, "access token (string)");
    options.addOption("url", true, "Future Gateway API URL");
    options.addOption("apps", false, "Lists all available applications");

    CommandLineParser parser = new PosixParser();

    try {
      cmdLine = parser.parse(options, appArgs);
    } catch (Exception ex) {
      throw new Exception("There were problems while parsing command line arguments", ex);
    }

    if (cmdLine.hasOption("help")) {
      retVal.add(new HelpCommand(options));

    }

    // What we do now, is parsing all other arguments. For most things we need token. Token
    // is stored inside tokenString variable. For some arguments, proceeding without token
    // makes no sense at all
    if (cmdLine.hasOption("token")) {
      tokenString = cmdLine.getOptionValue("token");
    }

    // The same reffers to url. Apart from help, probably every other call will need url
    if (cmdLine.hasOption("url")) {
      fgURLString = cmdLine.getOptionValue("url");
    }

    if (cmdLine.hasOption("apps")) {
      if (tokenString == null || tokenString.length() == 0) {
        throw new Exception("You have to pass user's token to list applications. Use -token argument to pass user's token");
      }
      if (fgURLString == null || fgURLString.length() == 0) {
        throw new Exception("You have to pass FutureGateway API URL if you want to list applications. Use -url argument to pass FG API URL.");
      }

      retVal.add(new ListApplicationsCommand( fgURLString, tokenString ));
    }

    return retVal;

  }

}
