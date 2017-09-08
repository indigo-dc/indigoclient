package pl.psnc.indigo.cli;

public class IndigoClient {

  public static void main(String [] args) {

    ICParser parser = new ICParser(args);
    try {
      parser.parse();
    } catch (Exception ex) {
      System.out.println("\n" + ex.getMessage());
      System.out.flush();
      System.exit(1);
    }

  }

}
