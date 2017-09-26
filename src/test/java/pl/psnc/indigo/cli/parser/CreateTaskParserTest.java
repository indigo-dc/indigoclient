package pl.psnc.indigo.cli.parser;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import pl.psnc.indigo.cli.OptionComparator;
import pl.psnc.indigo.fg.api.restful.category.UnitTests;

@Category(UnitTests.class)
public class CreateTaskParserTest {
    @Test(expected = IllegalArgumentException.class)
    public final void parseNoToken() throws Exception {
        final CommandLineParser parser = new DefaultParser();
        final String[] arguments = {"-createTask"};
        final CommandLine commandLine =
                parser.parse(OptionComparator.options(), arguments);
        CreateTaskParser.parse(commandLine);
    }

    @Test(expected = IllegalArgumentException.class)
    public final void parseNoUrl() throws Exception {
        final CommandLineParser parser = new DefaultParser();
        final String[] arguments = {"-createTask", "-token", "_"};
        final CommandLine commandLine =
                parser.parse(OptionComparator.options(), arguments);
        CreateTaskParser.parse(commandLine);
    }

    @Test(expected = IllegalArgumentException.class)
    public final void parseNoAppName() throws Exception {
        final CommandLineParser parser = new DefaultParser();
        final String[] arguments = {"-createTask", "-token", "_", "-url", "_"};
        final CommandLine commandLine =
                parser.parse(OptionComparator.options(), arguments);
        CreateTaskParser.parse(commandLine);
    }

    @Test
    public final void parse() throws ParseException {
        final CommandLineParser parser = new DefaultParser();
        final Options options = OptionComparator.options();
        final String[] simplest =
                {"-createTask", "-token", "_", "-url", "_", "-appName", "_"};
        CreateTaskParser.parse(parser.parse(options, simplest));

        final String[] withArgs =
                {"-createTask", "-token", "_", "-url", "_", "-appName", "_",
                 "-args", "_"};
        CreateTaskParser.parse(parser.parse(options, withArgs));

        final String[] withInputs =
                {"-createTask", "-token", "_", "-url", "_", "-appName", "_",
                 "-inputs", "_"};
        CreateTaskParser.parse(parser.parse(options, withInputs));

        final String[] withOutputs =
                {"-createTask", "-token", "_", "-url", "_", "-appName", "_",
                 "-outputs", "_"};
        CreateTaskParser.parse(parser.parse(options, withOutputs));
    }
}
