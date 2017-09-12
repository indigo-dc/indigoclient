/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.psnc.indigo.cli.parser;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.Options;
import pl.psnc.indigo.cli.commands.AbstractCommand;

/**
 * Abstract parser. All parsers can parse command line and if there is enough
 * information they create Command object that is passed back to main
 * application.
 *
 * @author michalo
 */
public interface AbstractParser {

  /**
   * Parses command line arguments and creates command.
   *
   * @param parser
   * @param options
   * @return
   * @throws Exception
   */
  AbstractCommand parse(
          CommandLine cmd,
          Options options) throws Exception;
}
