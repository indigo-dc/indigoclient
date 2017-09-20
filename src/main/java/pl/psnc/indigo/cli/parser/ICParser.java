package pl.psnc.indigo.cli.parser;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import pl.psnc.indigo.cli.commands.AbstractCommand;

import java.util.LinkedList;
import java.util.List;

/**
 * This class is responsible for parsing command line arguments.
 */
public final class ICParser {
    private String[] appArgs;
    private String fgURLString;
    private String tokenString;
    private String description;
    private List<String> arguments;
    private List<String> inputFiles;
    private List<String> outputFiles;
    private String appName;

    /**
     * Prevents from mistaken creation of class.
     */
    private ICParser() {
        super();
    }

    /**
     * By default, we want to parse all the arguments.
     *
     * @param args List of all arguments.
     */
    public ICParser(final String[] args) {
        super();
        appArgs = args.clone();
    }

    /**
     * After parsing all the parameters, parse method returns all the commands
     * that should/could be executed based on command line parametrs.
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
        if ((appArgs == null) || (appArgs.length == 0)) {
            throw new IllegalArgumentException(
                    "Application arguments are empty." +
                    " You have to specify at least -help.");
        }

        final Options options = new Options();
        final OptionComparator optionComparator =
                OptionComparator.getInstance();
        options.addOption("help", false, "show help");
        optionComparator.addOption("help", "001");

        options.addOption("listApps", false,
                          "Lists all available applications");
        optionComparator.addOption("listApps", "002");

        options.addOption("createTask", false, "Creates tasks");
        optionComparator.addOption("createTask", "003");

        options.addOption("getTask", true, "Get task info (requires id)");
        optionComparator.addOption("getTask", "004");

        options.addOption(
                Option.builder("uploadFile").desc("Upload file for task")
                      .hasArgs().numberOfArgs(UploadFileParser.NUMBER_OF_ARGS)
                      .argName("ID FILE_NAME LOCAL_FILE_LOCATION").build());
        optionComparator.addOption("uploadFile", "005");

        options.addOption("verbose", false, "Be verbose");
        optionComparator.addOption("verbose", "006");

        options.addOption(
                Option.builder().longOpt("token").desc("access token (string)")
                      .hasArgs().argName("TOKEN").build());
        optionComparator.addOption("token", "011");

        options.addOption(
                Option.builder().longOpt("url").desc("Future Gateway API URL")
                      .hasArg().argName("URL").build());
        optionComparator.addOption("url", "012");

        options.addOption(
                Option.builder().longOpt("inputs").desc("Names of files")
                      .hasArgs().argName("FILE_NAMES").valueSeparator(',')
                      .build());
        optionComparator.addOption("inputs", "013");

        options.addOption(
                Option.builder().longOpt("outputs").desc("Names of files")
                      .hasArgs().argName("PATHS").valueSeparator(',').build());
        optionComparator.addOption("outputs", "014");

        options.addOption(Option.builder().longOpt("description")
                                .desc("Description of the task").numberOfArgs(1)
                                .argName("DESCRIPTION").build());
        optionComparator.addOption("description", "015");

        options.addOption(Option.builder().longOpt("args")
                                .desc("Arguments of the task (embedded in " +
                                      "'args')").numberOfArgs(1).argName("ARGS")
                                .build());
        optionComparator.addOption("args", "016");

        options.addOption(Option.builder().longOpt("appName")
                                .desc("Name of the application to be created")
                                .numberOfArgs(1).argName("NAME").build());
        optionComparator.addOption("appName", "017");

        final CommandLineParser parser = new DefaultParser();
        final CommandLine cmdLine = parser.parse(options, appArgs);
        final List<AbstractCommand> retVal = new LinkedList<>();

        if (cmdLine.hasOption("help")) {
            retVal.add(HelpParser.getInstance().parse(cmdLine, options));
        } else if (cmdLine.hasOption("listApps")) {
            retVal.add(ListApplicationsParser.getInstance()
                                             .parse(cmdLine, options));
        } else if (cmdLine.hasOption("createTask")) {
            retVal.add(CreateTaskParser.getInstance().parse(cmdLine, options));
        } else if (cmdLine.hasOption("getTask")) {
            retVal.add(GetTaskParser.getInstance().parse(cmdLine, options));
        }

        return retVal;
    }
}
