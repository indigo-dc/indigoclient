package pl.psnc.indigo.fg.api.restful;

import org.junit.Before;
import org.junit.Test;
import pl.psnc.indigo.fg.api.restful.exceptions.FutureGatewayException;
import pl.psnc.indigo.fg.api.restful.jaxb.InputFile;
import pl.psnc.indigo.fg.api.restful.jaxb.OutputFile;
import pl.psnc.indigo.fg.api.restful.jaxb.Task;

import java.io.File;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

public class TasksAPITest {
    private TasksAPI api;

    @Before
    public void initialize() throws FutureGatewayException, URISyntaxException {
        api = new TasksAPI(BaseAPI.LOCALHOST_ADDRESS);
    }

    @Test
    public void testCreateTask() throws FutureGatewayException {
        Task task = new Task();
        task.setUser("brunor");
        task.setApplication("1");
        task.setDescription("hello");
        Task result = api.createTask(task);

        // Check the status of this task
        api.getTask(result);
    }

    @Test
    public void testFileAccess() {
        String fileName = getClass().getResource("/sayhello.sh").getFile();
        assertTrue(new File(fileName).canRead());
    }

    @Test
    public void testSubmitTaskWithFiles() throws FutureGatewayException {
        Task newTask = new Task();
        newTask.setUser("brunor");
        newTask.setApplication("2");
        newTask.setDescription("Test with files");

        List<String> arguments = new ArrayList<>();
        arguments.add("I am saying hello");

        List<OutputFile> outputFiles = new ArrayList<>();
        OutputFile oFile = new OutputFile();
        oFile.setName("sayhello.data");
        outputFiles.add(oFile);

        List<InputFile> inputFiles = new ArrayList<>();
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
    public void testSubmitTaskWithFilesWaitGetOutputs() throws FutureGatewayException, URISyntaxException, InterruptedException {
        Task newTask = new Task();
        newTask.setUser("brunor");
        newTask.setApplication("2");
        newTask.setDescription("Test with files");

        ArrayList<String> arguments = new ArrayList<>();
        arguments.add("I am saying hello");

        ArrayList<OutputFile> outputFiles = new ArrayList<>();
        OutputFile oFile = new OutputFile();
        oFile.setName("sayhello.data");
        outputFiles.add(oFile);

        ArrayList<InputFile> inputFiles = new ArrayList<>();
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
        Task.Status status;
        int retry = 100;

        do {
            Task tmp = api.getTask(result);
            status = tmp.getStatus();
            Thread.sleep(5000);
            retry--;
        } while (status != Task.Status.DONE && retry > 0);

        if (retry == 0) {
            fail("To many retries");
        }

        List<OutputFile> files = api.getOutputsForTask(result);
        File outputDir = new File(getClass().getResource("/outputs").getFile());

        for (OutputFile f : files) {
            api.downloadOutputFile(f, outputDir);
        }
    }

    @Test
    public void testGetTask() throws FutureGatewayException {
        // TODO: make sure to set proper task ID below
        // this one is an arbitrary value from previous calls to
        // TaskAPI
        Task newTask = new Task();
        newTask.setUser("brunor");
        newTask.setId("1");
        api.getTask(newTask);
    }

    @Test
    public void testGetAllTasks() throws FutureGatewayException, URISyntaxException {
        TasksAPI api = new TasksAPI(BaseAPI.LOCALHOST_ADDRESS);
        api.getAllTasks("brunor");
    }

    @Test
    public void testDeleteTask() throws FutureGatewayException {
        Task task = new Task();
        task.setApplication("1");
        task.setUser("brunor");
        task = api.createTask(task);

        assertTrue(api.deleteTask(task));
        assertFalse(api.deleteTask(task));
    }
}
