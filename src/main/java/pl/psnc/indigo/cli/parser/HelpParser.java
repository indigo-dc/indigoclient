/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.psnc.indigo.cli.parser;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.Options;
import pl.psnc.indigo.cli.commands.AbstractCommand;
import pl.psnc.indigo.cli.commands.Help;

/**
 * @author michalo
 */
public final class HelpParser implements AbstractParser {
    /**
     * We want to prevent from creating default objects.
     */
    private HelpParser() {
        super();
    }

    /**
     * Creates command that provides help message.
     *
     * @param cmd     Command line arguments
     * @param options All available options
     * @return return command object that shows help
     */
    public AbstractCommand parse(final CommandLine cmd, final Options options) {
        return new Help(options);
    }

    /**
     * Gets the instance of the class.
     *
     * @return Returns new object of the class
     */
    public static AbstractParser getInstance() {
        return new HelpParser();
    }

}
