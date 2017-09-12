package pl.psnc.indigo.cli;

import java.util.LinkedList;
import pl.psnc.indigo.cli.parser.ICParser;
import pl.psnc.indigo.cli.commands.AbstractCommand;

/**
 * IndigoClient main class.
 *
 */

public final class IndigoClient {

  /**
   * IndigoClient main entry point.
   *
   * @param args Arguments of application
   */
  public static void main(final String[] args) {

    ICParser parser = new ICParser(args);
    try {
      LinkedList<AbstractCommand> commands = parser.parse();
      for (AbstractCommand cmd : commands) {
        try {
          cmd.execute();
        } catch (Exception ex) {
          ex.printStackTrace();
        }
      }
    } catch (Exception ex) {
      System.out.println("\n" + ex.getMessage());
      System.out.flush();
      System.exit(1);
    }
  }

  /**
   * Just to make sure it will not be created somewhere.
   */
  private IndigoClient() {

  }
}
