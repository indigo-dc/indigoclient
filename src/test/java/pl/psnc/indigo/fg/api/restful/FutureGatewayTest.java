package pl.psnc.indigo.fg.api.restful;

import org.apache.commons.io.FileUtils;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import pl.psnc.indigo.fg.api.restful.category.IntegrationTests;
import pl.psnc.indigo.fg.api.restful.exceptions.FutureGatewayException;
import pl.psnc.indigo.fg.api.restful.jaxb.Application;
import pl.psnc.indigo.fg.api.restful.jaxb.InputFile;
import pl.psnc.indigo.fg.api.restful.jaxb.KeyValue;
import pl.psnc.indigo.fg.api.restful.jaxb.OutputFile;
import pl.psnc.indigo.fg.api.restful.jaxb.PatchRuntimeData;
import pl.psnc.indigo.fg.api.restful.jaxb.RuntimeData;
import pl.psnc.indigo.fg.api.restful.jaxb.Task;
import pl.psnc.indigo.fg.api.restful.jaxb.TaskStatus;

import javax.ws.rs.core.UriBuilder;
import java.io.File;
import java.net.URI;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

import static org.awaitility.Awaitility.await;
import static org.junit.Assert.*;

@Category(IntegrationTests.class)
public class FutureGatewayTest {
    private static final URI URI = UriBuilder.fromUri(
            System.getProperty("futuregateway.uri",
                               "https://fgw01.ncg.ingrid.pt/apis")).build();
    private static final String AUTHORIZATION_TOKEN =
            System.getProperty("auth.token", "TOKEN");
    private static final String TASK_DESCRIPTION = "Integration Testing";
    private static final String SAYHELLO_DATA = "sayhello.data";
    private static final String SAYHELLO_SH = "sayhello.sh";
    private static final String SAYHELLO_TXT = "sayhello.txt";
    private static final String DATA_NAME = "dataName";
    private static final String DATA_VALUE = "dataValue";

    @Test
    public final void testGetRoot() throws FutureGatewayException {
        new RootAPI(FutureGatewayTest.URI,
                    FutureGatewayTest.AUTHORIZATION_TOKEN);
    }

    @Test
    public final void testGetAllApplications() throws Exception {
        final ApplicationsAPI api = new ApplicationsAPI(FutureGatewayTest.URI,
                                                        FutureGatewayTest
                                                                .AUTHORIZATION_TOKEN);
        api.getAllApplications();
    }

    @Test
    public final void testGetApplication() throws Exception {
        final ApplicationsAPI api = new ApplicationsAPI(FutureGatewayTest.URI,
                                                        FutureGatewayTest
                                                                .AUTHORIZATION_TOKEN);
        final Application application = api.getApplication("1");
        final String name = application.getName();
        assertEquals("hostname", name);
    }

    @Test
    public final void testCreateTask() throws FutureGatewayException {
        final TasksAPI api = new TasksAPI(FutureGatewayTest.URI,
                                          FutureGatewayTest
                                                  .AUTHORIZATION_TOKEN);

        final Task task = new Task();
        task.setApplication("1");
        task.setDescription(FutureGatewayTest.TASK_DESCRIPTION);
        final Task result = api.createTask(task);

        // Check the status of this task
        final String id = result.getId();
        api.getTask(id);
    }

    @Test
    public final void testSubmitTaskWithFiles() throws Exception {
        final TasksAPI api = new TasksAPI(FutureGatewayTest.URI,
                                          FutureGatewayTest
                                                  .AUTHORIZATION_TOKEN);

        final Task newTask = new Task();
        newTask.setApplication("2");
        newTask.setDescription(FutureGatewayTest.TASK_DESCRIPTION);

        final List<String> arguments = new ArrayList<>(1);
        arguments.add(FutureGatewayTest.TASK_DESCRIPTION);

        final OutputFile oFile = new OutputFile();
        oFile.setName(FutureGatewayTest.SAYHELLO_DATA);

        final List<OutputFile> outputFiles = new ArrayList<>(1);
        outputFiles.add(oFile);

        final InputFile iFileSH = new InputFile();
        iFileSH.setName(FutureGatewayTest.SAYHELLO_SH);

        final InputFile iFileTXT = new InputFile();
        iFileTXT.setName(FutureGatewayTest.SAYHELLO_TXT);

        final List<InputFile> inputFiles = new ArrayList<>(2);
        inputFiles.add(iFileSH);
        inputFiles.add(iFileTXT);

        newTask.setOutputFiles(outputFiles);
        newTask.setInputFiles(inputFiles);

        newTask.setArguments(arguments);
        final Task result = api.createTask(newTask);

        // Once task is created, we can upload files
        final File fileNameSH =
                Helper.getResourceFile(FutureGatewayTest.SAYHELLO_SH);
        final File fileNameTXT =
                Helper.getResourceFile(FutureGatewayTest.SAYHELLO_TXT);
        api.uploadFileForTask(result, fileNameSH, fileNameTXT);
    }

    /**
     * This test is a complete scenario where job is submitted, executed and all
     * outputs are retrieved.
     */
    @Test
    public final void testSubmitTaskWithFilesWaitGetOutputs()
            throws FutureGatewayException {
        final TasksAPI api = new TasksAPI(FutureGatewayTest.URI,
                                          FutureGatewayTest
                                                  .AUTHORIZATION_TOKEN);

        final Task newTask = new Task();
        newTask.setApplication("2");
        newTask.setDescription(FutureGatewayTest.TASK_DESCRIPTION);

        final List<String> arguments = new ArrayList<>(1);
        arguments.add(FutureGatewayTest.TASK_DESCRIPTION);

        final OutputFile oFile = new OutputFile();
        oFile.setName(FutureGatewayTest.SAYHELLO_DATA);

        final List<OutputFile> outputFiles = new ArrayList<>(1);
        outputFiles.add(oFile);

        final InputFile iFileSH = new InputFile();
        iFileSH.setName(FutureGatewayTest.SAYHELLO_SH);

        final InputFile iFileTXT = new InputFile();
        iFileTXT.setName(FutureGatewayTest.SAYHELLO_TXT);

        final List<InputFile> inputFiles = new ArrayList<>(2);
        inputFiles.add(iFileSH);
        inputFiles.add(iFileTXT);

        newTask.setOutputFiles(outputFiles);
        newTask.setInputFiles(inputFiles);

        newTask.setArguments(arguments);
        final Task result = api.createTask(newTask);
        final String id = result.getId();

        // Once task is created, we can upload files
        final File fileNameSH =
                Helper.getResourceFile(FutureGatewayTest.SAYHELLO_SH);
        final File fileNameTXT =
                Helper.getResourceFile(FutureGatewayTest.SAYHELLO_TXT);
        api.uploadFileForTask(result, fileNameSH, fileNameTXT);

        await().atMost(10, TimeUnit.MINUTES).pollInterval(5, TimeUnit.SECONDS)
               .until(() -> {
                   final Task task = api.getTask(id);
                   return task.getStatus() == TaskStatus.DONE;
               });

        final List<OutputFile> files = api.getTask(id).getOutputFiles();
        final File outputDir = new File(FileUtils.getTempDirectory(),
                                        UUID.randomUUID().toString());
        for (final OutputFile outputFile : files) {
            api.downloadOutputFile(outputFile, outputDir);
        }
        FileUtils.deleteQuietly(outputDir);
    }

    @Test
    public final void testGetAllTasks() throws FutureGatewayException {
        final TasksAPI api = new TasksAPI(FutureGatewayTest.URI,
                                          FutureGatewayTest
                                                  .AUTHORIZATION_TOKEN);
        api.getAllTasks();
    }

    @Test
    public final void testDeleteTask() throws FutureGatewayException {
        final TasksAPI api = new TasksAPI(FutureGatewayTest.URI,
                                          FutureGatewayTest
                                                  .AUTHORIZATION_TOKEN);

        Task task = new Task();
        task.setApplication("1");
        task = api.createTask(task);

        final String id = task.getId();
        assertTrue(api.removeTask(id));
        assertFalse(api.removeTask(id));
    }

    @Test
    public final void testPatchTask() throws FutureGatewayException {
        final TasksAPI api = new TasksAPI(FutureGatewayTest.URI,
                                          FutureGatewayTest
                                                  .AUTHORIZATION_TOKEN);

        Task task = new Task();
        task.setApplication("1");
        task = api.createTask(task);
        assertTrue(task.getRuntimeData().isEmpty());

        final PatchRuntimeData patchRuntimeData = new PatchRuntimeData();
        patchRuntimeData.setRuntimeData(Collections.singletonList(
                new KeyValue(FutureGatewayTest.DATA_NAME,
                             FutureGatewayTest.DATA_VALUE)));
        api.patchRuntimeData(task.getId(), patchRuntimeData);
        task = api.getTask(task.getId());

        final List<RuntimeData> runtimeData = task.getRuntimeData();
        assertEquals(1, runtimeData.size());
        assertEquals(FutureGatewayTest.DATA_NAME, runtimeData.get(0).getName());
        assertEquals(FutureGatewayTest.DATA_VALUE,
                     runtimeData.get(0).getValue());
    }
}
