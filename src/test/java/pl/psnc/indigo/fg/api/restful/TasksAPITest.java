package pl.psnc.indigo.fg.api.restful;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.tomakehurst.wiremock.junit.WireMockRule;
import org.apache.commons.io.FileUtils;
import org.junit.Before;
import org.junit.Rule;
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

import javax.ws.rs.core.Response;
import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.nio.charset.Charset;
import java.util.Collections;
import java.util.List;

import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static com.github.tomakehurst.wiremock.client.WireMock.delete;
import static com.github.tomakehurst.wiremock.client.WireMock.get;
import static com.github.tomakehurst.wiremock.client.WireMock.patch;
import static com.github.tomakehurst.wiremock.client.WireMock.post;
import static com.github.tomakehurst.wiremock.client.WireMock.stubFor;
import static com.github.tomakehurst.wiremock.client.WireMock.urlEqualTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@Category(UnitTests.class)
public class TasksAPITest {
    @Rule
    public WireMockRule wireMockRule = new WireMockRule();

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
        String body = Helper.readResource("tasks.json");
        stubFor(get(urlEqualTo("/v1.0/tasks?user=all-tasks"))
                        .willReturn(aResponse().withBody(body)));

        List<Task> tasks = api.getAllTasks("all-tasks");
        assertThat(3, is(tasks.size()));

        Task task = tasks.get(0);
        assertThat(2, is(task.getOutputFiles().size()));
        assertThat(2, is(task.getLinks().size()));
    }

    @Test
    public final void testGetTask() throws Exception {
        String body = Helper.readResource("tasks_1.json");
        stubFor(get(urlEqualTo("/v1.0/tasks/1"))
                        .willReturn(aResponse().withBody(body)));

        Task task = api.getTask("1");
        assertThat("1", is(task.getId()));
        assertThat("2", is(task.getApplication()));
        assertThat("Test with files", is(task.getDescription()));
        assertThat("brunor", is(task.getUser()));
        assertThat(TaskStatus.DONE, is(task.getStatus()));

        List<OutputFile> outputFiles = task.getOutputFiles();
        assertThat(3, is(outputFiles.size()));

        OutputFile outputFile = outputFiles.get(0);
        assertThat("sayhello.data", is(outputFile.getName()));
        assertThat(URI.create(
                "file?path=%2Ftmp%2Fba3a8d88-1e71-11e6-92fb-fa163e26496e"
                + "%2F1tmpba3a8d881e7111e692fbfa163e26496e_2&name=sayhello"
                + ".data"), is(outputFile.getUrl()));
    }

    @Test(expected = FutureGatewayException.class)
    public final void testGetTaskInvalidUri() throws FutureGatewayException {
        stubFor(get(urlEqualTo("/v1.0/tasks/invalid-uri")).willReturn(
                aResponse().withStatus(
                        Response.Status.NOT_FOUND.getStatusCode())));
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
        stubFor(post(urlEqualTo("/v1.0/tasks/1/input?user=test")).willReturn(
                aResponse().withBody(mapper.writeValueAsString(new Upload()))));

        Task task = new Task();
        task.setId("1");
        task.setUser("test");

        File file = File.createTempFile("TasksAPITest", null);
        try {
            assertThat(api.uploadFileForTask(task, file),
                       not(is((Upload) null)));
        } finally {
            FileUtils.forceDelete(file);
        }
    }

    @Test(expected = FutureGatewayException.class)
    public final void testUploadFileForTaskInvalidUser() throws Exception {
        stubFor(post(urlEqualTo("/v1.0/tasks/invalid-uri/input?user=test"))
                        .willReturn(aResponse().withStatus(
                                Response.Status.NOT_FOUND.getStatusCode())));

        Task task = new Task();
        task.setId("invalid-uri");
        task.setUser("test");

        File file = File.createTempFile("TasksAPITest", null);
        try {
            api.uploadFileForTask(task, file);
        } finally {
            FileUtils.forceDelete(file);
        }
    }

    @Test(expected = FutureGatewayException.class)
    public final void testUploadFileForTaskInvalidBody() throws Exception {
        stubFor(post(urlEqualTo("/v1.0/tasks/invalid-body/input?user=test"))
                        .willReturn(aResponse().withBody("")));

        Task task = new Task();
        task.setId("invalid-body");
        task.setUser("test");

        File file = File.createTempFile("TasksAPITest", null);
        try {
            api.uploadFileForTask(task, file);
        } finally {
            FileUtils.forceDelete(file);
        }
    }

    @Test
    public final void testDeleteTask() throws FutureGatewayException {
        stubFor(delete(urlEqualTo("/v1.0/tasks/non-existing-task")).willReturn(
                aResponse().withStatus(
                        Response.Status.NOT_FOUND.getStatusCode())));
        stubFor(delete(urlEqualTo("/v1.0/tasks/existing-task")).willReturn(
                aResponse().withStatus(Response.Status.OK.getStatusCode())));

        assertThat(api.removeTask("non-existing-task"), is(false));
        assertThat(api.removeTask("existing-task"), is(true));
    }

    @Test
    public final void testDownloadOutputFile() throws Exception {
        stubFor(get(urlEqualTo("/v1.0/file?path=%2Ftmp&name=test.txt"))
                        .willReturn(aResponse().withBody("TEST")));

        OutputFile outputFile = new OutputFile();
        outputFile.setName("test.txt");
        outputFile.setUrl(URI.create("file?path=%2Ftmp&name=test.txt"));

        File directory = FileUtils.getTempDirectory();
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
        stubFor(get(urlEqualTo("/v1.0/file?path=%2Ftmp&name=non-existing-file"))
                        .willReturn(aResponse().withStatus(
                                Response.Status.NOT_FOUND.getStatusCode())));

        OutputFile outputFile = new OutputFile();
        outputFile.setName("test.txt");
        outputFile
                .setUrl(URI.create("file?path=%2Ftmp&name=non-existing-file"));

        File directory = FileUtils.getTempDirectory();
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
    public final void testCreateTask() throws Exception {
        Task task = new Task();
        task.setUser("test");
        task.setApplication("1");
        task.setDescription("hello");

        stubFor(post(urlEqualTo("/v1.0/tasks?user=test")).willReturn(
                aResponse().withBody(mapper.writeValueAsString(task))));

        Task taskFG = api.createTask(task);
        assertThat(taskFG, is(task));
    }

    @Test(expected = FutureGatewayException.class)
    public final void testCreateTaskInvalidUser()
            throws FutureGatewayException {
        stubFor(post(urlEqualTo("/v1.0/tasks?user=invalid-user")).willReturn(
                aResponse().withStatus(
                        Response.Status.BAD_REQUEST.getStatusCode())));

        Task task = new Task();
        task.setUser("invalid-user");
        task.setApplication("1");
        task.setDescription("hello");

        api.createTask(task);
    }

    @Test(expected = FutureGatewayException.class)
    public final void testCreateTaskInvalidBody()
            throws FutureGatewayException {
        stubFor(post(urlEqualTo("/v1.0/tasks?user=invalid-body"))
                        .willReturn(aResponse().withBody("")));

        Task mockTask = new Task();
        mockTask.setUser("invalid-body");
        api.createTask(mockTask);
    }

    @Test
    public final void testPatchRuntimeData() throws Exception {
        /*
         * Before PATCH
         */
        String body = Helper.readResource("tasks_3.json");
        stubFor(get(urlEqualTo("/v1.0/tasks/3"))
                        .willReturn(aResponse().withBody(body)));
        stubFor(patch(urlEqualTo("/v1.0/tasks/3")).willReturn(
                aResponse().withStatus(Response.Status.OK.getStatusCode())));

        PatchRuntimeData patchRuntimeData = new PatchRuntimeData();
        patchRuntimeData.setRuntimeData(Collections.singletonList(
                new PatchRuntimeData.KeyValue("name", "value")));

        Task task = api.getTask("3");
        assertThat(task.getRuntimeData().isEmpty(), is(true));
        api.patchRuntimeData(task.getId(), patchRuntimeData);

        /*
         * After PATCH
         */
        String patchedBody = Helper.readResource("tasks_3_patched.json");
        stubFor(get(urlEqualTo("/v1.0/tasks/3"))
                        .willReturn(aResponse().withBody(patchedBody)));

        task = api.getTask(task.getId());
        assertThat(task.getRuntimeData().size(), is(1));
        RuntimeData runtimeData = task.getRuntimeData().get(0);
        assertThat(runtimeData.getName(), is("name"));
        assertThat(runtimeData.getValue(), is("value"));
    }
}
