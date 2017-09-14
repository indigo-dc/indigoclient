/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.psnc.indigo.cli.parser;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.Options;
import pl.psnc.indigo.cli.commands.AbstractCommand;
import pl.psnc.indigo.cli.commands.ListApplicationsCommand;

/**
 * Parses arguments and tries to create Command for listing all applications.
 * @author michalo
 */
public final class ListApplicationsParser implements AbstractParser {

  /**
   * We want to prevent from creating objects.
   */
  private ListApplicationsParser() {
  }

  /**
   * Parses command line arguments and creates Command for listing applications.
   * @param cmd Command line - parsed
   * @param options All available options
   * @return Returns Command that calls FG API and returns available apps
   * @throws Exception In case of serious issues throws exception
   */
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

    return new ListApplicationsCommand(url, token);
  }

  /**
   * Provides instance of the class.
   * @return Instance of the AbstractParser
   */
  public static AbstractParser getInstance() {
    return new ListApplicationsParser();
  }
}
