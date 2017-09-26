package pl.psnc.indigo.cli.parser;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.MissingArgumentException;
import org.apache.commons.cli.ParseException;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import pl.psnc.indigo.cli.OptionComparator;
import pl.psnc.indigo.fg.api.restful.category.UnitTests;

@Category(UnitTests.class)
public class GetTaskParserTest {
    @Test(expected = IllegalArgumentException.class)
    public final void parseNoToken() throws Exception {
        final CommandLineParser parser = new DefaultParser();
        final String[] arguments = {"-getTask", "_"};
        final CommandLine commandLine =
                parser.parse(OptionComparator.options(), arguments);
        GetTaskParser.parse(commandLine);
    }

    @Test(expected = IllegalArgumentException.class)
    public final void parseNoUrl() throws Exception {
        final CommandLineParser parser = new DefaultParser();
        final String[] arguments = {"-getTask", "_", "-token", "_"};
        final CommandLine commandLine =
                parser.parse(OptionComparator.options(), arguments);
        GetTaskParser.parse(commandLine);
    }

    @Test(expected = MissingArgumentException.class)
    public final void parseWrongArgCount() throws Exception {
        final CommandLineParser parser = new DefaultParser();
        final String[] arguments = {"-getTask"};
        final CommandLine commandLine =
                parser.parse(OptionComparator.options(), arguments);
        GetTaskParser.parse(commandLine);
    }

    @Test
    public final void parse() throws ParseException {
        final CommandLineParser parser = new DefaultParser();
        final String[] arguments =
                {"-getTask", "_", "-token", "_", "-url", "_"};
        final CommandLine commandLine =
                parser.parse(OptionComparator.options(), arguments);
        GetTaskParser.parse(commandLine);
    }
}
