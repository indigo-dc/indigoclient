/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.psnc.indigo.cli.parser;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.Options;
import pl.psnc.indigo.cli.commands.AbstractCommand;
import pl.psnc.indigo.cli.commands.GetTask;

/**
 * @author michalo
 */
public final class GetTaskParser implements AbstractParser {
    /**
     * We want to make sure there are no GetTaskParser objects that were
     * created without parameters.
     */
    private GetTaskParser() {
        super();
    }

    /**
     * Parses CommandLine arguments and creates
     * Command for getting status of task.
     *
     * @param cmd     Command Line arguments - parsed
     * @param options Available options
     * @return returns Command that will call FG API and get task status
     */
    @Override
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

        return new GetTask(cmd.getOptionValue("getTask"), url, token);
    }

    public static AbstractParser getInstance() {
        return new GetTaskParser();
    }

}
