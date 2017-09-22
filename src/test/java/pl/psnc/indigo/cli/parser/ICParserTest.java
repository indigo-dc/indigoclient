package pl.psnc.indigo.cli.parser;

import org.junit.Test;
import org.junit.experimental.categories.Category;
import pl.psnc.indigo.cli.commands.AbstractCommand;
import pl.psnc.indigo.cli.commands.CreateTask;
import pl.psnc.indigo.cli.commands.GetTask;
import pl.psnc.indigo.cli.commands.Help;
import pl.psnc.indigo.cli.commands.ListApplications;
import pl.psnc.indigo.cli.commands.UploadFile;
import pl.psnc.indigo.fg.api.restful.category.UnitTests;

import java.util.List;

import static org.hamcrest.CoreMatchers.instanceOf;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;

@Category(UnitTests.class)
public class ICParserTest {
    @Test(expected = IllegalArgumentException.class)
    public final void parseEmptyArgs() throws Exception {
        final ICParser parser = new ICParser(new String[0]);
        parser.parse();
    }

    @Test
    public final void parse() throws Exception {
        final ICParser helpParser = new ICParser(new String[]{"-help"});
        final List<AbstractCommand> help = helpParser.parse();
        assertEquals(1, help.size());
        assertThat(help.get(0), instanceOf(Help.class));

        final ICParser listAppsParser = new ICParser(
                new String[]{"-listApps", "-token", "_", "-url", "_"});
        final List<AbstractCommand> listApps = listAppsParser.parse();
        assertEquals(1, listApps.size());
        assertThat(listApps.get(0), instanceOf(ListApplications.class));

        final ICParser createTaskParser = new ICParser(
                new String[]{"-createTask", "-token", "_", "-url", "_",
                             "-appName", "_"});
        final List<AbstractCommand> createTask = createTaskParser.parse();
        assertEquals(1, createTask.size());
        assertThat(createTask.get(0), instanceOf(CreateTask.class));

        final ICParser getTaskParser = new ICParser(
                new String[]{"-getTask", "_", "-token", "_", "-url", "_"});
        final List<AbstractCommand> getTask = getTaskParser.parse();
        assertEquals(1, getTask.size());
        assertThat(getTask.get(0), instanceOf(GetTask.class));

        final ICParser uploadFileParser = new ICParser(
                new String[]{"-uploadFile", "_", "_", "-token", "_", "-url",
                             "_"});
        final List<AbstractCommand> uploadFile = uploadFileParser.parse();
        assertEquals(1, uploadFile.size());
        assertThat(uploadFile.get(0), instanceOf(UploadFile.class));
    }
}
