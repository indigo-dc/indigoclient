/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.psnc.indigo.cli.parser;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.Options;
import pl.psnc.indigo.cli.commands.AbstractCommand;
import pl.psnc.indigo.cli.commands.UploadFileCommand;

/**
 *
 * @author michalo
 */
public final class UploadFileParser implements AbstractParser {

  /**
   * We need this value in two places. Here, and inside ICParser.
   *
   * Why three? Well, I guess Monty Python has something to say here
   * "First shalt thou take out the Holy Pin. Then shalt thou count to three,
   * no more, no less. Three shall be the number thou shalt count, and the
   * number of the counting shall be three. Four shalt thou not count,
   * neither count thou two, excepting that thou then proceed to three.
   * Five is right out."
   */
  public static final int NUMBER_OF_ARGS = 3;

  /**
   * We want to prevent from creating objects without arguments.
   */
  private UploadFileParser() {
  }

  /**
   * Creates Command for uploading files.
   * @param cmd Command line arguments - parsed.
   * @param options All the options available for parser
   * @return Command that uploads file to FG API server
   * @throws Exception In case of major issue we throw Exception
   */
  public AbstractCommand parse(
          final CommandLine cmd,
          final Options options)
          throws Exception {
    String token = null;
    String url = null;

    String id = null;
    String fileName = null;
    String filePath = null;

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

    String[] uploadArgs = cmd.getArgs();
    if (uploadArgs == null || uploadArgs.length != NUMBER_OF_ARGS) {
      throw new Exception(
              "You have to specify job's ID, file name, and " +
              "local file to upload it.");
    } else {
      id = uploadArgs[0];
      fileName = uploadArgs[1];
      filePath = uploadArgs[2];
    }

    return new UploadFileCommand(url, token, id, fileName, filePath);
  }

  public static AbstractParser getInstance() {
    return new UploadFileParser();
  }
}
