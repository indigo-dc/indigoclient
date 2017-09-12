package pl.psnc.indigo.cli.commands;

import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Options;
import pl.psnc.indigo.cli.parser.OptionComparator;

/**
 * Class responsible for printing Help of the application.
 */
public class HelpCommand implements AbstractCommand {

  private Options options;

  /**
   * This command prints help for CLI client.
   *
   * @param options Options created while parsing application's arguments.
   */
  public HelpCommand(final Options options) {
    this.options = options;
  }

  /**
   * Execute help command.
   *
   * @return 0    - everything is OK
   *         != 0 - something is definitelly not right
   *
   * @throws Exception In case of super nasty issue, it may throw Exception
   */
  public final int execute() throws Exception {
    HelpFormatter formatter = new HelpFormatter();
    formatter.setOptionComparator(OptionComparator.getInstance());
    formatter.printHelp(
      "java -cp ./indigoAPI.jar pl.psnc.indigo.cli.IndigoClient", options);
    return 0;
  }
}
