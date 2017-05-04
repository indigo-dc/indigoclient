package pl.psnc.indigo.fg.api.restful;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.tomakehurst.wiremock.junit.WireMockRule;
import org.apache.commons.io.FileUtils;
import org.apache.http.HttpStatus;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import pl.psnc.indigo.fg.api.restful.category.UnitTests;
import pl.psnc.indigo.fg.api.restful.exceptions.FutureGatewayException;
import pl.psnc.indigo.fg.api.restful.jaxb.KeyValue;
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

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@Category(UnitTests.class)
public class TasksAPITest {
    @Rule public final WireMockRule wireMockRule = new WireMockRule();

    private ObjectMapper mapper;
    private TasksAPI api;

    @Before
    public final void setup() throws IOException, FutureGatewayException {
        stubFor(get(urlEqualTo("/")).willReturn(
                aResponse().withBody(Helper.readResource("root.json"))));

        mapper = new ObjectMapper();
        api = new TasksAPI(URI.create("http://localhost:8080/"), "");
    }

    @Test
    public final void testGetAllTasks() throws Exception {
        final String body = Helper.readResource("tasks.json");
        stubFor(get(urlEqualTo("/v1.0/tasks"))
                        .willReturn(aResponse().withBody(body)));

        final List<Task> tasks = api.getAllTasks();
        assertEquals(3, tasks.size());

        final Task task = tasks.get(0);
        assertEquals(2, task.getOutputFiles().size());
        assertEquals(2, task.getLinks().size());
    }

    @Test
    public final void testGetTask() throws Exception {
        final String body = Helper.readResource("tasks_1.json");
        stubFor(get(urlEqualTo("/v1.0/tasks/1"))
                        .willReturn(aResponse().withBody(body)));

        final Task task = api.getTask("1");
        assertEquals("1", task.getId());
        assertEquals("2", task.getApplication());
        assertEquals("Test with files", task.getDescription());
        assertEquals("brunor", task.getUser());
        assertEquals(TaskStatus.DONE, task.getStatus());

        final List<OutputFile> outputFiles = task.getOutputFiles();
        assertEquals(3, outputFiles.size());

        final OutputFile outputFile = outputFiles.get(0);
        assertEquals("sayhello.data", outputFile.getName());
        assertEquals(URI.create(
                "file?path=%2Ftmp%2Fba3a8d88-1e71-11e6-92fb-fa163e26496e" +
                "%2F1tmpba3a8d881e7111e692fbfa163e26496e_2&name=sayhello" +
                ".data"), outputFile.getUrl());
    }

    @Test(expected = FutureGatewayException.class)
    public final void testGetTaskInvalidUri() throws FutureGatewayException {
        stubFor(get(urlEqualTo("/v1.0/tasks/invalid-uri")).willReturn(
                aResponse().withStatus(HttpStatus.SC_NOT_FOUND)));
        api.getTask("invalid-uri");
    }

    @Test(expected = FutureGatewayException.class)
    public final void testGetTaskInvalidBody() throws FutureGatewayException {
        stubFor(get(urlEqualTo("/v1.0/tasks/invalid-body"))
                        .willReturn(aResponse().withBody("")));
        api.getTask("invalid-body");
    }

    @Test
    public final void testUploadFileForTask() throws Exception {
        stubFor(post(urlEqualTo("/v1.0/tasks/1/input")).willReturn(
                aResponse().withBody(mapper.writeValueAsString(new Upload()))));

        final Task task = new Task();
        task.setId("1");

        final File file = File.createTempFile("TasksAPITest", null);
        try {
            assertNotNull(api.uploadFileForTask(task, file));
        } finally {
            FileUtils.forceDelete(file);
        }
    }

    @Test
    public final void testDeleteTask() throws FutureGatewayException {
        stubFor(delete(urlEqualTo("/v1.0/tasks/non-existing-task")).willReturn(
                aResponse().withStatus(HttpStatus.SC_NOT_FOUND)));
        stubFor(delete(urlEqualTo("/v1.0/tasks/existing-task"))
                        .willReturn(aResponse().withStatus(HttpStatus.SC_OK)));

        assertFalse(api.removeTask("non-existing-task"));
        assertTrue(api.removeTask("existing-task"));
    }

    @Test
    public final void testDownloadOutputFile() throws Exception {
        stubFor(get(urlEqualTo("/v1.0/file?path=%2Ftmp&name=test.txt"))
                        .willReturn(aResponse().withBody("TEST")));

        final OutputFile outputFile = new OutputFile();
        outputFile.setName("test.txt");
        outputFile.setUrl(URI.create("file?path=%2Ftmp&name=test.txt"));

        final File directory = FileUtils.getTempDirectory();
        api.downloadOutputFile(outputFile, directory);

        final File file = new File(directory, "test.txt");
        assertTrue(file.exists());
        assertEquals("TEST", FileUtils
                .readFileToString(file, Charset.defaultCharset()));
        assertTrue(file.delete());
    }

    @Test(expected = FutureGatewayException.class)
    public final void testDownloadOutputFileNonExisting()
            throws FutureGatewayException {
        stubFor(get(urlEqualTo("/v1.0/file?path=%2Ftmp&name=non-existing-file"))
                        .willReturn(aResponse().withStatus(
                                HttpStatus.SC_NOT_FOUND)));

        final OutputFile outputFile = new OutputFile();
        outputFile.setName("test.txt");
        outputFile
                .setUrl(URI.create("file?path=%2Ftmp&name=non-existing-file"));

        final File directory = FileUtils.getTempDirectory();
        api.downloadOutputFile(outputFile, directory);
    }

    @Test(expected = FutureGatewayException.class)
    public final void testDownloadOutputFileNotDirectory()
            throws FutureGatewayException {
        final File file = mock(File.class);
        when(file.exists()).thenReturn(true);
        when(file.isDirectory()).thenReturn(false);
        api.downloadOutputFile(new OutputFile(), file);
    }

    @Test(expected = FutureGatewayException.class)
    public final void testDownloadOutputFileCannotWrite()
            throws FutureGatewayException {
        final File file = mock(File.class);
        when(file.exists()).thenReturn(true);
        when(file.isDirectory()).thenReturn(true);
        when(file.canWrite()).thenReturn(false);
        api.downloadOutputFile(new OutputFile(), file);
    }

    @Test(expected = FutureGatewayException.class)
    public final void testDownloadOutputFileCannotMkdir()
            throws FutureGatewayException {
        final File file = mock(File.class);
        when(file.exists()).thenReturn(false);
        when(file.mkdirs()).thenReturn(false);
        api.downloadOutputFile(new OutputFile(), file);
    }

    @Test
    public final void testCreateTask() throws Exception {
        final Task task = new Task();
        task.setApplication("1");
        task.setDescription("hello");

        stubFor(post(urlEqualTo("/v1.0/tasks")).willReturn(
                aResponse().withBody(mapper.writeValueAsString(task))));

        final Task taskFG = api.createTask(task);
        assertEquals(task, taskFG);
    }

    @Test
    public final void testPatchRuntimeData() throws Exception {
        /*
         * Before PATCH
         */
        final String body = Helper.readResource("tasks_3.json");
        stubFor(get(urlEqualTo("/v1.0/tasks/3"))
                        .willReturn(aResponse().withBody(body)));
        stubFor(patch(urlEqualTo("/v1.0/tasks/3"))
                        .willReturn(aResponse().withStatus(HttpStatus.SC_OK)));

        final PatchRuntimeData patchRuntimeData = new PatchRuntimeData();
        patchRuntimeData.setRuntimeData(
                Collections.singletonList(new KeyValue("name", "value")));

        Task task = api.getTask("3");
        assertTrue(task.getRuntimeData().isEmpty());
        api.patchRuntimeData(task.getId(), patchRuntimeData);

        /*
         * After PATCH
         */
        final String patchedBody = Helper.readResource("tasks_3_patched.json");
        stubFor(get(urlEqualTo("/v1.0/tasks/3"))
                        .willReturn(aResponse().withBody(patchedBody)));

        task = api.getTask(task.getId());
        assertEquals(1, task.getRuntimeData().size());
        final RuntimeData runtimeData = task.getRuntimeData().get(0);
        assertEquals("name", runtimeData.getName());
        assertEquals("value", runtimeData.getValue());
    }
}
