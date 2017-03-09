package pl.psnc.indigo.fg.api.restful;

import org.apache.commons.io.FileUtils;
import org.junit.Before;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import pl.psnc.indigo.fg.api.restful.category.UnitTests;
import pl.psnc.indigo.fg.api.restful.exceptions.FutureGatewayException;
import pl.psnc.indigo.fg.api.restful.jaxb.OutputFile;
import pl.psnc.indigo.fg.api.restful.jaxb.PatchRuntimeData;
import pl.psnc.indigo.fg.api.restful.jaxb.RuntimeData;
import pl.psnc.indigo.fg.api.restful.jaxb.Task;
import pl.psnc.indigo.fg.api.restful.jaxb.TaskStatus;
import pl.psnc.indigo.fg.api.restful.jaxb.Upload;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.nio.charset.Charset;
import java.util.Collections;
import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@Category(UnitTests.class)
public class TasksAPITest {
    private TasksAPI api;

    @Before
    public final void before() throws IOException, FutureGatewayException {
        MockRestSession session = new MockRestSession();
        api = new TasksAPI(MockRestSession.MOCK_ADDRESS, session.getClient(),
                           "");
    }

    @Test
    public final void testGetAllTasks() throws FutureGatewayException {
        List<Task> tasks = api.getAllTasks("all-tasks");
        assertThat(3, is(tasks.size()));

        Task task = tasks.get(0);
        assertThat(2, is(task.getOutputFiles().size()));
        assertThat(2, is(task.getLinks().size()));
    }

    @Test
    public final void testGetTask() throws FutureGatewayException {
        Task task = api.getTask("1");
        assertThat("1", is(task.getId()));
        assertThat("2", is(task.getApplication()));
        assertThat("Test with files", is(task.getDescription()));
        assertThat("brunor", is(task.getUser()));
        assertThat(TaskStatus.DONE, is(task.getStatus()));
    }

    @Test(expected = FutureGatewayException.class)
    public final void testGetTaskInvalidUri() throws FutureGatewayException {
        api.getTask("invalid-uri");
    }

    @Test(expected = FutureGatewayException.class)
    public final void testGetTaskInvalidBody() throws FutureGatewayException {
        api.getTask("invalid-body");
    }

    @Test
    public final void testUploadFileForTask() throws FutureGatewayException {
        Task task = new Task();
        task.setUser("brunor");
        task.setId("1");

        File file = mock(File.class);
        assertThat(api.uploadFileForTask(task, file), not(is((Upload) null)));
    }

    @Test(expected = FutureGatewayException.class)
    public final void testUploadFileForTaskInvalidUser()
            throws FutureGatewayException {
        Task task = new Task();
        task.setUser("invalid-uri");
        task.setId("1");

        File file = mock(File.class);
        api.uploadFileForTask(task, file);
    }

    @Test(expected = FutureGatewayException.class)
    public final void testUploadFileForTaskInvalidBody()
            throws FutureGatewayException {
        Task task = new Task();
        task.setUser("invalid-body");
        task.setId("1");

        File file = mock(File.class);
        api.uploadFileForTask(task, file);
    }

    @Test
    public final void testGetOutputsForTask() throws FutureGatewayException {
        List<OutputFile> outputFiles = api.getTask("1").getOutputFiles();
        assertThat(3, is(outputFiles.size()));

        OutputFile outputFile = outputFiles.get(0);
        assertThat("sayhello.data", is(outputFile.getName()));
        assertThat(URI.create(
                "file?path=%2Ftmp%2Fba3a8d88-1e71-11e6-92fb-fa163e26496e"
                + "%2F1tmpba3a8d881e7111e692fbfa163e26496e_2&name=sayhello"
                + ".data"), is(outputFile.getUrl()));
    }

    @Test
    public final void testGetOutputsForTaskNotDone()
            throws FutureGatewayException {
        List<OutputFile> outputFiles = api.getTask("2").getOutputFiles();
        assertThat(outputFiles.size(), is(3));
    }

    @Test
    public final void testDeleteTask() {
        assertThat(api.removeTask("non-existing-task"), is(false));
        assertThat(api.removeTask("existing-task"), is(true));
    }

    @Test
    public final void testDownloadOutputFile() throws Exception {
        OutputFile outputFile = new OutputFile();
        outputFile.setName("test.txt");
        outputFile.setUrl(URI.create("file?path=%2Ftmp&name=test.txt"));

        File directory = new File(System.getProperty("java.io.tmpdir"));
        api.downloadOutputFile(outputFile, directory);

        File file = new File(directory, "test.txt");
        assertThat(file.exists(), is(true));
        assertThat("TEST", is(FileUtils.readFileToString(file,
                                                         Charset.defaultCharset())));
        assertThat(file.delete(), is(true));
    }

    @Test(expected = FutureGatewayException.class)
    public final void testDownloadOutputFileNonExisting()
            throws FutureGatewayException {
        OutputFile outputFile = new OutputFile();
        outputFile.setName("test.txt");
        outputFile.setUrl(URI.create(
                "file?path=%2Ftmp" + "&name=non-existing-file"));

        File directory = new File(System.getProperty("java.io.tmpdir"));
        api.downloadOutputFile(outputFile, directory);
    }

    @Test(expected = FutureGatewayException.class)
    public final void testDownloadOutputFileNotDirectory()
            throws FutureGatewayException {
        File file = mock(File.class);
        when(file.exists()).thenReturn(true);
        when(file.isDirectory()).thenReturn(false);
        api.downloadOutputFile(new OutputFile(), file);
    }

    @Test(expected = FutureGatewayException.class)
    public final void testDownloadOutputFileCannotWrite()
            throws FutureGatewayException {
        File file = mock(File.class);
        when(file.exists()).thenReturn(true);
        when(file.isDirectory()).thenReturn(true);
        when(file.canWrite()).thenReturn(false);
        api.downloadOutputFile(new OutputFile(), file);
    }

    @Test(expected = FutureGatewayException.class)
    public final void testDownloadOutputFileCannotMkdir()
            throws FutureGatewayException {
        File file = mock(File.class);
        when(file.exists()).thenReturn(false);
        when(file.mkdirs()).thenReturn(false);
        api.downloadOutputFile(new OutputFile(), file);
    }

    @Test
    public final void testCreateTask() throws FutureGatewayException {
        Task mockTask = MockRestSession.mockTask();
        Task task = api.createTask(mockTask);
        assertThat(mockTask, is(task));
    }

    @Test(expected = FutureGatewayException.class)
    public final void testCreateTaskInvalidUser()
            throws FutureGatewayException {
        Task mockTask = new Task();
        mockTask.setUser("invalid-uri");
        api.createTask(mockTask);
    }

    @Test(expected = FutureGatewayException.class)
    public final void testCreateTaskInvalidBody()
            throws FutureGatewayException {
        Task mockTask = new Task();
        mockTask.setUser("invalid-body");
        api.createTask(mockTask);
    }

    @Test
    public final void testPatchRuntimeData() throws FutureGatewayException {
        PatchRuntimeData patchRuntimeData = new PatchRuntimeData();
        patchRuntimeData.setRuntimeData(Collections.singletonList(
                new PatchRuntimeData.KeyValue("name", "value")));

        Task task = api.getTask("3");
        assertThat(task.getRuntimeData().isEmpty(), is(true));
        api.patchRuntimeData(task.getId(), patchRuntimeData);
        task = api.getTask(task.getId());
        assertThat(task.getRuntimeData().size(), is(1));
        RuntimeData runtimeData = task.getRuntimeData().get(0);
        assertThat(runtimeData.getName(), is("name"));
        assertThat(runtimeData.getValue(), is("value"));
    }
}
