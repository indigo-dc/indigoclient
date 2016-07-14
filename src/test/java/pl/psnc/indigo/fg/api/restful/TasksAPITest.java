package pl.psnc.indigo.fg.api.restful;

import org.apache.commons.io.FileUtils;
import org.junit.Before;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import pl.psnc.indigo.fg.api.restful.category.UnitTests;
import pl.psnc.indigo.fg.api.restful.exceptions.FutureGatewayException;
import pl.psnc.indigo.fg.api.restful.jaxb.OutputFile;
import pl.psnc.indigo.fg.api.restful.jaxb.Task;
import pl.psnc.indigo.fg.api.restful.jaxb.Task.Status;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.nio.charset.Charset;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@Category(UnitTests.class)
public class TasksAPITest {
    private MockRestSession session;
    private TasksAPI api;

    @Before
    public void before() throws IOException, FutureGatewayException {
        session = new MockRestSession();
        api = new TasksAPI(MockRestSession.MOCK_ADDRESS, session.getClient());
    }

    @Test
    public final void testGetAllTasks() throws FutureGatewayException {
        List<Task> tasks = api.getAllTasks("all-tasks");
        assertEquals(3, tasks.size());

        Task task = tasks.get(0);
        assertEquals(2, task.getOutputFiles().size());
        assertEquals(2, task.getLinks().size());
    }

    @Test
    public final void testGetTask() throws FutureGatewayException {
        Task task = api.getTask("1");
        assertEquals("1", task.getId());
        assertEquals("2", task.getApplication());
        assertEquals("Test with files", task.getDescription());
        assertEquals("brunor", task.getUser());
        assertEquals(Status.DONE, task.getStatus());
    }

    @Test(expected = FutureGatewayException.class)
    public void testGetTaskInvalidUri() throws FutureGatewayException {
        api.getTask("invalid-uri");
    }

    @Test(expected = FutureGatewayException.class)
    public void testGetTaskInvalidBody() throws FutureGatewayException {
        api.getTask("invalid-body");
    }

    @Test
    public void testUploadFileForTask() throws FutureGatewayException {
        Task task = new Task();
        task.setUser("brunor");
        task.setId("1");

        File file = mock(File.class);
        api.uploadFileForTask(task, file);
    }

    @Test(expected = FutureGatewayException.class)
    public void testUploadFileForTaskInvalidUser()
            throws FutureGatewayException {
        Task task = new Task();
        task.setUser("invalid-uri");
        task.setId("1");

        File file = mock(File.class);
        api.uploadFileForTask(task, file);
    }

    @Test(expected = FutureGatewayException.class)
    public void testUploadFileForTaskInvalidBody()
            throws FutureGatewayException {
        Task task = new Task();
        task.setUser("invalid-body");
        task.setId("1");

        File file = mock(File.class);
        api.uploadFileForTask(task, file);
    }

    @Test
    public void testGetOutputsForTask() throws FutureGatewayException {
        List<OutputFile> outputFiles = api.getOutputsForTask("1");
        assertEquals(3, outputFiles.size());

        OutputFile outputFile = outputFiles.get(0);
        assertEquals("sayhello.data", outputFile.getName());
        assertEquals(URI.create(
                "file?path=%2Ftmp%2Fba3a8d88-1e71-11e6-92fb-fa163e26496e"
                + "%2F1tmpba3a8d881e7111e692fbfa163e26496e_2&name=sayhello"
                + ".data"), outputFile.getUrl());
    }

    @Test
    public void testGetOutputsForTaskNotDone() throws FutureGatewayException {
        List<OutputFile> outputFiles = api.getOutputsForTask("2");
        assertEquals(0, outputFiles.size());
    }

    @Test
    public void testDeleteTask() throws FutureGatewayException {
        assertFalse(api.deleteTask("non-existing-task"));
        assertTrue(api.deleteTask("existing-task"));
    }

    @Test
    public void testDownloadOutputFile()
            throws FutureGatewayException, IOException {
        OutputFile outputFile = new OutputFile();
        outputFile.setName("test.txt");
        outputFile.setUrl(URI.create("file?path=%2Ftmp&name=test.txt"));

        File directory = new File(System.getProperty("java.io.tmpdir"));
        api.downloadOutputFile(outputFile, directory);

        File file = new File(directory, "test.txt");
        assertTrue(file.exists());
        assertEquals("TEST", FileUtils
                .readFileToString(file, Charset.defaultCharset()));
        file.delete();
    }

    @Test(expected = FutureGatewayException.class)
    public void testDownloadOutputFileNonExisting()
            throws FutureGatewayException, IOException {
        OutputFile outputFile = new OutputFile();
        outputFile.setName("test.txt");
        outputFile.setUrl(URI.create(
                "file?path=%2Ftmp" + "&name=non-existing-file"));

        File directory = new File(System.getProperty("java.io.tmpdir"));
        api.downloadOutputFile(outputFile, directory);
    }

    @Test(expected = IOException.class)
    public void testDownloadOutputFileNotDirectory()
            throws FutureGatewayException, IOException {
        File file = mock(File.class);
        when(file.exists()).thenReturn(true);
        when(file.isDirectory()).thenReturn(false);
        api.downloadOutputFile(new OutputFile(), file);
    }

    @Test(expected = IOException.class)
    public void testDownloadOutputFileCannotWrite()
            throws FutureGatewayException, IOException {
        File file = mock(File.class);
        when(file.exists()).thenReturn(true);
        when(file.isDirectory()).thenReturn(true);
        when(file.canWrite()).thenReturn(false);
        api.downloadOutputFile(new OutputFile(), file);
    }

    @Test(expected = IOException.class)
    public void testDownloadOutputFileCannotMkdir()
            throws FutureGatewayException, IOException {
        File file = mock(File.class);
        when(file.exists()).thenReturn(false);
        when(file.mkdirs()).thenReturn(false);
        api.downloadOutputFile(new OutputFile(), file);
    }

    @Test
    public void testCreateTask() throws FutureGatewayException {
        Task mockTask = MockRestSession.getMockTask();
        Task task = api.createTask(mockTask);
        assertEquals(mockTask, task);
    }

    @Test(expected = FutureGatewayException.class)
    public void testCreateTaskInvalidUser() throws FutureGatewayException {
        Task mockTask = new Task();
        mockTask.setUser("invalid-uri");
        api.createTask(mockTask);
    }

    @Test(expected = FutureGatewayException.class)
    public void testCreateTaskInvalidBody() throws FutureGatewayException {
        Task mockTask = new Task();
        mockTask.setUser("invalid-body");
        api.createTask(mockTask);
    }
}
