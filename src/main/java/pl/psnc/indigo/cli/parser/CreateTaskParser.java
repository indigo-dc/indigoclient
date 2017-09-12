/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.psnc.indigo.cli.parser;

import java.util.LinkedList;
import java.util.List;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.Options;
import pl.psnc.indigo.cli.commands.AbstractCommand;
import pl.psnc.indigo.cli.commands.CreateTask;

/**
 *
 * @author michalo
 */
public class CreateTaskParser implements AbstractParser {

  private CreateTaskParser() { }
  
  @Override
  public AbstractCommand parse(CommandLine cmd, Options options) throws Exception {
    String token = null;
    String url = null;
    String appName = null;
    String description = null;
    List<String> inputFileNames = new LinkedList<String>();
    List<String> outputFileNames = new LinkedList<String>();
    List<String> args = new LinkedList<String>();

    // First, we need to parse token, url, and application name
    if (cmd.hasOption("token")) {
      token = cmd.getOptionValue("token");
    }

    if (cmd.hasOption("url")) {
      url = cmd.getOptionValue("url");
    }
    
    if (cmd.hasOption("appName")) {
      appName = cmd.getOptionValue("appName");
    }

    // If either of three is null (empty) we need to terminate.
    // We need (for sure): token, url and app name. Otherwise it is not possible
    // to create task.
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
    if (appName == null || appName.length() == 0) {
      throw new Exception(
              "You have to pass application name in order to create new task");
    }
    
    // Now, we can parse optional elements: description, input files,
    // output files, and agruments
    
    return new CreateTask(
            appName, 
            description, 
            args, 
            inputFileNames, 
            outputFileNames, 
            url, 
            token);
    
  }
  
  public static AbstractParser getInstance() {
    return new CreateTaskParser();
  }
}
