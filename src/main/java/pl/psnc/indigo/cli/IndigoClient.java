package pl.psnc.indigo.cli;

import pl.psnc.indigo.cli.commands.*;

public class IndigoClient {

  public static void main(String [] args) {

    ICParser parser = new ICParser(args);
    try {
      LinkedList<AbstractICCommand> commands = parser.parse();
      for(AbstractICCommand cmd : commands) {
        try {
          cmd.execute();
        } catch( Exception ex ) {
        }
      } 
    } catch (Exception ex) {
      System.out.println("\n" + ex.getMessage());
      System.out.flush();
      System.exit(1);
    }

  }

}
