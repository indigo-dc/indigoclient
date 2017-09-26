/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.psnc.indigo.cli.parser;

import org.apache.commons.cli.CommandLine;
import pl.psnc.indigo.cli.commands.AbstractCommand;
import pl.psnc.indigo.cli.commands.CreateTask;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * @author michalo
 */
public final class CreateTaskParser {
    /**
     * Creates command for creating task. It is called in case we spot
     * argument that is responsible for creating task via FG API.
     *
     * @param cmd Command line arguments (parsed)
     * @return Command that creates task
     */
    public static AbstractCommand parse(final CommandLine cmd) {
        // First, we need to parse token, url, and application name
        final String token = cmd.getOptionValue("token", "");
        final String url = cmd.getOptionValue("url", "");
        final String appName = cmd.getOptionValue("appName", "");
        final String description = cmd.getOptionValue("description", "");

        // If either of three is null (empty) we need to terminate.
        // We need (for sure): token, url and app name. Otherwise it is not
        // possible to create task.
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
        if (appName.isEmpty()) {
            throw new IllegalArgumentException(
                    "You have to pass application name in order to create new" +
                    " task");
        }

        final List<String> args;
        if (cmd.hasOption("args")) {
            args = Arrays.asList(cmd.getOptionValues("args"));
        } else {
            args = Collections.emptyList();
        }

        final List<String> inputFileNames;
        if (cmd.hasOption("inputs")) {
            inputFileNames = Arrays.asList(cmd.getOptionValues("inputs"));
        } else {
            inputFileNames = Collections.emptyList();
        }

        final List<String> outputFileNames;
        if (cmd.hasOption("outputs")) {
            outputFileNames = Arrays.asList(cmd.getOptionValues("outputs"));
        } else {
            outputFileNames = Collections.emptyList();
        }

        return new CreateTask(appName, description, args, inputFileNames,
                              outputFileNames, url, token);
    }

    /**
     * We want to make sure that class is not instantiated without params.
     */
    private CreateTaskParser() {
        super();
    }
}
