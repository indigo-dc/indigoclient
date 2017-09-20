/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.psnc.indigo.cli.parser;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.Options;
import pl.psnc.indigo.cli.commands.AbstractCommand;
import pl.psnc.indigo.cli.commands.ListApplications;

/**
 * Parses arguments and tries to create Command for listing all applications.
 *
 * @author michalo
 */
public final class ListApplicationsParser implements AbstractParser {
    /**
     * We want to prevent from creating objects.
     */
    private ListApplicationsParser() {
        super();
    }

    /**
     * Parses command line arguments and creates Command for listing
     * applications.
     *
     * @param cmd     Command line - parsed
     * @param options All available options
     * @return Returns Command that calls FG API and returns available apps
     */
    public AbstractCommand parse(final CommandLine cmd, final Options options) {
        final String token = cmd.getOptionValue("token", "");
        final String url = cmd.getOptionValue("url", "");

        if (token.isEmpty()) {
            throw new IllegalArgumentException(
                    "You have to pass user's token to list applications." +
                    " Use -token argument to pass user's token");
        }
        if (url.isEmpty()) {
            throw new IllegalArgumentException(
                    "You have to pass FutureGateway API URL if " +
                    "you want to list applications." +
                    " Use -url argument to pass FG API URL.");
        }

        return new ListApplications(url, token);
    }

    /**
     * Provides instance of the class.
     *
     * @return Instance of the AbstractParser
     */
    public static AbstractParser getInstance() {
        return new ListApplicationsParser();
    }
}
