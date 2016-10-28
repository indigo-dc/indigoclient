package pl.psnc.indigo.fg.api.restful;

import org.junit.Assert;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import pl.psnc.indigo.fg.api.restful.category.IntegrationTests;
import pl.psnc.indigo.fg.api.restful.exceptions.FutureGatewayException;
import pl.psnc.indigo.fg.api.restful.jaxb.Application;
import pl.psnc.indigo.fg.api.restful.jaxb.InputFile;
import pl.psnc.indigo.fg.api.restful.jaxb.OutputFile;
import pl.psnc.indigo.fg.api.restful.jaxb.Task;
import pl.psnc.indigo.fg.api.restful.jaxb.TaskStatus;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import static junit.framework.TestCase.assertFalse;
import static junit.framework.TestCase.assertTrue;
import static org.awaitility.Awaitility.await;
import static org.hamcrest.core.Is.is;

@Category(IntegrationTests.class)
public class FutureGatewayTest {
    public static final String USERNAME = "brunor";

    @Test
    public final void testGetRoot() throws FutureGatewayException {
        new RootAPI(RootAPI.LOCALHOST_ADDRESS, "");
    }

    @Test
    public final void testGetAllApplications() throws FutureGatewayException {
        ApplicationsAPI api =
                new ApplicationsAPI(RootAPI.LOCALHOST_ADDRESS, "");
        api.getAllApplications();
    }

    @Test
    public final void testGetApplication() throws FutureGatewayException {
        ApplicationsAPI api =
                new ApplicationsAPI(RootAPI.LOCALHOST_ADDRESS, "");
        Application application = api.getApplication("1");
        String name = application.getName();
        Assert.assertThat("hostname", is(name));
    }

    @Test
    public final void testCreateTask() throws FutureGatewayException {
        TasksAPI api = new TasksAPI(RootAPI.LOCALHOST_ADDRESS, "");

        Task task = new Task();
        task.setUser(FutureGatewayTest.USERNAME);
        task.setApplication("1");
        task.setDescription("hello");
        Task result = api.createTask(task);

        // Check the status of this task
        String id = result.getId();
        api.getTask(id);
    }

    @Test
    public final void testSubmitTaskWithFiles() throws FutureGatewayException {
        TasksAPI api = new TasksAPI(RootAPI.LOCALHOST_ADDRESS, "");

        Task newTask = new Task();
        newTask.setUser(FutureGatewayTest.USERNAME);
        newTask.setApplication("2");
        newTask.setDescription("Test with files");

        List<String> arguments = new ArrayList<>(1);
        arguments.add("I am saying hello");

        List<OutputFile> outputFiles = new ArrayList<>(1);
        OutputFile oFile = new OutputFile();
        oFile.setName("sayhello.data");
        outputFiles.add(oFile);

        List<InputFile> inputFiles = new ArrayList<>(2);
        InputFile iFileSH = new InputFile();
        iFileSH.setName("sayhello.sh");

        InputFile iFileTXT = new InputFile();
        iFileTXT.setName("sayhello.txt");

        inputFiles.add(iFileSH);
        inputFiles.add(iFileTXT);

        newTask.setOutputFiles(outputFiles);
        newTask.setInputFiles(inputFiles);

        newTask.setArguments(arguments);
        Task result = api.createTask(newTask);

        // Once task is created, we can upload files
        String fileNameSH = getClass().getResource("/sayhello.sh").getFile();
        String fileNameTXT = getClass().getResource("/sayhello.txt").getFile();
        api.uploadFileForTask(result, new File(fileNameSH));
        api.uploadFileForTask(result, new File(fileNameTXT));
    }

    /**
     * This test is a complete scenario where job is submitted, executed and all
     * outputs are retrieved.
     */
    @Test
    public final void testSubmitTaskWithFilesWaitGetOutputs()
            throws FutureGatewayException, InterruptedException,
                   URISyntaxException, IOException {
        TasksAPI api = new TasksAPI(RootAPI.LOCALHOST_ADDRESS, "");

        Task newTask = new Task();
        newTask.setUser(FutureGatewayTest.USERNAME);
        newTask.setApplication("2");
        newTask.setDescription("Test with files");

        List<String> arguments = new ArrayList<>(1);
        arguments.add("I am saying hello");

        List<OutputFile> outputFiles = new ArrayList<>(1);
        OutputFile oFile = new OutputFile();
        oFile.setName("sayhello.data");
        outputFiles.add(oFile);

        List<InputFile> inputFiles = new ArrayList<>(2);
        InputFile iFileSH = new InputFile();
        iFileSH.setName("sayhello.sh");

        InputFile iFileTXT = new InputFile();
        iFileTXT.setName("sayhello.txt");

        inputFiles.add(iFileSH);
        inputFiles.add(iFileTXT);

        newTask.setOutputFiles(outputFiles);
        newTask.setInputFiles(inputFiles);

        newTask.setArguments(arguments);
        Task result = api.createTask(newTask);
        String id = result.getId();

        // Once task is created, we can upload files
        String fileNameSH = getClass().getResource("/sayhello.sh").getFile();
        String fileNameTXT = getClass().getResource("/sayhello.txt").getFile();
        api.uploadFileForTask(result, new File(fileNameSH));
        api.uploadFileForTask(result, new File(fileNameTXT));

        await().atMost(10, TimeUnit.MINUTES).until(() -> {
            Task task = api.getTask(id);
            return task.getStatus() == TaskStatus.DONE;
        });

        List<OutputFile> files = api.getOutputsForTask(id);
        String file = getClass().getResource("/outputs").getFile();
        File outputDir = new File(file);

        for (OutputFile outputFile : files) {
            api.downloadOutputFile(outputFile, outputDir);
        }
    }

    @Test
    public final void testGetTask() throws FutureGatewayException {
        TasksAPI api = new TasksAPI(RootAPI.LOCALHOST_ADDRESS, "");

        Task newTask = new Task();
        newTask.setUser(FutureGatewayTest.USERNAME);
        newTask.setId("1");
        api.getTask("1");
    }

    @Test
    public final void testGetAllTasks() throws FutureGatewayException {
        TasksAPI api = new TasksAPI(RootAPI.LOCALHOST_ADDRESS, "");
        api.getAllTasks(FutureGatewayTest.USERNAME);
    }

    @Test
    public final void testDeleteTask() throws FutureGatewayException {
        TasksAPI api = new TasksAPI(RootAPI.LOCALHOST_ADDRESS, "");

        Task task = new Task();
        task.setApplication("1");
        task.setUser(FutureGatewayTest.USERNAME);
        task = api.createTask(task);

        String id = task.getId();
        assertTrue(api.deleteTask(id));
        assertFalse(api.deleteTask(id));
    }
}
