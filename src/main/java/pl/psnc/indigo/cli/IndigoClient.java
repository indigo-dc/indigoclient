package pl.psnc.indigo.cli;

import org.apache.commons.cli.*;

public class IndigoClient {


  public static void main(String [] args) {
    Options options = new Options();
    CommandLine cmdline = null;

    options.addOption("help", "help", false, "show help");

    CommandLineParser parser = new PosixParser();

    try {
      cmdline = parser.parse(options, args);
    } catch (Exception ex) {
      System.out.println("\n" + ex.getMessage());
      System.exit(1);
    }

    if (cmdline.hasOption("help")) {
      HelpFormatter formatter = new HelpFormatter();
      formatter.printHelp( "java -cp ./indigoAPI.jar pl.psnc.indigo.cli.IndigoClient", options );
    }

  }

}
