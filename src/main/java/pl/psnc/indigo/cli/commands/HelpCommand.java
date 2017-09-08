package pl.psnc.indigo.cli.commands;

import org.apache.commons.cli.*;

public class HelpCommand implements AbstractCommand {

  Options options;

  /* This command prints help for CLI client
  */
  public HelpCommand(Options options) {
    this.options 	= options;
  }

  /* execute command can return
     - == 0 - everything is OK
     - != 0 - something is definitelly not right

    In case of super nasty issue, it may throw Exception
  */  
  public int execute() throws Exception {
    HelpFormatter formatter = new HelpFormatter();
    formatter.printHelp( "java -cp ./indigoAPI.jar pl.psnc.indigo.cli.IndigoClient", options );
    return 0;
  }
}
