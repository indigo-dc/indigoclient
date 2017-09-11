package pl.psnc.indigo.cli.commands;

/**
 * This interface represents <i>Abstract Command</i> that can
 * can be implemented by specific targets.
 *
 * @author Michal K. Owsiak
 */

public interface AbstractCommand {

  /**
   *
   * Executes command can return two different values.
   *
   * @return 0    - everything is OK
   *         != 0 - something is definitelly not right
   *
   * @throws Exception In case of super nasty issue, it may throw Exception
   *
   */

  int execute() throws Exception;
}
