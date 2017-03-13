package pl.psnc.indigo.fg.api.restful;

import org.junit.Test;
import org.junit.experimental.categories.Category;
import pl.psnc.indigo.fg.api.restful.category.IntegrationTests;
import pl.psnc.indigo.fg.api.restful.exceptions.FutureGatewayException;
import pl.psnc.indigo.fg.api.restful.jaxb.Application;
import pl.psnc.indigo.fg.api.restful.jaxb.InputFile;
import pl.psnc.indigo.fg.api.restful.jaxb.OutputFile;
import pl.psnc.indigo.fg.api.restful.jaxb.PatchRuntimeData;
import pl.psnc.indigo.fg.api.restful.jaxb.RuntimeData;
import pl.psnc.indigo.fg.api.restful.jaxb.Task;
import pl.psnc.indigo.fg.api.restful.jaxb.TaskStatus;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.TimeUnit;

import static org.awaitility.Awaitility.await;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

@Category(IntegrationTests.class)
public class FutureGatewayTest {
    public static final String USERNAME = "brunor";

    @Test
    public final void testGetRoot() throws FutureGatewayException {
        new RootAPI(RootAPI.LOCALHOST_ADDRESS, "testing");
    }

    @Test
    public final void testGetAllApplications() throws Exception {
        ApplicationsAPI api =
                new ApplicationsAPI(RootAPI.LOCALHOST_ADDRESS, "testing");
        api.getAllApplications();
    }

    @Test
    public final void testGetApplication() throws Exception {
        ApplicationsAPI api =
                new ApplicationsAPI(RootAPI.LOCALHOST_ADDRESS, "testing");
        Application application = api.getApplication("1");
        String name = application.getName();
        assertEquals("hostname", name);
    }

    @Test
    public final void testCreateTask() throws FutureGatewayException {
        TasksAPI api = new TasksAPI(RootAPI.LOCALHOST_ADDRESS, "testing");

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
        TasksAPI api = new TasksAPI(RootAPI.LOCALHOST_ADDRESS, "testing");

        Task newTask = new Task();
        newTask.setUser(FutureGatewayTest.USERNAME);
        newTask.setApplication("2");
        newTask.setDescription("Test with files");

        List<String> arguments = new ArrayList<>(1);
        arguments.add("I am saying hello");

        OutputFile oFile = new OutputFile();
        oFile.setName("sayhello.data");

        List<OutputFile> outputFiles = new ArrayList<>(1);
        outputFiles.add(oFile);

        InputFile iFileSH = new InputFile();
        iFileSH.setName("sayhello.sh");

        InputFile iFileTXT = new InputFile();
        iFileTXT.setName("sayhello.txt");

        List<InputFile> inputFiles = new ArrayList<>(2);
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
            throws FutureGatewayException {
        TasksAPI api = new TasksAPI(RootAPI.LOCALHOST_ADDRESS, "testing");

        Task newTask = new Task();
        newTask.setUser(FutureGatewayTest.USERNAME);
        newTask.setApplication("2");
        newTask.setDescription("Test with files");

        List<String> arguments = new ArrayList<>(1);
        arguments.add("I am saying hello");

        OutputFile oFile = new OutputFile();
        oFile.setName("sayhello.data");

        List<OutputFile> outputFiles = new ArrayList<>(1);
        outputFiles.add(oFile);

        InputFile iFileSH = new InputFile();
        iFileSH.setName("sayhello.sh");

        InputFile iFileTXT = new InputFile();
        iFileTXT.setName("sayhello.txt");

        List<InputFile> inputFiles = new ArrayList<>(2);
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

        await().atMost(10, TimeUnit.MINUTES).pollInterval(5, TimeUnit.SECONDS)
               .until(() -> {
                   Task task = api.getTask(id);
                   return task.getStatus() == TaskStatus.DONE;
               });

        List<OutputFile> files = api.getTask(id).getOutputFiles();
        String file = getClass().getResource("/outputs").getFile();
        File outputDir = new File(file);

        for (final OutputFile outputFile : files) {
            api.downloadOutputFile(outputFile, outputDir);
        }
    }

    @Test
    public final void testGetTask() throws FutureGatewayException {
        TasksAPI api = new TasksAPI(RootAPI.LOCALHOST_ADDRESS, "testing");

        Task newTask = new Task();
        newTask.setUser(FutureGatewayTest.USERNAME);
        newTask.setId("1");
        api.getTask("1");
    }

    @Test
    public final void testGetAllTasks() throws FutureGatewayException {
        TasksAPI api = new TasksAPI(RootAPI.LOCALHOST_ADDRESS, "testing");
        api.getAllTasks(FutureGatewayTest.USERNAME);
    }

    @Test
    public final void testDeleteTask() throws FutureGatewayException {
        TasksAPI api = new TasksAPI(RootAPI.LOCALHOST_ADDRESS, "testing");

        Task task = new Task();
        task.setApplication("1");
        task.setUser(FutureGatewayTest.USERNAME);
        task = api.createTask(task);

        String id = task.getId();
        assertTrue(api.removeTask(id));
        assertFalse(api.removeTask(id));
    }

    @Test
    public final void testPatchTask() throws FutureGatewayException {
        TasksAPI api = new TasksAPI(RootAPI.LOCALHOST_ADDRESS, "testing");

        Task task = new Task();
        task.setApplication("1");
        task.setUser(FutureGatewayTest.USERNAME);
        task = api.createTask(task);
        assertTrue(task.getRuntimeData().isEmpty());

        PatchRuntimeData patchRuntimeData = new PatchRuntimeData();
        patchRuntimeData.setRuntimeData(Collections.singletonList(
                new PatchRuntimeData.KeyValue("dataName", "dataValue")));
        api.patchRuntimeData(task.getId(), patchRuntimeData);
        task = api.getTask(task.getId());

        List<RuntimeData> runtimeData = task.getRuntimeData();
        assertEquals(1, runtimeData.size());
        assertEquals("dataName", runtimeData.get(0).getName());
        assertEquals("dataValue", runtimeData.get(0).getValue());
    }
}
