package pl.psnc.indigo.cli;

import org.apache.commons.cli.*;

public class ICParser {

  String [] appArgs     = null;
  String fgURLString    = null;
  String tokenString    = null;

  public ICParser(String [] args) {
    appArgs = args;
  }

  public void parse() throws Exception {
    if(appArgs == null || appArgs.length == 0) {
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
      HelpFormatter formatter = new HelpFormatter();
      formatter.printHelp( "java -cp ./indigoAPI.jar pl.psnc.indigo.cli.IndigoClient", options );
      // I guess, we should quit app once "-help/--help" was passed
      System.exit(0); 
    }

    // What we do now, is parsing all other arguments. For most things we need token. Token
    // is stored inside tokenString variable. For some arguments, proceeding without token
    // makes no sense at all
    if (cmdLine.hasOption("token")) {
      tokenString = cmdLine.getOptionValue("token");
    }

    // The same reffers to url. Apart from help, probably every other call will need url
    if (cmdLine.hasOption("url")) {
      fgURLString = cmdLine.getOptionValue("token");
    }

  }

}
