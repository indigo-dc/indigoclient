package pl.psnc.indigo.fg.api.restful;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import pl.psnc.indigo.fg.api.restful.exceptions.FutureGatewayException;
import pl.psnc.indigo.fg.api.restful.jaxb.InputFile;
import pl.psnc.indigo.fg.api.restful.jaxb.OutputFile;
import pl.psnc.indigo.fg.api.restful.jaxb.Task;
import pl.psnc.indigo.fg.api.restful.jaxb.Task.Status;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

public class TasksAPITest {
    public static final String USERNAME = "brunor";
    private TasksAPI api;

    @Before
    public final void initialize()
            throws FutureGatewayException, URISyntaxException {
        api = new TasksAPI(RootAPI.LOCALHOST_ADDRESS);
    }

    @Test
    public final void testCreateTask() throws FutureGatewayException {
        Task task = new Task();
        task.setUser(TasksAPITest.USERNAME);
        task.setApplication("1");
        task.setDescription("hello");
        Task result = api.createTask(task);

        // Check the status of this task
        String id = result.getId();
        api.getTask(id);
    }

    @Test
    public final void testFileAccess() {
        String fileName = getClass().getResource("/sayhello.sh").getFile();
        Assert.assertThat(new File(fileName).canRead(), is(true));
    }

    @Test
    public final void testSubmitTaskWithFiles() throws FutureGatewayException {
        Task newTask = new Task();
        newTask.setUser(TasksAPITest.USERNAME);
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
     * This test is a complete scenario where job is submitted, executed
     * and all outputs are retrieved.
     */
    @Test
    public final void testSubmitTaskWithFilesWaitGetOutputs()
            throws FutureGatewayException, InterruptedException,
                   URISyntaxException, IOException {
        Task newTask = new Task();
        newTask.setUser(TasksAPITest.USERNAME);
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

        // We can check status and wait for "DONE"
        Status status;
        int retry = 100;

        do {
            String id = result.getId();
            Task tmp = api.getTask(id);
            status = tmp.getStatus();
            Thread.sleep(5000L);
            retry--;
        } while ((status != Status.DONE) && (retry > 0));

        if (retry == 0) {
            fail("To many retries");
        }

        String id = result.getId();
        List<OutputFile> files = api.getOutputsForTask(id);
        String file = getClass().getResource("/outputs").getFile();
        File outputDir = new File(file);

        for (OutputFile outputFile : files) {
            api.downloadOutputFile(outputFile, outputDir);
        }
    }

    @Test
    public final void testGetTask() throws FutureGatewayException {
        // TODO: make sure to set proper task ID below
        // this one is an arbitrary value from previous calls to
        // TaskAPI
        Task newTask = new Task();
        newTask.setUser(TasksAPITest.USERNAME);
        newTask.setId("1");
        api.getTask("1");
    }

    @Test
    public final void testGetAllTasks() throws FutureGatewayException {
        api.getAllTasks(TasksAPITest.USERNAME);
    }

    @Test
    public final void testDeleteTask() throws FutureGatewayException {
        Task task = new Task();
        task.setApplication("1");
        task.setUser(TasksAPITest.USERNAME);
        task = api.createTask(task);

        String id = task.getId();
        assertTrue(api.deleteTask(id));
        assertFalse(api.deleteTask(id));
    }
}
