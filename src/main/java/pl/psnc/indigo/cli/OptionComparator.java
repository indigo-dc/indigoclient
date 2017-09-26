/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.psnc.indigo.cli;

import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import pl.psnc.indigo.cli.parser.UploadFileParser;

import java.io.Serializable;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;

/**
 * Comparator class for Options. This is singleton as we want to fill it
 * only once - when options are created.
 * This class is used by HelpFormatter
 *
 * @author michalo
 */
public final class OptionComparator
        implements Comparator<Option>, Serializable {
    private static final long serialVersionUID = 3465955962064963921L;

    private static final Options OPTIONS = new Options();

    static {
        OptionComparator.OPTIONS.addOption("help", false, "show help");
        OptionComparator.OPTIONS.addOption("listApps", false,
                                           "Lists all available applications");
        OptionComparator.OPTIONS
                .addOption("createTask", false, "Creates tasks");
        OptionComparator.OPTIONS
                .addOption("getTask", true, "Get task info (requires id)");
        OptionComparator.OPTIONS.addOption(
                Option.builder("uploadFile").desc("Upload file for task")
                      .hasArgs().numberOfArgs(UploadFileParser.NUMBER_OF_ARGS)
                      .argName("ID FILE_NAME LOCAL_FILE_LOCATION").build());
        OptionComparator.OPTIONS.addOption("verbose", false, "Be verbose");
        OptionComparator.OPTIONS.addOption(
                Option.builder().longOpt("token").desc("access token (string)")
                      .hasArgs().argName("TOKEN").build());
        OptionComparator.OPTIONS.addOption(
                Option.builder().longOpt("url").desc("Future Gateway API URL")
                      .hasArg().argName("URL").build());
        OptionComparator.OPTIONS.addOption(
                Option.builder().longOpt("inputs").desc("Names of files")
                      .hasArgs().argName("FILE_NAMES").valueSeparator(',')
                      .build());
        OptionComparator.OPTIONS.addOption(
                Option.builder().longOpt("outputs").desc("Names of files")
                      .hasArgs().argName("PATHS").valueSeparator(',').build());
        OptionComparator.OPTIONS.addOption(
                Option.builder().longOpt("description")
                      .desc("Description of the task").numberOfArgs(1)
                      .argName("DESCRIPTION").build());
        OptionComparator.OPTIONS.addOption(Option.builder().longOpt("args")
                                                 .desc("Arguments of the task" +
                                                       " (embedded in " +
                                                       "'args')")
                                                 .numberOfArgs(1)
                                                 .argName("ARGS").build());
        OptionComparator.OPTIONS.addOption(Option.builder().longOpt("appName")
                                                 .desc("Name of the " +
                                                       "application to be " +
                                                       "created")
                                                 .numberOfArgs(1)
                                                 .argName("NAME").build());
    }

    private static final OptionComparator INSTANCE = new OptionComparator();

    private final Map<String, String> optionIndexMap = new HashMap<>();

    /**
     * We want just one instance of  In fact, there is no sense
     * to have more of them.
     */
    private OptionComparator() {
        super();

        addOption("help", "001");
        addOption("listApps", "002");
        addOption("createTask", "003");
        addOption("getTask", "004");
        addOption("uploadFile", "005");
        addOption("verbose", "006");
        addOption("token", "011");
        addOption("url", "012");
        addOption("inputs", "013");
        addOption("outputs", "014");
        addOption("description", "015");
        addOption("args", "016");
        addOption("appName", "017");
    }

    /**
     * Get {@link Options} object with all possible command line options.
     *
     * @return Object containing information about all possible command line
     * options.
     */
    public static Options options() {
        return OptionComparator.OPTIONS;
    }

    /**
     * We are comparing values stored under keys that reflect names of options.
     * options(o1).compareTo(o2)
     *
     * @param t  First option to compare
     * @param t1 Second option to compare
     * @return Returns result of comparison.
     */
    @Override
    public int compare(final Option t, final Option t1) {
        final String o1;
        if (optionIndexMap.get(t.getOpt()) == null) {
            o1 = optionIndexMap.get(t.getLongOpt());
        } else {
            o1 = optionIndexMap.get(t.getOpt());
        }

        final String o2;
        if (optionIndexMap.get(t1.getOpt()) == null) {
            o2 = optionIndexMap.get(t1.getLongOpt());
        } else {
            o2 = optionIndexMap.get(t1.getOpt());
        }
        return o1.compareTo(o2);
    }

    public static OptionComparator getInstance() {
        return OptionComparator.INSTANCE;
    }

    /**
     * We can add options with idxes that are use to compare options.
     * Idxes are compared as regular strings.
     *
     * @param option Name of the option to be added
     * @param idx    sort index we are looking for
     */
    private void addOption(final String option, final String idx) {
        optionIndexMap.put(option, idx);
    }

}
