/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.psnc.indigo.cli.parser;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.Options;
import pl.psnc.indigo.cli.commands.AbstractCommand;
import pl.psnc.indigo.cli.commands.GetTaskCommand;

/**
 *
 * @author michalo
 */
public final class GetTaskParser implements AbstractParser {

  /**
   * We want to make sure there are no GetTaskParser objects that were
   * created without parameters.
   */
  private GetTaskParser() { }

  /**
   * Parses CommandLine arguments and creates
   *        Command for getting status of task.
   * @param cmd Command Line arguments - parsed
   * @param options Available options
   * @return returns Command that will call FG API and get task status
   * @throws Exception In case of major issue, we are throwing Exception.
   */
  @Override
  public AbstractCommand parse(
          final CommandLine cmd,
          final Options options)
          throws Exception {
    String token = null;
    String url = null;

    if (cmd.hasOption("token")) {
      token = cmd.getOptionValue("token");
    }

    if (cmd.hasOption("url")) {
      url = cmd.getOptionValue("url");
    }

    if (token == null || token.length() == 0) {
      throw new Exception(
              "You have to pass user's token to list applications."
              + " Use -token argument to pass user's token");
    }
    if (url == null || url.length() == 0) {
      throw new Exception(
              "You have to pass FutureGateway API URL if "
              + "you want to list applications."
              + " Use -url argument to pass FG API URL.");
    }

    return new GetTaskCommand(cmd.getOptionValue("getTask"), url, token);
  }

  public static AbstractParser getInstance() {
    return new GetTaskParser();
  }

}
