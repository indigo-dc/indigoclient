package pl.psnc.indigo.cli.parser;

import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.junit.runner.RunWith;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import pl.psnc.indigo.cli.commands.AbstractCommand;
import pl.psnc.indigo.cli.commands.CreateTask;
import pl.psnc.indigo.cli.commands.GetTask;
import pl.psnc.indigo.cli.commands.Help;
import pl.psnc.indigo.cli.commands.ListApplications;
import pl.psnc.indigo.cli.commands.UploadFile;
import pl.psnc.indigo.fg.api.restful.ApplicationsAPI;
import pl.psnc.indigo.fg.api.restful.TasksAPI;
import pl.psnc.indigo.fg.api.restful.category.UnitTests;
import pl.psnc.indigo.fg.api.restful.jaxb.Application;
import pl.psnc.indigo.fg.api.restful.jaxb.Task;
import pl.psnc.indigo.fg.api.restful.jaxb.Upload;

import java.io.File;
import java.util.Collections;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

@PrepareForTest({ListApplications.class, GetTask.class, UploadFile.class,
                 CreateTask.class})
@RunWith(PowerMockRunner.class)
@Category(UnitTests.class)
public class CommandTest {
    @Test
    public final void getTask() throws Exception {
        // mock TasksAPI
        final TasksAPI api = Mockito.mock(TasksAPI.class);
        Mockito.when(api.getTask(ArgumentMatchers.anyString()))
               .thenReturn(new Task());

        // mock GetTask, so that 'new TasksAPI()' returns mock
        PowerMockito.whenNew(TasksAPI.class).withAnyArguments().thenReturn(api);

        final AbstractCommand getTask = new GetTask("", "", "");
        getTask.execute();
    }

    @Test
    public final void listApplications() throws Exception {
        // mock ApplicationsAPI
        final ApplicationsAPI api = Mockito.mock(ApplicationsAPI.class);
        Mockito.when(api.getAllApplications())
               .thenReturn(Collections.emptyList())
               .thenReturn(Collections.singletonList(new Application()));

        // mock ListApplications, so that 'new ApplicationsAPI()' returns mock
        PowerMockito.whenNew(ApplicationsAPI.class).withAnyArguments()
                    .thenReturn(api);

        final AbstractCommand listApplications = new ListApplications("", "");
        listApplications.execute();
        listApplications.execute();
    }

    @Test
    public final void uploadFile() throws Exception {
        // mock Upload
        final Upload upload = Mockito.mock(Upload.class);
        Mockito.when(upload.getMessage()).thenReturn("uploaded")
               .thenReturn("error");

        // mock TasksAPI
        final TasksAPI api = Mockito.mock(TasksAPI.class);
        Mockito.when(api.uploadFileForTask(ArgumentMatchers.any(Task.class),
                                           ArgumentMatchers.any(File.class)))
               .thenReturn(upload);

        // mock GetTask, so that 'new TasksAPI()' returns mock
        PowerMockito.whenNew(TasksAPI.class).withAnyArguments().thenReturn(api);

        final AbstractCommand uploadFile = new UploadFile("", "", "", "");
        assertEquals(0, uploadFile.execute());
        assertNotEquals(0, uploadFile.execute());
    }

    @Test
    public final void createTask() throws Exception {
        // mock Upload
        final Task task = Mockito.mock(Task.class);
        Mockito.when(task.getId()).thenReturn("");

        // mock TasksAPI
        final TasksAPI api = Mockito.mock(TasksAPI.class);
        Mockito.when(api.createTask(ArgumentMatchers.any(Task.class)))
               .thenReturn(task);

        // mock GetTask, so that 'new TasksAPI()' returns mock
        PowerMockito.whenNew(TasksAPI.class).withAnyArguments().thenReturn(api);

        final AbstractCommand createTask =
                new CreateTask("", "", Collections.emptyList(),
                               Collections.emptyList(), Collections.emptyList(),
                               "", "");
        createTask.execute();

        final AbstractCommand createTaskWithInputs =
                new CreateTask("", "", Collections.emptyList(),
                               Collections.singletonList(""),
                               Collections.emptyList(), "", "");
        createTaskWithInputs.execute();

        final AbstractCommand createTaskWithOutputs =
                new CreateTask("", "", Collections.emptyList(),
                               Collections.emptyList(),
                               Collections.singletonList(""), "", "");
        createTaskWithOutputs.execute();
    }

    @Test
    public final void help() {
        final Help help = new Help();
        help.execute();
    }
}
