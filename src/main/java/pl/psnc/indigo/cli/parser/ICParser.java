package pl.psnc.indigo.cli.parser;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import pl.psnc.indigo.cli.OptionComparator;
import pl.psnc.indigo.cli.commands.AbstractCommand;
import pl.psnc.indigo.cli.commands.Help;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

/**
 * This class is responsible for parsing command line arguments.
 */
public final class ICParser {
    private final String[] args;

    /**
     * By default, we want to parse all the arguments.
     *
     * @param args List of all arguments.
     */
    public ICParser(final String[] args) {
        super();
        this.args = Optional.ofNullable(args).orElse(new String[0]);
    }

    /**
     * After parsing all the parameters, parse method returns all the commands
     * that should/could be executed based on command line parameters.
     * <p>
     * Now, this is a question, how to proceed here. Either we allow just one
     * command at a time, or we can call multiple commands. E.g. to call status
     * check for multiple jobs
     *
     * @return Returns list of all commands that were found during parsing
     * @throws ParseException In case of parsing related issues, this
     *                        function will throw exception. Message contains
     *                        information what went wrong.
     */
    public List<AbstractCommand> parse() throws ParseException {
        if (args.length == 0) {
            throw new IllegalArgumentException(
                    "Application arguments are empty." +
                    " You have to specify at least -help.");
        }

        final Options options = OptionComparator.options();
        final CommandLineParser parser = new DefaultParser();
        final CommandLine cmdLine = parser.parse(options, args);

        // if help is present anywhere, this is the only action
        if (cmdLine.hasOption("help")) {
            return Collections.singletonList(new Help());
        }

        final List<AbstractCommand> commands = new LinkedList<>();
        if (cmdLine.hasOption("listApps")) {
            commands.add(ListApplicationsParser.parse(cmdLine));
        }
        if (cmdLine.hasOption("createTask")) {
            commands.add(CreateTaskParser.parse(cmdLine));
        }
        if (cmdLine.hasOption("getTask")) {
            commands.add(GetTaskParser.parse(cmdLine));
        }
        if (cmdLine.hasOption("uploadFile")) {
            commands.add(UploadFileParser.parse(cmdLine));
        }

        return commands;
    }
}
