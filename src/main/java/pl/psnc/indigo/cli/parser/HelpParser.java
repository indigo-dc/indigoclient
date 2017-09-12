/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.psnc.indigo.cli.parser;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.Options;
import pl.psnc.indigo.cli.commands.AbstractCommand;
import pl.psnc.indigo.cli.commands.HelpCommand;

/**
 *
 * @author michalo
 */
public class HelpParser implements AbstractParser {

  private HelpParser() {
  }

  public AbstractCommand parse(CommandLine cmd, Options options) throws Exception {
    return new HelpCommand(options);
  }

  public static AbstractParser getInstance() {
    return new HelpParser();
  }

}
