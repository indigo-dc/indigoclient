/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.psnc.indigo.cli.parser;

import org.apache.commons.cli.CommandLine;
import pl.psnc.indigo.cli.commands.AbstractCommand;
import pl.psnc.indigo.cli.commands.UploadFile;

/**
 * @author michalo
 */
public final class UploadFileParser {
    /**
     * First argument is the task id, second argument is the path to file.
     */
    public static final int NUMBER_OF_ARGS = 2;

    /**
     * Creates Command for uploading files.
     *
     * @param cmd Command line arguments - parsed.
     * @return Command that uploads file to FG API server
     */
    public static AbstractCommand parse(final CommandLine cmd) {
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

        final String[] uploadArgs = cmd.getOptionValues("uploadFile");
        final String id = uploadArgs[0];
        final String filePath = uploadArgs[1];
        return new UploadFile(url, token, id, filePath);
    }

    /**
     * We want to prevent from creating objects without arguments.
     */
    private UploadFileParser() {
        super();
    }
}
